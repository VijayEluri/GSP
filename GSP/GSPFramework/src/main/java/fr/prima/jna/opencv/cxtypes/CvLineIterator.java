package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvLineIterator extends com.ochafik.lang.jnaerator.runtime.Structure<CvLineIterator, CvLineIterator.ByValue, CvLineIterator.ByReference> {
	/**
	 * pointer to the current point<br>
	 * C type : uchar*
	 */
	public com.sun.jna.Pointer ptr;
	/// Bresenham algorithm state
	public int err;
	public int plus_delta;
	public int minus_delta;
	public int plus_step;
	public int minus_step;
	public CvLineIterator() {
		super();
	}
	/**
	 * @param ptr pointer to the current point<br>
	 * C type : uchar*<br>
	 * @param err Bresenham algorithm state
	 */
	public CvLineIterator(com.sun.jna.Pointer ptr, int err, int plus_delta, int minus_delta, int plus_step, int minus_step) {
		super();
		this.ptr = ptr;
		this.err = err;
		this.plus_delta = plus_delta;
		this.minus_delta = minus_delta;
		this.plus_step = plus_step;
		this.minus_step = minus_step;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvLineIterator newInstance() { return new CvLineIterator(); }
	public static CvLineIterator[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvLineIterator.class, arrayLength);
	}
	public static class ByReference extends CvLineIterator implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvLineIterator implements com.sun.jna.Structure.ByValue {}
}
