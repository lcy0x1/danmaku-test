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
			if (s instanceof Circle)
				return pos.dis(((Circle) s).pos) - r - ((Circle) s).r;
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