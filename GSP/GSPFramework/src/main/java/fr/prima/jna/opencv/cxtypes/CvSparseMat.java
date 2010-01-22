package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvSparseMat extends com.ochafik.lang.jnaerator.runtime.Structure<CvSparseMat, CvSparseMat.ByValue, CvSparseMat.ByReference> {
	public int type;
	public int dims;
	/// C type : int*
	public com.sun.jna.ptr.IntByReference refcount;
	public int hdr_refcount;
	/// C type : CvSet*
	public fr.prima.jna.opencv.cxtypes.CvSet.ByReference heap;
	/// C type : void**
	public com.sun.jna.ptr.PointerByReference hashtable;
	public int hashsize;
	public int valoffset;
	public int idxoffset;
	/// C type : int[32]
	public int[] size = new int[(32)];
	public CvSparseMat() {
		super();
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvSparseMat newInstance() { return new CvSparseMat(); }
	public static CvSparseMat[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvSparseMat.class, arrayLength);
	}
	public static class ByReference extends CvSparseMat implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvSparseMat implements com.sun.jna.Structure.ByValue {}
}
