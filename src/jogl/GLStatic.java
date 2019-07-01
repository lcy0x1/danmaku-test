package jogl;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import main.MainTH;

public class GLStatic {

	public static final boolean MIP = false;
	public static final GLProfile GLP;
	public static final GLCapabilities GLC;

	public static boolean ALWAYS_GLIMG = true;
	public static boolean GLTEST = !MainTH.WRITE;
	public static boolean JOGL_SHADER = true;
	public static boolean ALL_BIMG = true;

	static {
		GLP = GLProfile.get(GLProfile.GL2);
		GLC = new GLCapabilities(GLP);

	}

}
