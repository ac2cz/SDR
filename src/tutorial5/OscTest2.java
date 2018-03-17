package tutorial5;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import tutorial5.plot.FFTPanel;
import tutorial5.signal.Complex;
import tutorial5.signal.ComplexOscillator;
import tutorial5.signal.Window;

public class OscTest2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException, InterruptedException {
		
		int len = 1024;
		double amp = 0.01;
		int sampleRate = 192000;
		double[] bmw = Window.initBlackmanWindow(len);
		double[] buffer = new double[2*len];
		boolean window = true;
		
		ComplexOscillator c_osc = new ComplexOscillator(sampleRate, 5000);// 4800 6000 
		
		JFrame frame = new JFrame();
		frame.setTitle("Complex Oscillator Test");
		frame.setBounds(100,100,700,400);
		FFTPanel plot = new FFTPanel(sampleRate, 0, len, true);
		frame.add(plot);
		
		for (int n=0; n< len; n++) {
			Complex c = c_osc.nextSample();
			buffer[2*n] = c.geti() * amp ; 
			buffer[2*n+1] = c.getq() * amp;

			if (window) {
				buffer[2*n] = buffer[2*n] * bmw[n] + dither(); 
				buffer[2*n+1] = buffer[2*n+1] * bmw[n] + dither();
			}
		}

		if (buffer != null) {
			plot.setData(buffer);
			frame.setVisible(true); // causes window to be redrawn
		}
	}
	
	private static double dither() {
		return Math.random()/100000;
	}
}
