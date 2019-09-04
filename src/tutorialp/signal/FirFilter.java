package tutorialp.signal;

public class FirFilter {
	
	double coeffs[];
	double[] xv;  // This array holds the delayed values
	int M; // The number of taps, the length of the filter
	double Fc = 0d; // Will be set to cutoffFreq/SAMPLE_RATE; 
	
	public FirFilter (double[] taps) {
		coeffs = taps;
		M = coeffs.length-1;
		xv = new double[M+1];
	}
	
	/**
	 * Calculate the result with convolution.  This assumes that the filter kernel coeffs are already reversed as
	 * new valuea are added to the end of the delay line xv.  Note that a symmetrical filter such as a raised 
	 * cosine does not need to be reversed.
	 * @param in
	 * @return
	 */
	public double filter(double in) {
		double sum; 
		int i;
		for (i = 0; i < M; i++) 
			xv[i] = xv[i+1];
		xv[M] = in;
		sum = 0.0;
		for (i = 0; i <= M; i++) {
			sum += (coeffs[i] * xv[i]);
//////////			System.out.println(coeffs[i] + " * " + xv[i]);
		}
//////////		System.out.println("Sum: " + sum);
		return sum;
	}
}
