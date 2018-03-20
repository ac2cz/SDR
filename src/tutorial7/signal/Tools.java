package tutorial7.signal;

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
	
	public static void getDoublesFromBytes(double[]out, byte[] readBuffer, boolean stereo) {
		for (int i = 0; i < out.length; i++) {
			int step = 4;
			if (stereo) step = 2;
			byte[] ab = {readBuffer[step*i],readBuffer[step*i+1]};
			double value =  littleEndian2(ab,16)/32768.0;
			out[i] = value;
		}
	}
	
	static public void getBytesFromDoubles(final double[] audioData, final int storedSamples, boolean stereo, byte[] audioDataBytes) {
		int k = 0;
		if (!stereo) {
			for (int i = 0; i < storedSamples; i++) {
				audioData[i] = Math.min(1.0, Math.max(-1.0, audioData[i])); // saturation
				int sample = (int) Math.round((audioData[i] + 1.0) * 32767.5) - 32768; // scaling and conversion to integer
				byte high = (byte) ((sample >> 8) & 0xFF);
				byte low = (byte) (sample & 0xFF);
				audioDataBytes[k] = low;
				audioDataBytes[k + 1] = high;
				k = k + 2;
			}
		} else {
			// STEREO
			for (int i = 0; i < storedSamples; i++) {
				audioData[i] = Math.min(1.0, Math.max(-1.0, audioData[i])); // saturation
				int sample = (int) Math.round((audioData[i] + 1.0) * 32767.5) - 32768; // scaling and conversion to integer
				byte high = (byte) ((sample >> 8) & 0xFF);
				byte low = (byte) (sample & 0xFF);
				audioDataBytes[k] = low;
				audioDataBytes[k + 1] = high;
				// Put the same in the other channel
				audioDataBytes[k + 2] = low;
				audioDataBytes[k + 3] = high;
				k = k + 4;
			}				
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
