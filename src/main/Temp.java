package main;

import java.util.function.Consumer;

public class Temp {

	public static void main(String[] args) {
		System.out.println(Math.atan2(0, 0));
		Temp t1 = new Temp(1), t2 = new Temp(2);

		System.out.println(t1.ci.getClass() == t2.ci.getClass());
		t1.ci.accept(0);
		System.out.println(t1.x);
	}

	private int x;

	private Consumer<Integer> ci;

	private Temp(int a) {
		x = a;
		pc(i -> System.out.println(x++));
	}

	private void pc(Consumer<Integer> c) {
		ci = c;
		System.out.println(c.getClass());
	}

}
