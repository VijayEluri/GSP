package fr.prima.jna.vlfeat;
/**
 * <i>native declaration : /local_home/softs/sources/vlfeat/vl/hikmeans.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a>, <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class VlHIKMNode extends com.ochafik.lang.jnaerator.runtime.Structure<VlHIKMNode, VlHIKMNode.ByValue, VlHIKMNode.ByReference> {
	/**
	 * < IKM filter for this node<br>
	 * C type : VlIKMFilt*
	 */
	public fr.prima.jna.vlfeat.VlIKMFilt.ByReference filter;
	/**
	 * < Node children (if any)<br>
	 * C type : _VlHIKMNode**
	 */
	public fr.prima.jna.vlfeat.VlHIKMNode.ByReference[] children;
	public VlHIKMNode() {
		super();
	}
	/**
	 * @param filter < IKM filter for this node<br>
	 * C type : VlIKMFilt*<br>
	 * @param children < Node children (if any)<br>
	 * C type : _VlHIKMNode**
	 */
	public VlHIKMNode(fr.prima.jna.vlfeat.VlIKMFilt.ByReference filter, fr.prima.jna.vlfeat.VlHIKMNode.ByReference children[]) {
		super();
		this.filter = filter;
		if (children.length != this.children.length) 
			throw new java.lang.IllegalArgumentException("Wrong array size !");
		this.children = children;
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected VlHIKMNode newInstance() { return new VlHIKMNode(); }
	public static VlHIKMNode[] newArray(int arrayLength) {
		return com.ochafik.lang.jnaerator.runtime.Structure.newArray(VlHIKMNode.class, arrayLength);
	}
	public static class ByReference extends VlHIKMNode implements com.sun.jna.Structure.ByReference {}
	public static class ByValue extends VlHIKMNode implements com.sun.jna.Structure.ByValue {}
}
