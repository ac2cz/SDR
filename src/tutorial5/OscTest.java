package tutorial5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import tutorial5.plot.LineChart;
import tutorial5.signal.Complex;
import tutorial5.signal.ComplexOscillator;

public class OscTest {

	public static void main(String[] args) {
		
		int len = 4000;
		double amp = 1;
		int sampleRate = 192000;
		double[] bufferI = new double[len];
		double[] bufferQ = new double[len];
		
		ComplexOscillator c_osc = new ComplexOscillator(sampleRate, 4800);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Complex Oscillator Test");
		frame.setBounds(100,100,700,500);
		LineChart plotI = new LineChart("Plot I");
		plotI.setPreferredSize(new Dimension(800, 230));
		LineChart plotQ = new LineChart("Plot Q");
		plotQ.setPreferredSize(new Dimension(800, 230));
		frame.add(plotI);
		frame.add(plotQ, BorderLayout.SOUTH);


		for (int n=0; n< len; n++) {
			Complex c = c_osc.nextSample();
			bufferI[n] = c.geti() * amp;
			bufferQ[n] = c.getq() * amp;
		}

		if (bufferI != null) {
			plotI.setData(bufferI);
			plotQ.setData(bufferQ);
			frame.setVisible(true); // causes window to be redrawn
		}
		System.out.println("I Data:");
		for (double d : bufferI)
			System.out.println(d);
		System.out.println("Q Data:");
		for (double d : bufferQ)
			System.out.println(d);
		
	}
	
	private static double dither() {
		return Math.random()/1000;
	}
}
