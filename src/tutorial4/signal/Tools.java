package tutorial4.signal;

public class Tools {
	public static int littleEndian2(byte b[], int bitsPerSample) {
		byte b1 = b[0];
		byte b2 = b[1];
		int value =  ((b2 & 0xff) << 8)
		     | ((b1 & 0xff) << 0);
		if (value > (Math.pow(2,bitsPerSample-1)-1)) 
			value = (int) (-1*Math.pow(2,bitsPerSample) + value);
		return value;
	}

	public static int bigEndian2(byte b[], int bitsPerSample) {
		byte b1 = b[1];
		byte b2 = b[0];
		int value =  ((b2 & 0xff) << 8)
		     | ((b1 & 0xff) << 0);
		if (value > (2^(bitsPerSample-1)-1)) 
			value = -(2^bitsPerSample) + value;
		return value;
	}
	
	public static double average (double avg, double new_sample, int N) {
		avg -= avg / N;
		avg += new_sample / N;
		return avg;
	}

	public static double psd(double re, double im, double binBandwidth) {
		return (20*Math.log10(Math.sqrt((re*re) + (im*im))/binBandwidth));
	}
	
	public static double[] initBlackmanWindow(int len) {
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
