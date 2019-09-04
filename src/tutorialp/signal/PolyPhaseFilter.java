package tutorialp.signal;

public class PolyPhaseFilter {
	double coeffs[];
	double alpha = 0.5;
	FirFilter[] subFilters;
	
	/**
	 * Taps LEN must be an integer number of decimationRate
	 * @param sampleRate
	 * @param freq
	 * @param decimationRate
	 * @param len
	 */
	public PolyPhaseFilter(double sampleRate, double freq, int decimationRate, int len) {
		coeffs = Filter.makeRaiseCosine(sampleRate, freq, alpha, len);
		System.out.println("Raised Cosine PolyPhase Filter");
		initSubFilters(coeffs, decimationRate);
	}
	
	private void initSubFilters(double[] coeffs, int R) {
		int M = coeffs.length;
		subFilters = new FirFilter[R];
		int P = R - 1; // position of the polyphase switch

		for (int j=0; j < R; j ++) {
			double[] taps = new double[M/R];			
			for (int i = 0; i < M/R; i++) {
				taps[i] = (coeffs[P + i*R]); 
			}
			subFilters[j] = new FirFilter(taps);
			P = P - 1;
			if (P < 0)
				P = R - 1;
		}
	}

	/**
	 * Each time we get an input we rotate though the coefficients based on the decimate rate R and the position P.
	 * We pass in enough coefficients to perform a complete rotation, because that produces one output value. It
	 * is up to the calling routine to store the values and pass them in as a set.
	 * @param in
	 * @return
	 */
	public double filter(double[] in) {
		double sum; 
		int j = in.length-1;
		sum = 0.0;
		for (int i = 0; i < in.length; i++) 
			sum += subFilters[i].filter(in[j--]);
	//		sum += subFilters[i].filter(in[i]);
////		System.out.println("Sum: " + sum);
		return sum;
	}
	
	public static void main(String[] args) {
		PolyPhaseFilter f = new PolyPhaseFilter(48000, 12000, 4, 12);
		double[] in = {1,2,3,4};
		for (int j=0; j<12; j++) {
			f.filter(in);
		}
	}

}
