package tutorial3;

import javax.swing.JFrame;

import tutorial3.plot.FFTPanel;
import tutorial3.plot.LineChart;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	FFTPanel fftPanel;

	public MainWindow(String title, int sampleRate) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 400);
		fftPanel = new FFTPanel(sampleRate, 7100000, 4096);
		add(fftPanel);
	}

	public void setData(double[] data) {
		fftPanel.setData(data);
	}
}
