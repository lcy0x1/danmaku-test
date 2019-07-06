package jogl.util;

import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_ONE;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_DST_COLOR;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_COLOR;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_ZERO;

import java.awt.Color;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;

import battle.Sprite;
import util.Data;
import util.P;

public class GLGraphics implements GeoAuto {

	static class GeomG {

		private static final int CIR = 12;

		private final GLGraphics gra;

		private final GL2 g;

		private Integer color = null;

		private GeomG(GLGraphics glg, GL2 gl2) {
			gra = glg;
			g = gl2;
		}

		public void drawCurve(Curve c) {
			checkMode();
			setColor();

			Curve cp = c.copy();
			cp.times(gra.sh);
			P[] ps = cp.ps;
			double end = cp.end, ra = cp.r;
			int n = ps.length;

			double l = 0;
			for (int i = 1; i < ps.length; i++)
				l += ps[i - 1].dis(ps[i]);
			double lp = (1 - end * 2) / l;

			double d0 = ps[1].atan2(ps[0]);
			double d1 = ps[n - 2].atan2(ps[n - 1]);
			P[] p0 = new P[2], p1 = new P[2];
			P pc = P.polar(end / lp, d0).plus(ps[0]);
			P ec = P.polar(end / lp, d1).plus(ps[n - 1]);
			p0[0] = P.polar(ra, d0 + Math.PI / 2).plus(pc);
			p0[1] = P.polar(ra, d0 - Math.PI / 2).plus(pc);
			p1[0] = P.polar(ra, d1 - Math.PI / 2).plus(ec);
			p1[1] = P.polar(ra, d1 + Math.PI / 2).plus(ec);

			P[][] s0 = new P[2][ps.length - 1];
			for (int i = 0; i < n - 1; i++) {
				d0 = ps[i + 1].atan2(ps[i]);
				P pm = ps[i + 1].middle(ps[i], 0.5);
				s0[0][i] = P.polar(ra, d0 + Math.PI / 2).plus(pm);
				s0[1][i] = P.polar(ra, d0 - Math.PI / 2).plus(pm);
			}

			for (int k = 0; k < 1; k++) {
				g.glBegin(GL.GL_TRIANGLE_STRIP);
				addP(pc);
				addP(p0[k]);
				for (int i = 0; i < n - 1; i++) {
					addP(ps[i]);
					addP(s0[k][i]);
				}
				addP(ps[n - 1]);
				addP(p1[k]);
				addP(ec);
				g.glEnd();
			}

		}

		protected void colRect(int x, int y, int w, int h, int r, int gr, int b, int... a) {
			checkMode();
			if (a.length == 0)
				setColor(r, gr, b);
			else
				g.glColor4f(r / 256f, gr / 256f, b / 256f, a[0] / 256f);
			color = null;
			g.glBegin(GL2ES3.GL_QUADS);
			addP(x, y);
			addP(x + w, y);
			addP(x + w, y + h);
			addP(x, y + h);
			g.glEnd();
		}

		protected void drawCircles(Coord[] cs) {
			checkMode();
			setColor();
			g.glBegin(GL.GL_TRIANGLES);
			GLT glt = gra.getTransform();
			for (int i = 0; i < cs.length; i++) {
				Coord c = cs[i].copy();
				c.times(gra.sh);
				gra.translate(c.x, c.y);
				for (int j = 0; j < CIR; j++) {
					double t0 = j * Math.PI * 2 / CIR;
					double t1 = t0 + Math.PI * 2 / CIR;
					addP(0, 0);
					addP(c.w / 2 * Math.cos(t0), c.h / 2 * Math.sin(t0));
					addP(c.w / 2 * Math.cos(t1), c.h / 2 * Math.sin(t1));
				}
				gra.setTransform(glt);
			}
			g.glEnd();
		}

		protected void drawLine(int i, int j, int x, int y) {
			checkMode();
			setColor();
			g.glBegin(GL.GL_LINES);
			addP(i, j);
			addP(x, y);
			g.glEnd();

		}

		protected void drawOval(int i, int j, int k, int l) {
			// checkMode();
			// setColor();
			// TODO circular

		}

		protected void drawRect(int x, int y, int w, int h) {
			checkMode();
			setColor();
			g.glBegin(GL.GL_LINE_LOOP);
			addP(x, y);
			addP(x + w, y);
			addP(x + w, y + h);
			addP(x, y + h);
			g.glEnd();
		}

		protected void fillOval(int i, int j, int k, int l) {
			// checkMode();
			// setColor();
			// TODO circular

		}

		protected void fillRect(int x, int y, int w, int h) {
			checkMode();
			setColor();
			g.glBegin(GL2ES3.GL_QUADS);
			addP(x, y);
			addP(x + w, y);
			addP(x + w, y + h);
			addP(x, y + h);
			g.glEnd();
		}

		protected void gradRect(int x, int y, int w, int h, int a, int b, int[] c, int d, int e, int[] f) {
			checkMode();
			P vec = new P(d - a, e - b);
			double l = vec.abs();
			l *= l;
			g.glBegin(GL2ES3.GL_QUADS);

			for (int i = 0; i < 4; i++) {
				int px = x, py = y;
				if (i == 2 || i == 3)
					px += w;
				if (i == 1 || i == 2)
					py += h;
				float cx = (float) (vec.dotP(new P(px - a, py - b)) / l);
				cx = P.reg(cx);
				float[] cs = new float[3];
				for (int j = 0; j < 3; j++)
					cs[j] = c[j] + cx * (f[j] - c[j]);
				setColor(cs[0], cs[1], cs[2]);
				addP(px, py);
			}
			g.glEnd();
			color = null;
		}

		protected void setColor(int c) {
			if (c == RED)
				color = Color.RED.getRGB();
			if (c == YELLOW)
				color = Color.YELLOW.getRGB();
			if (c == BLACK)
				color = Color.BLACK.getRGB();
			if (c == MAGENTA)
				color = Color.MAGENTA.getRGB();
			if (c == BLUE)
				color = Color.BLUE.getRGB();
			if (c == CYAN)
				color = Color.CYAN.getRGB();
			if (c == WHITE)
				color = Color.WHITE.getRGB();
		}

		private void addP(double x, double y) {
			gra.addP(x, y);
		}

		private void addP(P p) {
			gra.addP(p.x, p.y);
		}

		private void checkMode() {
			gra.checkMode(PURE);
		}

		private void setColor() {
			if (color == null)
				return;
			setColor(color >> 16 & 255, color >> 8 & 255, color & 255);
		}

		private void setColor(float c0, float c1, float c2) {
			g.glColor3f(c0 / 256f, c1 / 256f, c2 / 256f);
		}

	}

	private static class GLC {

		int mode;

		int[] para;

		boolean done;

		public GLC(int mod, int... par) {
			mode = mod;
			para = par;
		}
	}

	private static final int PURE = 0, IMG = 1;

	protected static int count = 0;
	private final GL2 g;

	private final ResManager tm;
	private final GeomG geo;
	private final int sw, sh;

	private float[] trans = new float[] { 1, 0, 0, 0, 1, 0 };

	private int mode = PURE;
	private int bind = 0;
	private GLC comp = new GLC(DEF);

	public GLGraphics(GL2 gl2, int wid, int hei) {
		g = gl2;
		geo = new GeomG(this, gl2);
		tm = ResManager.get(g);
		sw = wid;
		sh = hei;
		count++;
		if (Data.CLEARBG)
			g.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		g.glLoadIdentity();
		geo.setColor(RED);
	}

	public void dispose() {
		checkMode(PURE);
		count--;
	}

	@Override
	public void drawCurve(Sprite s, Curve c) {
		checkMode(IMG);
		if (s.img == null)
			return;
		compImpl();
		bind(tm.load(this, s.img));
		float[] r = s.img.getRect();

		Curve cp = c.copy();
		cp.times(sh);
		cp.size(1 / s.rad);
		P[] ps = cp.ps;
		double end = cp.end, ra = cp.r;
		int n = ps.length;

		double l = 0;
		for (int i = 1; i < ps.length; i++)
			l += ps[i - 1].dis(ps[i]);
		double lp = (1 - end * 2) / l;

		double d0 = ps[1].atan2(ps[0]);
		double d1 = ps[n - 2].atan2(ps[n - 1]);
		P[] p0 = new P[2], p1 = new P[2];
		P pc = P.polar(end / lp, d0).plus(ps[0]);
		P ec = P.polar(end / lp, d1).plus(ps[n - 1]);
		p0[0] = P.polar(ra, d0 + Math.PI / 2).plus(pc);
		p0[1] = P.polar(ra, d0 - Math.PI / 2).plus(pc);
		p1[0] = P.polar(ra, d1 - Math.PI / 2).plus(ec);
		p1[1] = P.polar(ra, d1 + Math.PI / 2).plus(ec);

		double cl0 = end, cl1 = end;
		P[][] s0 = new P[2][ps.length - 1];
		for (int i = 0; i < n - 1; i++) {
			cl1 += lp * ps[i + 1].dis(ps[i]);
			d0 = ps[i + 1].atan2(ps[i]);
			P pm = ps[i + 1].middle(ps[i], 0.5);
			s0[0][i] = P.polar(ra, d0 + Math.PI / 2).plus(pm);
			s0[1][i] = P.polar(ra, d0 - Math.PI / 2).plus(pm);
			cl0 = cl1;
		}

		for (int k = 0; k < 1; k++) {
			g.glBegin(GL.GL_TRIANGLE_STRIP);
			cl0 = cl1 = end;
			texc(r, 0, 0.5, pc);
			texc(r, 0, k, p0[k]);
			for (int i = 0; i < n - 1; i++) {
				cl1 += lp * ps[i + 1].dis(ps[i]);
				double clm = (cl0 + cl1) / 2;
				texc(r, cl0, 0.5, ps[i]);
				texc(r, clm, k, s0[k][i]);
				cl0 = cl1;
			}
			texc(r, cl0, 0.5, ps[n - 1]);
			texc(r, 1, k, p1[k]);
			texc(r, 1, 0.5, ec);
			g.glEnd();
		}

	}

	@Override
	public void drawImage(GLImage bimg, double x, double y) {
		drawImage(bimg, x, y, bimg.getWidth(), bimg.getHeight());
	}

	@Override
	public void drawImage(GLImage gl, double x, double y, double w, double h) {
		checkMode(IMG);
		if (gl == null)
			return;
		compImpl();
		bind(tm.load(this, gl));
		g.glBegin(GL2ES3.GL_QUADS);
		float[] r = gl.getRect();
		g.glTexCoord2f(r[0], r[1]);
		addP(x, y);
		g.glTexCoord2f(r[0] + r[2], r[1]);
		addP(x + w, y);
		g.glTexCoord2f(r[0] + r[2], r[1] + r[3]);
		addP(x + w, y + h);
		g.glTexCoord2f(r[0], r[1] + r[3]);
		addP(x, y + h);
		g.glEnd();
	}

	@Override
	public void drawImages(Sprite s, int n, Coord[] cs) {
		checkMode(IMG);
		if (s.img == null)
			return;
		compImpl();
		bind(tm.load(this, s.img));
		g.glBegin(GL2ES3.GL_QUADS);
		float[] r = s.img.getRect();
		GLT glt = getTransform();
		for (int i = 0; i < n; i++) {
			Coord c = cs[i].copy();
			c.times(sh);
			c.size(1 / s.rad);
			translate(c.x, c.y);
			rotate(c.a);
			translate(-c.w * s.piv.x, -c.h * s.piv.y);
			g.glTexCoord2f(r[0], r[1]);
			addP(0, 0);
			g.glTexCoord2f(r[0] + r[2], r[1]);
			addP(c.w, 0);
			g.glTexCoord2f(r[0] + r[2], r[1] + r[3]);
			addP(c.w, c.h);
			g.glTexCoord2f(r[0], r[1] + r[3]);
			addP(0, c.h);
			setTransform(glt);
		}
		g.glEnd();
	}

	@Override
	public GeomG getGeo() {
		return geo;
	}

	@Override
	public GLT getTransform() {
		GLT glt = new GLT();
		glt.data = trans.clone();
		return glt;
	}

	@Override
	public void rotate(double d) {
		float c = (float) Math.cos(d);
		float s = (float) Math.sin(d);
		float f0 = trans[0] * c + trans[1] * s;
		float f1 = trans[0] * -s + trans[1] * c;
		float f3 = trans[3] * c + trans[4] * s;
		float f4 = trans[3] * -s + trans[4] * c;
		trans[0] = f0;
		trans[1] = f1;
		trans[3] = f3;
		trans[4] = f4;
	}

	@Override
	public void scale(int hf, int vf) {
		trans[0] *= hf;
		trans[3] *= hf;
		trans[1] *= vf;
		trans[4] *= vf;
	}

	@Override
	public void setComposite(int mode, int... para) {
		if (mode == GRAY) { // 1-d
			checkMode(PURE);
			g.glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);
			setColor(WHITE);
		} else
			comp = new GLC(mode, para);
	}

	@Override
	public void setRenderingHint(int key, int object) {
	}

	@Override
	public void setTransform(GLT at) {
		trans = at.data.clone();
	}

	@Override
	public void translate(double x, double y) {
		trans[2] += trans[0] * x + trans[1] * y;
		trans[5] += trans[3] * x + trans[4] * y;
	}

	protected void bind(int id) {
		if (bind == id)
			return;
		g.glBindTexture(GL_TEXTURE_2D, id);
		bind = id;
	}

	private void addP(double x, double y) {
		double fx = trans[0] * x + trans[1] * y + trans[2];
		double fy = trans[3] * x + trans[4] * y + trans[5];
		g.glVertex2f((float) (2 * fx / sw - 1), (float) (1 - 2 * fy / sh));
	}

	private void checkMode(int i) {
		if (mode == i)
			return;
		int premode = mode;
		mode = i;
		if (premode == IMG) {
			g.glDisable(GL_TEXTURE_2D);
			g.glUseProgram(0);
			g.glEnable(GL_BLEND);
			g.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		if (mode == IMG) {
			g.glEnable(GL_TEXTURE_2D);
			g.glEnable(GL_BLEND);
			g.glUseProgram(tm.prog);
		}
	}

	private void compImpl() {
		if (comp.done)
			return;
		int mode = comp.mode;
		int[] para = comp.para;
		comp.done = true;
		if (mode == DEF) {
			// sC *sA + dC *(1-sA)
			g.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			g.glUniform1i(tm.mode, 0);
		}
		if (mode == TRANS) {
			g.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			g.glUniform1i(tm.mode, 1);
			g.glUniform1f(tm.para, para[0] * 1.0f / 256);
		}
		if (mode == BLEND) {
			g.glUniform1f(tm.para, para[0] * 1.0f / 256);
			if (para[1] == 0) {
				g.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				g.glUniform1i(tm.mode, 1);
			} else if (para[1] == 1) {// d+s*a
				g.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
				g.glUniform1i(tm.mode, 1);// sA=sA*p
			} else if (para[1] == 2) {// d*(1-a+s*a)
				g.glBlendFunc(GL_ZERO, GL_SRC_COLOR);
				g.glUniform1i(tm.mode, 2);// sA=sA*p, sC=1-sA+sC*sA
			} else if (para[1] == 3) {// d+(1-d)*s*a
				g.glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE);
				g.glUniform1i(tm.mode, 1);// sA=sA*p
			} else if (para[1] == -1) {// d-s*a
				g.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
				g.glUniform1i(tm.mode, 3);// sA=-sA*p
			}
		}
	}

	private void texc(float[] r, double tx, double ty, P p) {
		g.glTexCoord2f((float) (r[0] + tx * r[2]), (float) (r[1] + ty * r[3]));
		addP(p.x, p.y);
	}

}
