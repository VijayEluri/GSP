/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.prima.gsp.framework.nativeutil;

import com.bridj.BridJ;
import com.bridj.Demangler.DemanglingException;
import com.bridj.Demangler.MemberRef;
import com.bridj.Demangler.TypeRef;
import com.bridj.cpp.GCC4Demangler;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author remonet
 */
public abstract class NativeSymbolDemangler {

    public static NativeSymbolDemangler create() {
        return createBridjGccBasedDemangler();
    }

    public static class Info {
        public String mangledName;
        public String name;
        public String[] fullName;
        public NativeType returnType;
        public NativeType[] parameterTypes;
    }

    // API
    public final Info demangle(String libraryName, String symbol) {
        return demangleImpl(libraryName, symbol);
    }
    
    // SPI
    protected abstract Info demangleImpl(String libraryName, String symbol);

    // inner
    static NativeSymbolDemangler createBridjGccBasedDemangler() {
        return new NativeSymbolDemangler() {

            @Override
            protected Info demangleImpl(String libraryName, String symbol) {
                try {
                    Info res = new Info();
                    res.mangledName = symbol;
                    GCC4Demangler dem = new GCC4Demangler(BridJ.getNativeLibrary(libraryName), symbol);
                    MemberRef parsed = dem.parseSymbol();
                    if (parsed == null || parsed.paramTypes == null) {
                        // not a c++ thing?
                        return null;
                    }
                    res.fullName = fullName(parsed.getMemberName());
                    res.name = res.fullName[res.fullName.length-1];
                    res.parameterTypes = getParameterTypes(parsed.paramTypes);
                    return res;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NativeSymbolDemangler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DemanglingException ex) {
                    //Logger.getLogger(NativeSymbolDemangler.class.getName()).log(Level.SEVERE, null, ex);
                    // TODO could log for demangler improvement
                }
                return null;
            }

            private String[] fullName(Object memberName) {
                if (memberName instanceof List) {
                    List<?> l = (List<?>) memberName;
                    String[] res = new String[l.size()];
                    for (int i = 0; i < res.length; i++) {
                        res[i] = (String) l.get(i);
                    }
                    return res;
                } else if (memberName instanceof String) {
                    return new String[]{(String) memberName};
                } else {
                    throw new IllegalArgumentException("Wrong type " + memberName.getClass());
                }
            }


            private NativeType[] getParameterTypes(TypeRef[] paramTypes) {
                NativeType[] res = new NativeType[paramTypes.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = getParameterType(paramTypes[i]);
                }
                return res;
            }

            private final Map<String, NativeType> nativeTypes = new HashMap<String, NativeType>() {{
                put("int", NativeType.INT);
                put("byte", NativeType.CHAR);
                put("float", NativeType.FLOAT);
                put("boolean", NativeType.BOOL);
                put("com.bridj.Pointer", NativeType.VOID_POINTER);
            }};

            private NativeType getParameterType(TypeRef typeRef) {
                String pType = typeRef.getQualifiedName(new StringBuilder(), true).toString();
                NativeType res = nativeTypes.get(pType);
                if (res == null) {
                    throw new UnsupportedOperationException("TODO for type '" + pType + "'");
                }
                return res;
            }

        };
    }

}