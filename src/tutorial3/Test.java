package tutorial3;

import tutorial3.signal.Tools;

public class Test {

	public static void main(String[] args) {
		double a = 6;
		int av = 6;
		a = Tools.average(a, 6, av);
		a = Tools.average(a, 6, av);
		a = Tools.average(a, 6, av);
		a = Tools.average(a, 6, av);
		a = Tools.average(a, 8, av);
		a = Tools.average(a, 8, av);
		a = Tools.average(a, 8, av);
		a = Tools.average(a, 8, av);
		a = Tools.average(a, 8, av);
		System.out.println(a);

	}

}
