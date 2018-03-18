package tutorial4;

import javax.swing.JFrame;
import tutorial3.plot.FFTPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	FFTPanel fftPanel;

	public MainWindow(String title, int sampleRate, int samples) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 400);
		fftPanel = new FFTPanel(sampleRate, 7200000, samples);
		add(fftPanel);
	}

	public void setData(double[] data) {
		fftPanel.setData(data);
	}
}
