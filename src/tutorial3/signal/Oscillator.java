package tutorial3.signal;

public class Oscillator {

	private final int TABLE_SIZE = 128;
	private double[] sinTable = new double[TABLE_SIZE];
	private int samplesPerSecond = 48000;
	private int frequency = 0;
	private double phase = 0;
	private double phaseIncrement = 0;
	
	public Oscillator(int samples, int freq) {
		this.samplesPerSecond = samples;
		this.frequency = freq;
		phaseIncrement = 2 * Math.PI * frequency / samplesPerSecond;
		for (int n=0; n<TABLE_SIZE; n++) {
			sinTable[n] = Math.sin(n*2.0*Math.PI/TABLE_SIZE);
		}
	}
	
	public double nextSample() {
		phase = phase + phaseIncrement;
		if (phase >= 2 * Math.PI)
			phase = 0;
		int idx = (int)((phase * TABLE_SIZE/(2 * Math.PI))%TABLE_SIZE);
		double value = sinTable[idx];
		return value;
	}
	
}
