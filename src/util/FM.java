package util;

public class FM {

	public static final double PI = Math.PI;
	public static final double E = Math.E;

	public static int c0 = 0, c1 = 0, c2 = 0;

	public static double abs(double d) {
		return d > 0 ? d : -d;
	}

	public static double atan2(double y, double x) {
		c1++;
		return Math.atan2(y, x);
	}

	public static void clear(boolean print) {
		if (print)
			System.out.println("cos: " + c0 + ", atan: " + c1 + ", sqrt: " + c2);
		c0 = c1 = c2 = 0;
	}

	public static double cos(double t) {
		double x = abs(t / PI * 2) % 4;
		if (x < 1)
			return trig(1 - x);
		if (x < 2)
			return -trig(x - 1);
		if (x < 3)
			return -trig(3 - x);
		return trig(x - 3);

	}

	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	public static double min(double a, double b) {
		return a > b ? b : a;
	}

	public static double pow(double b, double p) {
		return Math.pow(b, p);
	}

	public static double random() {
		return Math.random();
	}

	public static double signum(double a) {
		return Math.signum(a);
	}

	public static double sin(double t) {
		return cos(t - PI / 2);
	}

	public static double sq(double x) {
		return x * x;
	}

	public static double sqrt(double a) {
		c2++;
		return Math.sqrt(a);
	}

	private static double trig(double x) {
		c0++;
		double x2 = x * x;
		return ((((.00015148419 * x2 - .00467376557) * x2 + .07968967928) * x2 - .64596371106) * x2 + 1.57079631847)
				* x;
	}

}
