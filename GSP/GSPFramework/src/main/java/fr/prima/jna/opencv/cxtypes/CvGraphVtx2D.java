package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvGraphVtx2D extends com.ochafik.lang.jnaerator.runtime.Structure<CvGraphVtx2D, CvGraphVtx2D.ByValue, CvGraphVtx2D.ByReference> {
	public int flags;
	/// C type : CvGraphEdge*
	public fr.prima.jna.opencv.cxtypes.CvGraphEdge.ByReference first;
	/// C type : CvPoint2D32f*
	public fr.prima.jna.opencv.cxtypes.CvPoint2D32f.ByReference ptr;
	public CvGraphVtx2D() {
		super();
	}
	/**
	 * @param first C type : CvGraphEdge*<br>
	 * @param ptr C type : CvPoint2D32f*
	 */
	public CvGraphVtx2D(int flags, fr.prima.jna.opencv.cxtypes.CvGraphEdge.ByReference first, fr.prima.jna.opencv.cxtypes.CvPoint2D32f.ByReference ptr) {
		super();
		this.flags = flags;
		this.first = first;
		this.ptr = ptr;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvGraphVtx2D newInstance() { return new CvGraphVtx2D(); }
	public static CvGraphVtx2D[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvGraphVtx2D.class, arrayLength);
	}
	public static class ByReference extends CvGraphVtx2D implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvGraphVtx2D implements com.sun.jna.Structure.ByValue {}
}
