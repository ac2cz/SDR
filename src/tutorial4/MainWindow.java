package tutorial4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tutorial4.plot.FFTPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	FFTPanel rfFftPanel;
	FFTPanel audioFftPanel;

	public MainWindow(String title, int sampleRate, int samples) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		JPanel bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		JLabel lblSampleRate = new JLabel(""+sampleRate);
		bottomPanel.add(lblSampleRate);
		audioFftPanel = new FFTPanel(sampleRate, 0, samples/2, false);
		audioFftPanel.setPreferredSize(new Dimension(1200, 200));
		add(audioFftPanel, BorderLayout.CENTER);
		rfFftPanel = new FFTPanel(sampleRate, 7200000, samples, true);
		rfFftPanel.setPreferredSize(new Dimension(1200,200));
		add(rfFftPanel, BorderLayout.NORTH);
		//
	}

	public void setRfData(double[] data) {
		rfFftPanel.setData(data);
	}
	public void setAudioData(double[] data) {
		audioFftPanel.setData(data);
	}
}
