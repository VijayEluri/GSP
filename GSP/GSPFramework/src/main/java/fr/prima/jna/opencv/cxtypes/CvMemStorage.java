package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvMemStorage extends com.ochafik.lang.jnaerator.runtime.Structure<CvMemStorage, CvMemStorage.ByValue, CvMemStorage.ByReference> {
	public int signature;
	/**
	 * first allocated block<br>
	 * C type : CvMemBlock*
	 */
	public fr.prima.jna.opencv.cxtypes.CvMemBlock.ByReference bottom;
	/**
	 * current memory block - top of the stack<br>
	 * C type : CvMemBlock*
	 */
	public fr.prima.jna.opencv.cxtypes.CvMemBlock.ByReference top;
	/**
	 * borrows new blocks from<br>
	 * C type : CvMemStorage*
	 */
	public fr.prima.jna.opencv.cxtypes.CvMemStorage.ByReference parent;
	/// block size
	public int block_size;
	/// free space in the current block
	public int free_space;
	public CvMemStorage() {
		super();
	}
	/**
	 * @param bottom first allocated block<br>
	 * C type : CvMemBlock*<br>
	 * @param top current memory block - top of the stack<br>
	 * C type : CvMemBlock*<br>
	 * @param parent borrows new blocks from<br>
	 * C type : CvMemStorage*<br>
	 * @param block_size block size<br>
	 * @param free_space free space in the current block
	 */
	public CvMemStorage(int signature, fr.prima.jna.opencv.cxtypes.CvMemBlock.ByReference bottom, fr.prima.jna.opencv.cxtypes.CvMemBlock.ByReference top, fr.prima.jna.opencv.cxtypes.CvMemStorage.ByReference parent, int block_size, int free_space) {
		super();
		this.signature = signature;
		this.bottom = bottom;
		this.top = top;
		this.parent = parent;
		this.block_size = block_size;
		this.free_space = free_space;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvMemStorage newInstance() { return new CvMemStorage(); }
	public static CvMemStorage[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvMemStorage.class, arrayLength);
	}
	public static class ByReference extends CvMemStorage implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvMemStorage implements com.sun.jna.Structure.ByValue {}
}
