package tutorialx.signal;

public class Filter {
	
	
	public static double[] initTest(double sampleRate, double freq, double alpha, int len) {
		int M = len-1;
		double[] coeffs = new double[M+1];
		for (int i=0; i < len; i++)
			coeffs[i] = i+1;
		return coeffs;
	}
	
	/**
	 * Calculate the values for a Raised Cosine filter
	 * @param sampleRate
	 * @param freq
	 * @param len
	 */
	public static double[] makeRaiseCosine(double sampleRate, double freq, double alpha, int len) {
		int M = len-1;
		double[] coeffs = new double[len];
		double Fc = freq/sampleRate;
		
		double sumofsquares = 0;
		double[] tempCoeffs = new double[len];
		int limit = (int)(0.5 / (alpha * Fc));
		for (int i=0; i <= M; i++) {
			double sinc = (Math.sin(2 * Math.PI * Fc * (i - M/2)))/ (i - M/2);
			double cos = Math.cos(alpha * Math.PI * Fc * (i - M/2)) / ( 1 - (Math.pow((2 * alpha * Fc * (i - M/2)),2)));
			
			if (i == M/2) {
				tempCoeffs[i] = 2 * Math.PI * Fc * cos;
			} else {
				tempCoeffs[i] = sinc * cos;
			}
			
			// Care because ( 1 - ( 2 * Math.pow((alpha * Fc * (i - M/2)),2))) is zero for 
			if ((i-M/2) == limit || (i-M/2) == -limit) {
				tempCoeffs[i] = 0.25 * Math.PI * sinc;
			} 
			
			sumofsquares += tempCoeffs[i]*tempCoeffs[i];
		}
		double gain = Math.sqrt(sumofsquares);
		for (int i=0; i < tempCoeffs.length; i++) {
			coeffs[i] = tempCoeffs[tempCoeffs.length-i-1]/gain;
			//System.out.println(coeffs[i]);
		}
		return coeffs;
	}
}
