package tutorial8.signal;

public class Window {

	public static double[] initBlackmanWindow(int M) {
		int len = M;
		if (M % 2 == 0) // if length passed is even we set len to M - 1
			len = M-1;
		double[] blackmanWindow = new double[len+1];
		for (int i=0; i <= len; i ++) {
			blackmanWindow[i] =  (0.42 - 0.5 * Math.cos(2 * Math.PI * i / len) 
					+ 0.08 * Math.cos((4 * Math.PI * i) / len));
			if (blackmanWindow[i] < 0)
				blackmanWindow[i] = 0;
		}
		return blackmanWindow;
	}
}
