package tutorial7.signal;

public abstract class Oscillator {

	protected final int TABLE_SIZE = 9600;
	protected double[] sinTable = new double[TABLE_SIZE];
	private int samplesPerSecond = 0;
	private double frequency = 0;
	private double phase = 0;
	private double phaseIncrement = 0;
	
	public Oscillator(int samples, int freq) {
		this.samplesPerSecond = samples;
		this.frequency = freq;
		this.phaseIncrement = 2 * Math.PI * frequency / (double)samplesPerSecond;
	}

	public void setFrequency(double freq) {
		this.frequency = freq;
		this.phaseIncrement = 2 * Math.PI * frequency / (double)samplesPerSecond;		
	}
	
	public double nextSample() {
		phase = phase + phaseIncrement;
		if (frequency > 0 && phase >= 2 * Math.PI)
			phase = phase - 2*Math.PI;
		if (frequency < 0 && phase <= 0)
			phase = phase + 2*Math.PI;
		int idx = (int)((phase * (double)TABLE_SIZE/(2 * Math.PI))%TABLE_SIZE);
		double value = sinTable[idx];
		return value;
	}
	
}
