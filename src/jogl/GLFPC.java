package jogl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import battle.Engine;
import jogl.util.GLGraphics;

public class GLFPC extends GLCstd {

	private static final long serialVersionUID = 1L;

	public final Engine e;

	public GLFPC(Engine eng) {
		e = eng;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		int w = getWidth();
		int h = getHeight();
		GLGraphics fg = new GLGraphics(drawable.getGL().getGL2(), w, h);
		e.draw(fg);
		fg.dispose();
		gl.glFlush();
	}

	public void paint() {
		display();
	}

}
