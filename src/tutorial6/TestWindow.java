package tutorial6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import tutorial5.plot.FFTPanel;

@SuppressWarnings("serial")
public class TestWindow extends JFrame {

	FFTPanel rfFftPanel;
	FFTPanel rfFftPanel2;

	public TestWindow(String title, int sampleRate, int samples) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		
		rfFftPanel = new FFTPanel(sampleRate, 7200000, samples);
		rfFftPanel.setPreferredSize(new Dimension(1200,300));
		add(rfFftPanel, BorderLayout.NORTH);
		
		rfFftPanel2 = new FFTPanel(sampleRate, 7200000, samples);
		rfFftPanel2.setPreferredSize(new Dimension(600, 200));
		add(rfFftPanel2, BorderLayout.CENTER);
	}

	public void setRfData(double[] data) {
		rfFftPanel.setData(data);
	}
	public void setRfData2(double[] data) {
		rfFftPanel2.setData(data);
	}
}
