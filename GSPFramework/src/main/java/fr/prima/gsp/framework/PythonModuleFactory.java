/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.prima.gsp.framework;

import com.heeere.python27.PyCompilerFlags;
import com.heeere.python27.PyMethodDef;
import com.heeere.python27.PyObject;
import static com.heeere.python27.Python27Library.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.bridj.BridJ;
import org.bridj.CLong;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author remonet
 *
 *
 * TODO: each call to python should test for return and print/clear the errors
 */
class PythonModuleFactory {

    private boolean inited = false;
    Pointer<PyObject> pyTrue;
    Pointer<PyObject> pyFalse;
    Pointer<PyObject> pyBool;
    Pointer<PyObject> pyTuple;
    Pointer<PyObject> pyGSP;
    public static PythonModuleFactory instance = null; // dirty...

    private void lazyInit() {
        if (inited) {
            return;
        }
        // Need LD_PRELOAD...
        BridJ.addNativeLibraryAlias("python27", "python2.7");
        Py_Initialize();
        { // get some built-in, optimistic (no checks)
            Pointer<PyObject> dict = PyImport_GetModuleDict();
            Pointer<PyObject> builtin = PyDict_GetItemString(dict, s("__builtin__"));
            pyTrue = PyObject_GetAttrString(builtin, s("True"));
            pyFalse = PyObject_GetAttrString(builtin, s("False"));
            pyBool = PyObject_Type(pyTrue);
            pyTuple = PyObject_GetAttrString(builtin, s("tuple"));
        }
        { // load the GSP framework part written in python
            int res = PyRun_SimpleStringFlags(s(getPythonCode()), flags(Py_file_input));
            if (res != 0) {
                PyErr_Print();// ??? does it work for non-exceptions?
                throw new RuntimeException("Error while executing gsp internal python functions (gsp.py)");
            }
            Pointer<PyObject> dict = PyModule_GetDict(PyImport_AddModule(s("__main__")));
            pyGSP = PyDict_GetItemString(dict, s("GSP"));
        }
        instance = this;
        inited = true;
        PyEval_ReleaseLock();
    }
    // NOTE: what is called a module in Python (that can be imported) is called a Bundle here, to avoid conflict with PythonModule (for the gsp)
    Map<String, Bundle> bundles = new HashMap<String, Bundle>();
    List<Module> modules = new LinkedList<Module>();

    private Pointer<PyCompilerFlags> flags(int i) {
        PyCompilerFlags res = new PyCompilerFlags();
        res.cf_flags(i);
        return Pointer.pointerTo(res);
    }

    private String getPythonCode() {
        return new Scanner(PythonModuleFactory.class.getResourceAsStream("/gsp.py"), "UTF-8").useDelimiter("\\A").next();
    }

    public Module createModule(String pythonModuleName, String typeName) {
        lazyInit();
        Bundle bundle = getBundle(pythonModuleName);
        PythonModule module = new PythonModule(bundle, pythonModuleName, typeName);
        modules.add(module);
        return module;
    }

    private Bundle getBundle(String pythonModuleName) {
        Bundle bundle = bundles.get(pythonModuleName);
        if (bundle == null) {
            bundle = new Bundle(pythonModuleName);
            bundles.put(pythonModuleName, bundle);
        }
        return bundle;
    }

    public static Pointer<Byte> s(String string) {
        return Pointer.pointerToCString(string);
    }

    public static Pointer<PyObject> sp(String string) {
        return Py_BuildValue(s("s"), s(string));
    }

    public static Pointer<PyObject> pyNone() {
        return Py_BuildValue(s(""));
    }

    private Pointer<PyObject> pyGSP(String methodName) {
        Pointer<PyObject> res = PyObject_GetAttrString(pyGSP, s(methodName));
        if (res == Pointer.NULL) {
            PyErr_Print();
        }
        return res;
    }

    Pointer<PyObject> pyHasLockMakeTupleNoErrHandling(String s) {
        Pointer<PyObject> res = PyObject_CallFunctionObjArgs(pyGSP("makeTuple"), sp(s), null);
        return res;
    }

    boolean pyIsStructureOrArray(Pointer<PyObject> pypt) {
        IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
        boolean res = PyObject_Compare(pyTrue, PyObject_CallFunctionObjArgs(pyGSP("isStructureOrArray"), pypt, null)) == 0;
        PyGILState_Release(state);
        return res;
    }

    long pyCAddress(Pointer<PyObject> pypt) {
        ValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
        long res = PyInt_AsSsize_t(PyObject_CallFunctionObjArgs(pyGSP("cAddress"), pypt, null));
        return res;
    }

    String pyCClassName(Pointer<PyObject> pypt) {
        ValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
        String res = PyString_AsStringJava(PyObject_CallFunctionObjArgs(pyGSP("cClassName"), pypt, null));
        return res;
    }

    Object pySimpleTypeToJava(Pointer<PyObject> pypt) {
        IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
        try {
            String typeString = PyString_AsStringJava(PyObject_CallFunctionObjArgs(pyGSP("typeString"), pypt, null));
            // TODO maybe redo with a Map and some kind of java8 closures
            if ("NoneType".equals(typeString)) {
                return null;
            } else if ("int".equals(typeString)) {
                // bypass wrapper problem (maybe todo change it)
                return Long.parseLong(PyString_AsStringJava(PyObject_CallFunctionObjArgs(pyGSP("valueString"), pypt, null)));
                //return sizeAsLong(PyInt_AsSsize_t(pypt));
            } else if ("float".equals(typeString)) {
                return PyFloat_AsDouble(pypt);
            } else if ("str".equals(typeString)) {
                return PyString_AsStringJava(pypt);
            } else if ("bool".equals(typeString)) {
                return PyObject_Compare(pyTrue, pypt) == 0;
            } else {
                return null;
            }
        } finally {
            PyGILState_Release(state);
        }
    }

    boolean pyIsString(Pointer<PyObject> pypt) {
        return PyObject_Compare(pyTrue, PyObject_CallFunctionObjArgs(pyGSP("isString"), pypt, null)) == 0;
    }

    private static class Bundle {

        String name;
        Pointer<PyObject> pythonModule;
        Pointer<PyObject> pythonModuleDict;

        private Bundle(String pythonModuleName) {
            this.name = pythonModuleName;
            this.pythonModule = PyImport_ImportModule(s(pythonModuleName));
            if (this.pythonModule == Pointer.NULL) {
                PyErr_Print();
                throw new RuntimeException("Error while importing python module/file '" + pythonModuleName + "'");
            }
            this.pythonModuleDict = PyModule_GetDict(this.pythonModule);
            if (this.pythonModule == Pointer.NULL) {
                PyErr_Print();
                throw new RuntimeException("Could not get dict for python module/file '" + pythonModuleName + "'");
            }
        }
    }

    public static abstract class FrameworkCallback extends org.bridj.Callback {

        public abstract Pointer<PyObject> callback(Pointer<PyObject> self, Pointer<PyObject> args, Pointer<PyObject> keywds);
        /*  static PyObject *keywdarg_parrot(PyObject *self, PyObject *args, PyObject *keywds)  */
    }

    private class PythonModule extends BaseAbstractModule {

        Bundle bundle;
        String pyClassName;
        //Pointer<PyObject> pyClass;
        Pointer<PyObject> pyClassInstance;
        FrameworkCallback frameworkCallback;
        PyMethodDef callbackMethodDef = new PyMethodDef();
        Pointer<PyObject> callbackMethodObject;

        public PythonModule(Bundle bundle, String pythonModuleName, String typeName) {
            this.bundle = bundle;
            this.pyClassName = typeName;
            IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
            Pointer<PyObject> pyClass = PyDict_GetItemString(bundle.pythonModuleDict, s(typeName));
            if (pyClass == Pointer.NULL) {
                PyErr_Print();
                throw new RuntimeException("In python module '" + bundle.name + "' could not find class '" + typeName + "'");
            }
            pyClassInstance = PyObject_CallFunctionObjArgs(pyClass, (Object) null);
            if (pyClassInstance == Pointer.NULL) {
                PyErr_Print();
                throw new RuntimeException("In python module '" + bundle.name + "' could not invoke contructor for class '" + typeName + "'");
            }
            frameworkCallback = new FrameworkCallback() {
                @Override
                public Pointer<PyObject> callback(Pointer<PyObject> self, Pointer<PyObject> args, Pointer<PyObject> keywds) {
                    pythonCallback(self, args);
                    return pyNone();
                }
            };
            callbackMethodDef = new PyMethodDef();
            callbackMethodDef.ml_name(s("emitNamedEvent"));
            callbackMethodDef.ml_meth(frameworkCallback.toPointer());
            callbackMethodDef.ml_flags(METH_VARARGS);
            callbackMethodObject = PyCFunction_NewEx(Pointer.pointerTo(callbackMethodDef), pyClassInstance, pyNone());
            PyObject_SetAttrString(pyClassInstance, s("emitNamedEvent"), callbackMethodObject);
            PyGILState_Release(state);
        }

        @Override
        protected void initModule() {
            callIfExists("initModule");
        }

        @Override
        protected void stopModule() {
            callIfExists("stopModule");
        }

        private String repeat(String what, int times) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < times; i++) {
                res.append(what);
            }
            return res.toString();
        }

        private void pythonCallback(Pointer<PyObject> self, Pointer<PyObject> args) {
            IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
            int nArgs = nArgs(args);
            Pointer<CLong>[] parametersPointers = new Pointer[nArgs];
            for (int i = 0; i < parametersPointers.length; i++) {
                parametersPointers[i] = Pointer.allocateCLong();
            }
            int res;
            res = PyArg_ParseTuple(args, s(repeat("O", nArgs)), (Object[]) parametersPointers);
            Pointer<PyObject> obj = (Pointer<PyObject>) Pointer.pointerToAddress(parametersPointers[0].getCLong());
            String eventName = PyString_AsStringJava(obj);
            PythonPointer[] eventParameters = new PythonPointer[nArgs - 1];
            for (int i = 0; i < eventParameters.length; i++) {
                eventParameters[i] = new PythonPointer((Pointer<PyObject>) Pointer.pointerToAddress(parametersPointers[i + 1].getCLong()));
            }
            emitNamedEvent(eventName, (Object[]) eventParameters);
            PyGILState_Release(state);
        }

        public EventReceiver getEventReceiver(String portName) {
            final Pointer<PyObject> method = PyObject_GetAttrString(pyClassInstance, s(portName));
            if (method == Pointer.NULL) {
                PyErr_Print();
                throw new RuntimeException("In gsp python module of type '" + bundle.name + "." + pyClassName + "', could not find input with name '" + portName + "'");
            }
            return new EventReceiver() {
                public void receiveEvent(Event e) {
                    IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
                    Pointer<PyObject> res = PyObject_CallFunctionObjArgs(method, eventToParameters(e));
                    if (res == Pointer.NULL) {
                        PyErr_Print();
                    }
                    PyGILState_Release(state);
                }
            };
        }

        private Object[] eventToParameters(Event e) {
            Object[] ei = e.getInformation();
            Object[] res = new Object[ei.length + 1]; // add a null at the end
            for (int c = 0; c < ei.length; c++) {
                res[c] = getPythonView(ei[c]);
            }
            return res;
        }

        public void addConnector(String portName, EventReceiver eventReceiver) {
            listenersFor(portName).add(eventReceiver);
        }

        public void configure(Element conf) {
            NamedNodeMap attributes = conf.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                String parameterName = node.getNodeName();
                if (CModuleFactory.nonParameters.contains(parameterName)) {
                    continue;
                }
                String text = node.getTextContent();
                setParameter(parameterName, text);
            }
        }

        private void setParameter(String parameterName, String text) {
            IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
            try {
                Pointer<PyObject> attr = PyObject_GetAttrString(pyClassInstance, s(parameterName));
                if (attr == null) {
                    throw new RuntimeException("Could not find attribute '" + parameterName + "' in python object of type '" + bundle.name + "." + pyClassName + "'");
                } else {
                    Pointer<PyObject> attrType = PyObject_Type(attr);
                    Pointer<PyObject> newVal;
                    if (attrType.equals(pyBool)) { // handle the Boolean case
                        newVal = Boolean.parseBoolean(text) ? pyTrue : pyFalse;
                    } else if (attrType.equals(pyTuple)) { // special case for tuples
                        newVal = pyHasLockMakeTupleNoErrHandling(text);
                        if (newVal == Pointer.NULL) {
                            PyErr_Print();
                            throw new RuntimeException("Could not make tuple for attribute '" + parameterName + "' from value '" + text + "' (in python object of type '" + bundle.name + "." + pyClassName + "')");
                        }
                    } else {
                        newVal = PyObject_CallFunctionObjArgs(attrType, sp(text), null);
                        if (newVal == Pointer.NULL) {
                            PyErr_Print();
                            throw new RuntimeException("Could not call constructor for attribute '" + parameterName + "' from value '" + text + "' (in python object of type '" + bundle.name + "." + pyClassName + "')");
                        }
                    }
                    PyObject_SetAttrString(pyClassInstance, s(parameterName), newVal);
                    String cbName = parameterName + "Changed";
                    Pointer<PyObject> notificationMethod = PyObject_GetAttrString(pyClassInstance, s(cbName));
                    if (notificationMethod == Pointer.NULL) {
                        PyErr_Clear();
                    } else {
                        Pointer<PyObject> res = PyObject_CallFunctionObjArgs(notificationMethod, attr, newVal, null);
                        if (res == Pointer.NULL) {
                            PyErr_Print();
                            throw new RuntimeException("Problem while calling callback for attribute '" + parameterName + "' (callback is " + cbName + ") (in python object of type '" + bundle.name + "." + pyClassName + "')");
                        }
                    }
                    // TODO, maybe use eval as a last resort
                }
            } finally {
                PyGILState_Release(state);
            }
        }

        private void callIfExists(String methodName) {
            IntValuedEnum<PyGILState_STATE> state = PyGILState_Ensure();
            Pointer<PyObject> method = PyObject_GetAttrString(pyClassInstance, s(methodName));
            if (method != Pointer.NULL) {
                PyObject_CallFunctionObjArgs(method, (Object) null);
            } else {
                PyErr_Clear();
            }
            PyGILState_Release(state);
        }

        private int nArgs(Pointer<PyObject> args) {
            long nArgs = PyTuple_Size(args);
            return (int) nArgs;
        }
    }

    private static Map<Class, String> typeToBuildValueString = new IdentityHashMap<Class, String>() {
        {
            put(Integer.class, "i");
            put(Long.class, "l");
            put(Short.class, "h");
            put(Float.class, "f");
            put(Double.class, "d");
        }
    };

    private static Object getPythonView(Object o) {
        if (o == null) {
            return pyNone();
        } else if (o instanceof String) {
            return Py_BuildValue(s("s"), s((String) o));
        } else if (o instanceof PythonPointer) {
            return ((PythonPointer) o).pointer;
        } else if (o instanceof NativePointer) {
            NativePointer np = (NativePointer) o;
            if (NativePointerUtils.isNull(np)) {
                return pyNone();
            } else {
                return Py_BuildValue(s("l"), NativePointerUtils.address((NativePointer) o));
            }
        } else {
            String pyBuildString = typeToBuildValueString.get(o.getClass());
            if (pyBuildString != null) {
                return Py_BuildValue(s(pyBuildString), o);
            } else {
                // wtd? TODO
                System.err.println("UNHANDLED TYPE " + o.getClass());
                return null;
            }
        }
    }

    public Pointer<Byte> PyString_AsCString(Pointer<PyObject> str) {
        return PyString_AsString(str);
    }

    private String PyString_AsStringJava(Pointer<PyObject> str) {
        Pointer<Byte> arr = PyString_AsString(str);
        return arr.getCString();
    }
}
