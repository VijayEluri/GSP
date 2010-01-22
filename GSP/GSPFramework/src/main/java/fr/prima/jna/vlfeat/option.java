package fr.prima.jna.vlfeat;
/**
 * @brief ::getopt_long option<br>
 * <i>native declaration : /local_home/softs/sources/vlfeat/vl/getopt_long.h:27</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class option extends com.ochafik.lang.jnaerator.runtime.Structure<option, option.ByValue, option.ByReference> {
	/**
	 * < option long name<br>
	 * C type : const char*
	 */
	public com.sun.jna.Pointer name;
	/// < no, required or optional argument flag
	public int has_arg;
	/**
	 * < pointer to variable to set (if null, returns)<br>
	 * C type : int*
	 */
	public com.sun.jna.ptr.IntByReference flag;
	/// < value to set or to return
	public int val;
	public option() {
		super();
	}
	/**
	 * @param name < option long name<br>
	 * C type : const char*<br>
	 * @param has_arg < no, required or optional argument flag<br>
	 * @param flag < pointer to variable to set (if null, returns)<br>
	 * C type : int*<br>
	 * @param val < value to set or to return
	 */
	public option(com.sun.jna.Pointer name, int has_arg, com.sun.jna.ptr.IntByReference flag, int val) {
		super();
		this.name = name;
		this.has_arg = has_arg;
		this.flag = flag;
		this.val = val;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected option newInstance() { return new option(); }
	public static option[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(option.class, arrayLength);
	}
	public static class ByReference extends option implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends option implements com.sun.jna.Structure.ByValue {}
}
