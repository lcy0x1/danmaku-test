package main;

import util.FM;

public class Temp {

	public static void main(String[] args) {
		double n0 = 150000, n1 = 7000, n2 = 64000;
		long t0 = System.nanoTime();
		for (int i = 0; i < n0; i++)
			FM.sin(i * 1000 % 1007 * 2.1);
		for (int i = 0; i < n1; i++)
			FM.atan2(i * 1000 % 1007 * 2.1, i * 2000 % 1007 * 2.1);
		for (int i = 0; i < n2; i++)
			FM.sqrt(i * 1000 % 1007 * 2.1);
		long t1 = System.nanoTime();
		System.out.println(((t1 - t0) >> 20) + "ms");
	}

}
