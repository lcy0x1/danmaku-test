package battle;

import main.Printer;
import util.P;

public interface Shape {

	public static class Circle extends PosShape {

		public double r;

		public Circle(P p, double radius) {
			super(p);
			r = radius;
		}

		@Override
		public double dis(Shape s) {
			if (s instanceof Circle) {
				Circle c = (Circle) s;
				return pos.dis(c.pos) - r - c.r;
			}
			return s.dis(this);
		}

	}

	public static abstract class LineSegs implements Shape {

		public double r;

		public LineSegs(double ra) {
			r = ra;
		}

		@Override
		public double dis(Shape s) {
			if (s instanceof Circle) {
				Circle c = (Circle) s;
				double dis = Double.MAX_VALUE;
				for (P[] ps : getPos()) {
					if (ps.length == 1)
						continue;
					P.Line l = new P.Line(ps);
					dis = Math.min(dis, l.dis(c.pos));
				}
				return dis - r - c.r;
			}
			return s.dis(this);
		}

		public abstract P[][] getPos();

	}

	public static class NonShape extends PosShape {

		public NonShape(P p) {
			super(p);
		}

		@Override
		public double dis(Shape s) {
			if (s instanceof Circle) {
				Circle c = (Circle) s;
				return c.pos.dis(pos) - c.r;
			}
			return s.dis(this);
		}

	}

	public static class Polygon extends PosShape {

		public final P.Polygon poly;

		public double a;

		public Polygon(P p, double angle, P.Polygon pp) {
			super(p);
			a = angle;
			poly = pp;
		}

		@Override
		public double dis(Shape s) {
			if (s instanceof Circle)
				return poly.dis(pos.sf(((Circle) s).pos).rotate(-a)) - ((Circle) s).r;
			Printer.e("Shape", 42, "cannot handle shape collision");
			return Double.NaN;
		}

	}

	public abstract class PosShape implements Shape {

		public final P pos;

		public PosShape(P p) {
			pos = p;
		}

	}

	public double dis(Shape s);

}