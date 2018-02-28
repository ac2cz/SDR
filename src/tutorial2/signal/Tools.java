package tutorial2.signal;

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
	
	public static void getDoublesFromBytes(double[]out, byte[] readBuffer) {
		for (int i = 0; i < out.length; i++) {// 4 bytes for each sample. 2 in each stereo channel.
			byte[] ab = {readBuffer[4*i],readBuffer[4*i+1]};
			double value =  littleEndian2(ab,16);
			value = value /32768.0;
			out[i] = value;
		}	
	}
	
	public static double average (double avg, double new_sample, int N) {
		avg -= avg / N;
		avg += new_sample / N;
		return avg;
	}

	public static double psd(double re, double im, double binBandwidth) {
		return (20*Math.log10(Math.sqrt((re*re) + (im*im))/binBandwidth));
	}	
}
