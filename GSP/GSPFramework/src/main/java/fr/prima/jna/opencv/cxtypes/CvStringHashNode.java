package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvStringHashNode extends com.ochafik.lang.jnaerator.runtime.Structure<CvStringHashNode, CvStringHashNode.ByValue, CvStringHashNode.ByReference> {
	public int hashval;
	/// C type : CvString
	public fr.prima.jna.opencv.cxtypes.CvString str;
	/// C type : CvStringHashNode*
	public fr.prima.jna.opencv.cxtypes.CvStringHashNode.ByReference next;
	public CvStringHashNode() {
		super();
	}
	/**
	 * @param str C type : CvString<br>
	 * @param next C type : CvStringHashNode*
	 */
	public CvStringHashNode(int hashval, fr.prima.jna.opencv.cxtypes.CvString str, fr.prima.jna.opencv.cxtypes.CvStringHashNode.ByReference next) {
		super();
		this.hashval = hashval;
		this.str = str;
		this.next = next;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvStringHashNode newInstance() { return new CvStringHashNode(); }
	public static CvStringHashNode[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvStringHashNode.class, arrayLength);
	}
	public static class ByReference extends CvStringHashNode implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvStringHashNode implements com.sun.jna.Structure.ByValue {}
}
