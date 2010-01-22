package fr.prima.jna.opencv.cxtypes;
/**
 * <i>native declaration : cxtypes.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvSeqBlock extends com.ochafik.lang.jnaerator.runtime.Structure<CvSeqBlock, CvSeqBlock.ByValue, CvSeqBlock.ByReference> {
	/**
	 * previous sequence block<br>
	 * C type : CvSeqBlock*
	 */
	public fr.prima.jna.opencv.cxtypes.CvSeqBlock.ByReference prev;
	/**
	 * next sequence block<br>
	 * C type : CvSeqBlock*
	 */
	public fr.prima.jna.opencv.cxtypes.CvSeqBlock.ByReference next;
	/**
	 * index of the first element in the block +<br>
	 * sequence->first->start_index
	 */
	public int start_index;
	/// number of elements in the block
	public int count;
	/**
	 * pointer to the first element of the block<br>
	 * C type : char*
	 */
	public com.sun.jna.Pointer data;
	public CvSeqBlock() {
		super();
	}
	/**
	 * @param prev previous sequence block<br>
	 * C type : CvSeqBlock*<br>
	 * @param next next sequence block<br>
	 * C type : CvSeqBlock*<br>
	 * @param start_index index of the first element in the block +<br>
	 * sequence->first->start_index<br>
	 * @param count number of elements in the block<br>
	 * @param data pointer to the first element of the block<br>
	 * C type : char*
	 */
	public CvSeqBlock(fr.prima.jna.opencv.cxtypes.CvSeqBlock.ByReference prev, fr.prima.jna.opencv.cxtypes.CvSeqBlock.ByReference next, int start_index, int count, com.sun.jna.Pointer data) {
		super();
		this.prev = prev;
		this.next = next;
		this.start_index = start_index;
		this.count = count;
		this.data = data;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected CvSeqBlock newInstance() { return new CvSeqBlock(); }
	public static CvSeqBlock[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(CvSeqBlock.class, arrayLength);
	}
	public static class ByReference extends CvSeqBlock implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends CvSeqBlock implements com.sun.jna.Structure.ByValue {}
}
