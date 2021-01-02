package tutorialx.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tutorialx.signal.ComplexOscillator;
import tutorialx.Sdr;
import tutorialx.plot.FFTPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements WindowListener, Runnable {

	FFTPanel rfFftPanel;
	FFTPanel rfFftPanel2;
	FFTPanel audioFftPanel;
	FFTPanel audioFftPanel2;
	Sdr sdr;

	public MainWindow(String title, Sdr sdr) {
		super(title);
		this.sdr = sdr;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		setBounds(100, 100, 900, 600);
		JPanel bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		JLabel lblSampleRate = new JLabel("Sample Rate: " +sdr.sampleRate);
		bottomPanel.add(lblSampleRate);
//		JRadioButton am = new JRadioButton("AM");
//		bottomPanel.add(am);
//		JRadioButton usb = new JRadioButton("USB");
//		bottomPanel.add(usb);
//		JRadioButton lsb = new JRadioButton("LSB");
//		bottomPanel.add(lsb);
		
		audioFftPanel = new FFTPanel("Audio", sdr.sampleRate/sdr.R, 0, sdr.sampleLength/(2*sdr.R), false);
		audioFftPanel.setPreferredSize(new Dimension(400, 200));
		add(audioFftPanel, BorderLayout.CENTER);
		
		rfFftPanel = new FFTPanel("RF", sdr.sampleRate, sdr.centerFrequency, sdr.sampleLength, true);
		rfFftPanel.setPreferredSize(new Dimension(1200,300));
		add(rfFftPanel, BorderLayout.NORTH);
		
		rfFftPanel2 = new FFTPanel("IF", sdr.sampleRate, sdr.centerFrequency, sdr.sampleLength, true);
		rfFftPanel2.setPreferredSize(new Dimension(400, 200));
		add(rfFftPanel2, BorderLayout.WEST);
		
		rfFftPanel.setNco(sdr.localOsc);
		Thread windowRefresh = new Thread(this);
		windowRefresh.start();
		setVisible(true);
	}

	double[] IQbuffer;
	double[] IFbuffer;
	double[] audioBuffer;
	
	public void run() {
		Thread.currentThread().setName("MainWindow");
		
		IQbuffer = new double[sdr.IQbuffer.length];
		IFbuffer = new double[sdr.IFbuffer.length];
		audioBuffer = new double[sdr.audioBuffer.length];
		
		// Runs until we exit
		while(true) {
			// Sleep first to avoid race conditions at start up
			try {
				Thread.sleep(1000/30); // Delay in milliseconds - about 30 times per second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (sdr.IQbuffer != null) {
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						System.arraycopy(sdr.IQbuffer, 0, IQbuffer, 0, sdr.IQbuffer.length);;
						System.arraycopy(sdr.IFbuffer, 0, IFbuffer, 0, sdr.IFbuffer.length);;
						System.arraycopy(sdr.audioBuffer, 0, audioBuffer, 0, sdr.audioBuffer.length);;
						rfFftPanel.setData(IQbuffer);
						rfFftPanel2.setData(IFbuffer);
						audioFftPanel.setData(audioBuffer);
						rfFftPanel2.setCenterFrequency(rfFftPanel.getCenterFreqkHz()*1000 - rfFftPanel.getSelectedFrequency());
					}
				});
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		sdr.exit();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
