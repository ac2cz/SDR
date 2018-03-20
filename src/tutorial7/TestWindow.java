package tutorial7;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tutorial6.plot.FFTPanel;
import tutorial6.signal.ComplexOscillator;


@SuppressWarnings("serial")
public class TestWindow extends JFrame {

	FFTPanel rfFftPanel;
	FFTPanel rfFftPanel2;
	FFTPanel audioFftPanel;
	FFTPanel audioFftPanel2;

	public TestWindow(String title, int sampleRate, int samples) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 800);
		JPanel bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		JLabel lblSampleRate = new JLabel(""+sampleRate);
		bottomPanel.add(lblSampleRate);
		//audioFftPanel = new FFTPanel(sampleRate/4, 0, samples/8, false);
		
		JPanel audioPanel = new JPanel();
		add(audioPanel, BorderLayout.CENTER);
		audioPanel.setLayout(new BorderLayout());
		
		audioFftPanel = new FFTPanel("I Real Audio", sampleRate/4, 0, samples/8, true);
		audioFftPanel.realOnly = true;
		audioFftPanel.setPreferredSize(new Dimension(600, 200));
		audioPanel.add(audioFftPanel, BorderLayout.EAST);
		
		audioFftPanel2 = new FFTPanel("Q Real Audio", sampleRate/4, 0, samples/8, true);
		audioFftPanel2.realOnly = true;
		audioFftPanel2.setPreferredSize(new Dimension(600, 200));
		audioPanel.add(audioFftPanel2, BorderLayout.CENTER);
		
		rfFftPanel = new FFTPanel("RF", sampleRate, 0, samples, true);
		rfFftPanel.setPreferredSize(new Dimension(1200,300));
		add(rfFftPanel, BorderLayout.NORTH);
		rfFftPanel2 = new FFTPanel("IF", sampleRate, 0, samples, true);
		rfFftPanel2.setPreferredSize(new Dimension(1200, 200));
		add(rfFftPanel2, BorderLayout.SOUTH);
	}

	public void setRfData(double[] data) {
		rfFftPanel.setData(data);
	}
	public void setRfData2(double[] data) {
		rfFftPanel2.setData(data);
	}
	public void setAudioData(double[] data) {
		audioFftPanel.setData(data);
	}
	public void setAudioData2(double[] data) {
		audioFftPanel2.setData(data);
	}
	public void setNco(ComplexOscillator nco) {
		rfFftPanel.setNco(nco);
	}
}
