package fr.prima.jna.vlfeat;
/**
 * <i>native declaration : /local_home/softs/sources/vlfeat/vl/sift.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class VlSiftFilt extends com.ochafik.lang.jnaerator.runtime.Structure<VlSiftFilt, VlSiftFilt.ByValue, VlSiftFilt.ByReference> {
	/// < nominal image smoothing.
	public double sigman;
	/// < smoothing of pyramid base.
	public double sigma0;
	/// < k-smoothing
	public double sigmak;
	/// < delta-smoothing.
	public double dsigma0;
	/// < image width.
	public int width;
	/// < image height.
	public int height;
	/// < number of octaves.
	public int O;
	/// < number of levels per octave.
	public int S;
	/// < minimum octave index.
	public int o_min;
	/// < minimum level index.
	public int s_min;
	/// < maximum level index.
	public int s_max;
	/// < current octave.
	public int o_cur;
	/**
	 * < temporary pixel buffer.<br>
	 * C type : vl_sift_pix*
	 */
	public com.sun.jna.ptr.FloatByReference temp;
	/**
	 * < current GSS data.<br>
	 * C type : vl_sift_pix*
	 */
	public com.sun.jna.ptr.FloatByReference octave;
	/**
	 * < current DoG data.<br>
	 * C type : vl_sift_pix*
	 */
	public com.sun.jna.ptr.FloatByReference dog;
	/// < current octave width.
	public int octave_width;
	/// < current octave height.
	public int octave_height;
	/**
	 * < detected keypoints.<br>
	 * C type : VlSiftKeypoint*
	 */
	public fr.prima.jna.vlfeat.VlSiftKeypoint.ByReference keys;
	/// < number of detected keypoints.
	public int nkeys;
	/// < size of the keys buffer.
	public int keys_res;
	/// < peak threshold.
	public double peak_thresh;
	/// < edge threshold.
	public double edge_thresh;
	/// < norm threshold.
	public double norm_thresh;
	/// < magnification factor.
	public double magnif;
	/// < size of Gaussian window (in spatial bins)
	public double windowSize;
	/**
	 * < GSS gradient data.<br>
	 * C type : vl_sift_pix*
	 */
	public com.sun.jna.ptr.FloatByReference grad;
	/// < GSS gradient data octave.
	public int grad_o;
	public VlSiftFilt() {
		super();
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected VlSiftFilt newInstance() { return new VlSiftFilt(); }
	public static VlSiftFilt[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(VlSiftFilt.class, arrayLength);
	}
	public static class ByReference extends VlSiftFilt implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends VlSiftFilt implements com.sun.jna.Structure.ByValue {}
}
