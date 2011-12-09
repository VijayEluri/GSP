package org.bridj.demangling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bridj.CLong;
import org.bridj.NativeLibrary;
import org.bridj.demangling.Demangler.ClassRef;
import org.bridj.demangling.Demangler.DemanglingException;
import org.bridj.demangling.Demangler.Ident;
import org.bridj.demangling.Demangler.MemberRef;
import org.bridj.demangling.Demangler.NamespaceRef;
import org.bridj.demangling.Demangler.TypeRef;
import org.bridj.demangling.Demangler.SpecialName;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GCC4Demangler extends Demangler {
	
	public GCC4Demangler(NativeLibrary library, String symbol) {
		super(library, symbol);
	}

        
    private Map<String, List<IdentLike>> prefixShortcuts = new HashMap<String, List<IdentLike>>() {{
        // prefix shortcut: e.g. St is for std::
        put("t", Arrays.asList((IdentLike) new Ident("std")));
        put("a", Arrays.asList((IdentLike) new Ident("std"), new Ident("allocator")));
        // Ss == std::string == std::basic_string<char, std::char_traits<char>, std::allocator<char> >
        Ident string = new Ident("string", new TemplateArg[]{
            classType(Byte.TYPE),
            enclosed(new ClassRef(new Ident("char_traits", new TemplateArg[]{classType(Byte.TYPE)})), "std"),
            enclosed(new ClassRef(new Ident("allocator", new TemplateArg[]{classType(Byte.TYPE)})), "std")
        });
        put("s", Arrays.asList((IdentLike) new Ident("std"), string));
    }

        private TemplateArg enclosed(ClassRef classRef, String string) {
            classRef.setEnclosingType(new NamespaceRef(new Object[]{string}));
            return classRef;
        }
    };
    private Set<String> shouldContinueAfterPrefix = new HashSet<String>() {{
        add("t");
    }};
    private Map<String, TypeRef> typeShortcuts = new HashMap<String, TypeRef>() {{
    }};
    private <T> T ensureOfType(Object o, Class<T> type) throws DemanglingException {
        if (type.isInstance(o)) {
            return type.cast(o);
        } else {
            throw new DemanglingException("Internal error in demangler: trying to cast to " + type.getCanonicalName() + " the object '" + o.toString() + "'");
        }
    }
    int nextShortcutId = -1;
    private String nextShortcutId() {
        int n = nextShortcutId++;
        return n == -1 ? "_" : Integer.toString(n, 36).toUpperCase() + "_";
    }
    /*
    private String previousShortcutId() {
        nextShortcutId--;
        int n = nextShortcutId;
        return n == -1 ? "_" : Integer.toString(n, 36).toUpperCase() + "_";
    }*/

    /*
     private Map<String, TypeRef> shortcuts = new HashMap<String, TypeRef>() {{
        // type shortcut: e.g. Ss is for string
        put("s", classType(StdString.class));
        // these are the default type shortcuts (some more will be added)
    }};

    private TypeRef parseShortcutType() throws DemanglingException {
        char c = peekChar();
        // GCC builds shortcuts for each encountered type, they appear in the mangling as: S_, S0_, S1_, ..., SA_, SB_, ..., SZ_, S10_
        if (c == '_') { // we encounter S_
            return ensureOfType(shortcuts.get(Character.toString(consumeChar())), TypeRef.class);
        }
        if (Character.isDigit(c) || Character.isUpperCase(c)) { // memory shorcut S[0-9A-Z]+_
            String id = "";
            while ((c = peekChar()) != '_' && c != 0) {
                id += consumeChar();
            }
            if (peekChar() == 0) {
                throw new DemanglingException("Encountered a unexpected end in gcc mangler shortcut '" + id + "'.");
            }
            id += consumeChar(); // the '_'
            Object res = shortcuts.get(id);
            if (res == null) {
                throw new DemanglingException("Encountered a unexpected gcc mangler shortcut '" + id + "'.");
            }
            return ensureOfType(res, TypeRef.class);
        } else if (Character.isLowerCase(c)) { // other, single character built-in shorcuts. We suppose for now that all shortcuts are lower case (e.g. Ss, St, ...)
            String id = Character.toString(consumeChar());
            Object res = shortcuts.get(id); // first try the built-in type shortcuts
            if (res != null) {
                return ensureOfType(res, TypeRef.class);
            }
            throw new DemanglingException("Encountered a unexpected gcc mangler built-in shortcut '" + id + "'.");
        } else {
            throw new DemanglingException("Encountered a unexpected gcc unknown shortcut '" + c + "'.");
        }
    }*/
    private TypeRef parsePointerType() throws DemanglingException {
        TypeRef pointed = parseType();
        TypeRef res = pointerType(pointed);
        String id = nextShortcutId();
        typeShortcuts.put(id, res);
        return res;
    }

    public TemplateArg parseTemplateArg() throws DemanglingException {
    		if (consumeCharIf('L')) {
    			TypeRef tr = parseType();
    			StringBuffer b = new StringBuffer();
    			char c;
                while (Character.isDigit(c = peekChar())) {
                    consumeChar();
                    b.append(c);
                }
                expectChars('E');
			// TODO switch on type !
			return new Constant(Integer.parseInt(b.toString()));
    		} else
    			return parseType();
    }
	public TypeRef parseType() throws DemanglingException {
		if (Character.isDigit(peekChar())) {
                    Ident name = ensureOfType(parseNonCompoundIdent(), Ident.class);
                    String id = nextShortcutId(); // we get the id before parsing the part (might be template parameters and we need to get the ids in the right order)
                    TypeRef res = simpleType(name);
                    typeShortcuts.put(id, res);
                    return res;
                }

        char c = consumeChar();
		switch (c) {
                    case 'S':
                    { // here we first check if we have a type shorcut saved, if not we fallback to the (compound) identifier case
                        char cc = peekChar();
                        int delta = 0;
                        if (Character.isDigit(cc) || Character.isUpperCase(cc) || cc == '_') {
                            String id = "";
                            while ((c = peekChar()) != '_' && c != 0) {
                                id += consumeChar();
                                delta++;
                            }
                            if (peekChar() == 0) {
                                throw new DemanglingException("Encountered a unexpected end in gcc mangler shortcut '" + id + "' " + prefixShortcuts.keySet());
                            }
                            id += consumeChar(); // the '_'
                            delta++;
                            System.err.println(typeShortcuts.entrySet() + " , "+prefixShortcuts.entrySet());
                            if (typeShortcuts.containsKey(id)) {
                                return typeShortcuts.get(id);
                            }
                            position -= delta;
                        }
                        // WARNING/INFO/NB: we intentionally continue to the N case
                    }
                    case 'N':
                        position--; // I actually would peek()
                    {
                        List<IdentLike> ns = parseSimpleOrComplexIdent();
                        ClassRef res = new ClassRef(ensureOfType(ns.remove(ns.size() - 1), Ident.class));
                        if (!ns.isEmpty()) {
                            res.setEnclosingType(new NamespaceRef(ns.toArray(new Ident[ns.size()])));
                        }
                        return res;
                    }
		case 'P':
			return parsePointerType();
		case 'F':
			// TODO parse function type correctly !!!
			while (consumeChar() != 'E') {}
			
			return null;
		case 'K':
			return parseType();
		case 'v': // char
			return classType(Void.TYPE);
		case 'c':
		case 'a':
		case 'h': // unsigned
			return classType(Byte.TYPE);
		case 'b': // bool
			return classType(Boolean.TYPE);
		case 'l':
		case 'm': // unsigned
			return classType(CLong.class);
			//return classType(Platform.is64Bits() ? Long.TYPE : Integer.TYPE);
		case 'x':
		case 'y': // unsigned
			return classType(Long.TYPE);
		case 'i':
		case 'j': // unsigned
			return classType(Integer.TYPE);
		case 's':
		case 't': // unsigned
			return classType(Short.TYPE);
		case 'f':
			return classType(Float.TYPE);
		case 'd':
			return classType(Double.TYPE);
		case 'z': // varargs
			return classType(Object[].class);
        default:
			throw error("Unexpected type char '" + c + "'", -1);
		}
	}

    public static class StdString implements IdentLike {}

	String parseName() throws DemanglingException { // parses a plain name, e.g. "4plop" (the 4 is the length)
		char c;
		StringBuilder b = new StringBuilder();
		while (Character.isDigit(c = peekChar())) {
			consumeChar();
			b.append(c);
		}
		int len;
		try {
			len = Integer.parseInt(b.toString());
		} catch (NumberFormatException ex) {
			throw error("Expected a number", 0);
		}
		b.setLength(0);
		for (int i = 0; i < len; i++)
			b.append(consumeChar());
		return b.toString();
	}
    private List<IdentLike> parseSimpleOrComplexIdent() throws DemanglingException {
        List<IdentLike> res = new ArrayList<IdentLike>();
        boolean shouldContinue = false;
        boolean expectEInTheEnd = false;
        if (consumeCharIf('N')) { // complex (NB: they don't nest (they actually can within a template parameter but not elsewhere))
            if (consumeCharIf('S')) { // it uses some shortcut prefix or type
                parseShortcutInto(res);
            }
            shouldContinue = true;
            expectEInTheEnd = true;
        } else { // simple
            if (consumeCharIf('S')) { // it uses some shortcut prefix or type
                shouldContinue = parseShortcutInto(res);
            } else {
                res.add(parseNonCompoundIdent());
            }
        }
        if (shouldContinue) {
            do {
                String id = nextShortcutId(); // we get the id before parsing the part (might be template parameters and we need to get the ids in the right order)
                IdentLike part = parseNonCompoundIdent();
                res.add(part);
                prefixShortcuts.put(id, new ArrayList<IdentLike>(res)); // the current compound name is save by gcc as a shortcut (we do the same)
            } while (Character.isDigit(peekChar()));
            //prefixShortcuts.remove(previousShortcutId()); // correct the fact that we parsed one too much
            nextShortcutId--;
        }
        if (consumeCharIf('I')) {
            String id = nextShortcutId(); // we get the id before parsing the part (might be template parameters and we need to get the ids in the right order)
            List<TemplateArg> args = new ArrayList<TemplateArg>();
            while (!consumeCharIf('E')) {
                args.add(parseTemplateArg());
            }
            Ident templatedIdent = new Ident(ensureOfType(res.remove(res.size() - 1), Ident.class).toString(), args.toArray(new TemplateArg[args.size()]));
            res.add(templatedIdent);
            prefixShortcuts.put(id, new ArrayList<IdentLike>(res));
            {
                List<IdentLike> ns = new ArrayList<IdentLike>(res);
                ClassRef clss = new ClassRef(ensureOfType(ns.remove(ns.size() - 1), Ident.class));
                if (!ns.isEmpty()) {
                    clss.setEnclosingType(new NamespaceRef(ns.toArray(new Ident[ns.size()])));
                }
                typeShortcuts.put(id, clss);
            }
        }
        if (expectEInTheEnd) {
            expectAnyChar('E');
        }
        return res;
    }

    /**
     * @return whether we should expect more parsing after this shortcut (e.g. std::vector<...> is actually not NSt6vectorI...EE but St6vectorI...E (without trailing N)
     */
    private boolean parseShortcutInto(List<IdentLike> res) throws DemanglingException{
        System.err.println(str.substring(position));
        char c = peekChar();
        // GCC builds shortcuts for each encountered type, they appear in the mangling as: S_, S0_, S1_, ..., SA_, SB_, ..., SZ_, S10_
        if (c == '_') { // we encounter S_
            List<IdentLike> toAdd = prefixShortcuts.get(Character.toString(consumeChar()));
            if (toAdd == null) {
                throw new DemanglingException("Encountered a yet undefined gcc mangler shortcut S_ (first one), i.e. '_' " + prefixShortcuts.keySet());
            }
            res.addAll(toAdd);
            return false;
        } else if (Character.isDigit(c) || Character.isUpperCase(c)) { // memory shorcut S[0-9A-Z]+_
            String id = "";
            while ((c = peekChar()) != '_' && c != 0) {
                id += consumeChar();
            }
            if (peekChar() == 0) {
                throw new DemanglingException("Encountered a unexpected end in gcc mangler shortcut '" + id + "' " + prefixShortcuts.keySet());
            }
            id += consumeChar(); // the '_'
            List<IdentLike> toAdd = prefixShortcuts.get(id);
            if (toAdd == null) {
                throw new DemanglingException("Encountered a unexpected gcc mangler shortcut '" + id + "' " + prefixShortcuts.keySet());
            }
            res.addAll(toAdd);
            return false;
        } else if (Character.isLowerCase(c)) { // other, single character built-in shorcuts. We suppose for now that all shortcuts are lower case (e.g. Ss, St, ...)
            String id = Character.toString(consumeChar());
            List<IdentLike> toAdd = prefixShortcuts.get(id);
            if (toAdd == null) {
                throw new DemanglingException("Encountered a unexpected gcc mangler built-in shortcut '" + id + "' " + prefixShortcuts.keySet());
            }
            res.addAll(toAdd);
            return shouldContinueAfterPrefix.contains(id);
        } else {
            throw new DemanglingException("Encountered a unexpected gcc unknown shortcut '" + c + "' " + prefixShortcuts.keySet());
        }
    }

    IdentLike parseNonCompoundIdent() throws DemanglingException { // This is a plain name  with possible template parameters (or special like constructor C1, C2, ...)
        if (consumeCharIf('C')) {
            if (consumeCharIf('1')) {
                return SpecialName.Constructor;
            } else if (consumeCharIf('2')) {
                return SpecialName.SpecialConstructor;
            } else {
                throw error("Unknown constructor type 'C" + peekChar() + "'");
            }
        } else if (consumeCharIf('D')) {
            // see http://zedcode.blogspot.com/2007/02/gcc-c-link-problems-on-small-embedded.html
            if (consumeCharIf('0')) {
                return SpecialName.DeletingDestructor;
            } else if (consumeCharIf('1')) {
                return SpecialName.Destructor;
            } else if (consumeCharIf('2')) {
                return SpecialName.SelfishDestructor;
            } else {
                throw error("Unknown destructor type 'D" + peekChar() + "'");
            }
        } else {
            String n = parseName();
            return new Ident(n);
            /*
            List<TemplateArg> args = new ArrayList<TemplateArg>();
            if (consumeCharIf('I')) {
                while (!consumeCharIf('E')) {
                    args.add(parseTemplateArg());
                }
            }
            return new Ident(n, args.toArray(new TemplateArg[args.size()]));
             */
        }
    }
	@Override
	public MemberRef parseSymbol() throws DemanglingException {
		MemberRef mr = new MemberRef();
		if (!consumeCharIf('_')) {
			mr.setMemberName(new Ident(str));
			return mr;
		}
		consumeCharIf('_');
		expectChars('Z');
		
		if (consumeCharIf('T')) {
			if (consumeCharIf('V')) {
				mr.setEnclosingType(ensureOfType(parseType(), ClassRef.class));
				mr.setMemberName(SpecialName.VFTable);
				return mr;
			}
			return null; // can be a type info, a virtual table or strange things like that
		}
		/*
			Reverse engineering of C++ operators :
			delete[] = __ZdaPv
			delete  = __ZdlPv
			new[] = __Znam
			new = __Znwm
		*/
		if (consumeCharsIf('d', 'l', 'P', 'v')) {
			mr.setMemberName(SpecialName.Delete);
			return mr;
		}
		if (consumeCharsIf('d', 'a', 'P', 'v')) {
			mr.setMemberName(SpecialName.DeleteArray);
			return mr;
		}
		if (consumeCharsIf('n', 'w', 'm')) {
			mr.setMemberName(SpecialName.New);
			return mr;
		}
		if (consumeCharsIf('n', 'a', 'm')) {
			mr.setMemberName(SpecialName.NewArray);
			return mr;
		}
                
            {
                List<IdentLike> ns = parseSimpleOrComplexIdent();
                mr.setMemberName(ns.remove(ns.size() - 1));
                if (!ns.isEmpty()) {
                    ClassRef parent = new ClassRef(ensureOfType(ns.remove(ns.size() - 1), Ident.class));
                    if (!ns.isEmpty()) {
                        parent.setEnclosingType(new NamespaceRef(ns.toArray(new Ident[ns.size()])));
                    }
                    mr.setEnclosingType(parent);
                }
            }

		//System.out.println("mr = " + mr + ", peekChar = " + peekChar());
					
		//mr.isStatic =
		//boolean isMethod = consumeCharIf('E');
		
		if (consumeCharIf('v')) {
			if (position < length)
				error("Expected end of symbol", 0);
			mr.paramTypes = new TypeRef[0];
		} else {
			List<TypeRef> paramTypes = new ArrayList<TypeRef>();
			while (position < length) {// && !consumeCharIf('E')) {
				paramTypes.add(parseType());
			}
			mr.paramTypes = paramTypes.toArray(new TypeRef[paramTypes.size()]);
		}
		return mr;
	}

	
}