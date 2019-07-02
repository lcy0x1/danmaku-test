package main;

public class Temp {

	public static void main(String[] args) {
		double a0 = 1.2e13, a = a0;
		double c = 1, r = 0.50;
		int i = 0;
		System.out.println(Math.pow(a0, 1.0 / 3));
		while (a > a0 * r) {
			a -= c * Math.pow(a, 1.0 / 3);
			i++;
		}
		System.out.println(i);
	}

}
