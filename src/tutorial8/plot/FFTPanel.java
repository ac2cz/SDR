package tutorial8.plot;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import org.jtransforms.fft.DoubleFFT_1D;

import tutorial8.plot.LineChart;
import tutorial8.signal.ComplexOscillator;
import tutorial8.signal.Tools;

@SuppressWarnings("serial")
public class FFTPanel extends JPanel implements MouseListener {

	public static final boolean COMPLEX = true;
	public static final boolean REAL = false;
	
	double[] data;
	long centerFrequency = 0; // in kHz
	int sampleRate = 0;
	int fftLength;
	public static final int BORDER = 30;
	DoubleFFT_1D fft;
	double[] psdBuffer;
	double binBandwidth;
	int averageNum = 10;
	int selectedBin;
	boolean firstRun = true;
	boolean complexOrReal = true;
	ComplexOscillator nco = null;
	
	public boolean realOnly = false;
	public boolean imagOnly = false;
	
	String name;
	
	public FFTPanel(String name, int rate, long freq, int length, boolean complexOrReal) {
		sampleRate = rate;
		centerFrequency = freq;
		fftLength = length;
		this.name = name;
		setFFTLength(length);
		addMouseListener(this);
		this.complexOrReal = complexOrReal;
	}

	public void setNco(ComplexOscillator localOsc) {
		this.nco = localOsc;
	}
	
	public void setCenterFrequency(long freq) {
		centerFrequency = freq;
	}
	
	public int getCenterFreqkHz() {
		return (int) (centerFrequency/1000.0);
	}

	public long getSelectedFrequency() {
		return binToFrequency(selectedBin);
	}
	
	public void setSampleRate(int rate) {
		this.sampleRate = rate; 
		binBandwidth = (double)sampleRate/(double)fftLength;
	}
	
	public void setFFTLength(int len) {
		this.fftLength = len;
		psdBuffer = new double[fftLength+1];
		fft = new DoubleFFT_1D(fftLength);
		binBandwidth = (double)sampleRate/(double)fftLength;
	}
	
	public void setData(double[] buffer) {
		if (complexOrReal == COMPLEX) {
			fft.complexForward(buffer);
		} else
			fft.realForward(buffer);
		for (int k=0; k<buffer.length/2; k++) {
			double psd = 0;
			if (realOnly)
				psd = buffer[2*k];
			else if (imagOnly)
				psd = buffer[2*k+1];
			else
				psd = Tools.psd(buffer[2*k],buffer[2*k+1], fftLength);
			if (firstRun)
				psdBuffer[k] = psd; // we do not have an average yet
			else
				psdBuffer[k] = Tools.average(psdBuffer[k], psd, averageNum);
		}
		firstRun = false;
		this.data = psdBuffer;
		repaint();
	}

	public void paintComponent(Graphics gr) {
		super.paintComponent( gr ); // call superclass's paintComponent  
		Graphics2D g2 = (Graphics2D) gr;
		
		int graphWidth = getWidth()-BORDER*2;
		int graphHeight = getHeight()-BORDER*2;

		gr.drawString(name, graphWidth/2, BORDER/2); 

		// Our FFT has a fixed vertical scale
		double maxValue = 10;
		double minValue = -100;
		if (realOnly) {
			maxValue = 0.5;
			minValue = -0.5;
		}
		if (imagOnly) {
			maxValue = 5.0;
			minValue = -0.5;
			
		}
		
		// The zero point is where the labels will go and is along the bottom of the JPanel.
		int zeroPoint = getHeight()-BORDER;
		drawVerticalScale(gr, minValue, maxValue, graphHeight, zeroPoint);
		drawHorizontalScale(gr, graphWidth, zeroPoint);
		
		int selection = getSelectionFromBin(selectedBin);

		int c = LineChart.getRatioPosition(0, fftLength, selection, graphWidth);
		if (complexOrReal == COMPLEX)
			gr.drawLine(c+BORDER, BORDER, c+BORDER, zeroPoint);

		int lastx = BORDER, lasty = zeroPoint;

		int step = 1;
		if (graphWidth > fftLength)
			step = 1;
		else
			step = fftLength/graphWidth;
		if (step < 0) step = 1;

		// Draw the FFT result, one half at a time
		// First the negative frequencies, which we will draw on the left
		if (data != null) {
			if (complexOrReal == COMPLEX)
			for (int n=fftLength/2; n< (fftLength); n+=step) {
				int y = LineChart.getRatioPosition(minValue, maxValue, data[n], graphHeight);
				int x = LineChart.getRatioPosition(fftLength/2, 0, n-fftLength/2, graphWidth/2);
				x = x + BORDER;
				gr.drawLine(lastx, lasty, x, y);
				lastx = x;
				lasty = y;
			}
			// Then the positive frequencies, which we draw on the right
			for (int i=0; i< (fftLength/2); i+=step) {
				int y = LineChart.getRatioPosition(minValue, maxValue, data[i], graphHeight);
				int x = 0;
				if (complexOrReal == COMPLEX) {
					x = LineChart.getRatioPosition(fftLength/2, 0, i, graphWidth/2);
					x = x + BORDER + graphWidth/2;
				} else {
					x = LineChart.getRatioPosition(fftLength/2, 0, i, graphWidth);
					x = x + BORDER;
				}
				gr.drawLine(lastx, lasty, x, y);
				lastx = x;
				lasty = y;
			}
		}
	}

	private void drawHorizontalScale(Graphics gr, int graphWidth, int zeroPoint) {
		int minFreqValue = 0;
		int maxFreqValue = 0;
		if (complexOrReal == COMPLEX) {
			minFreqValue = (int) (getCenterFreqkHz()-sampleRate/2000);//96;
			maxFreqValue = (int) (getCenterFreqkHz()+sampleRate/2000);//96;
		} else {
			minFreqValue = 0;
			maxFreqValue = (int) (getCenterFreqkHz()+sampleRate/2000);//96;
		}
		int labelWidth = 50; // allow 50 pixels per label
		int numLabels = (graphWidth) / labelWidth; 
		if (numLabels == 0) numLabels = 1; // avoid divide by zero
		int increment = (maxFreqValue - minFreqValue) / numLabels;
		int label = getCenterFreqkHz()-increment*numLabels/2;
		gr.drawLine(BORDER, getHeight()-BORDER, BORDER, BORDER);
		for (int v=0; v < numLabels; v++) {
			int pos = LineChart.getRatioPosition(maxFreqValue, minFreqValue, label, graphWidth);
			gr.drawString(""+label, pos+BORDER, zeroPoint+15); 
			gr.drawLine(pos+BORDER, zeroPoint, pos+BORDER, zeroPoint+5);
			label = label + increment;
		}
	}

	private void drawVerticalScale(Graphics gr, double minValue, double maxValue, int graphHeight, int zeroPoint) {
		gr.drawLine(BORDER, zeroPoint, getWidth()-BORDER, zeroPoint);
		int labelHeight = 30;
		// calculate number of labels we need on vertical axis
		int numberOfLabels = graphHeight/labelHeight;
		if (numberOfLabels != 0) {
			int label = (int) minValue;
			int increment = (int) ((minValue - maxValue) / numberOfLabels);
			for (int v=0; v < numberOfLabels; v++) {
				int pos = LineChart.getRatioPosition(minValue, maxValue, label, graphHeight);
				gr.drawString(""+label, 10, pos); 
				label = label - increment;
			}
		}
	}

	private int binToFrequencyInkHz(int bin) {
		return (int) (binToFrequency(bin)/1000);
	}
	
	private long binToFrequency(int bin) {
		long freq = 0;
		if (bin < fftLength/2) {
			freq = (long)(bin*binBandwidth);
		} else {
			freq = (long)( -1* (fftLength-bin)*binBandwidth);
		}
		return freq;
	}
	
	private int getSelectionFromBin(int bin) {
		int selection;
		if (bin < fftLength/2)
			selection = bin + fftLength/2;
		else
			selection = bin - fftLength/2;

		return selection;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int x=e.getX();
	    x = x - BORDER;
		int selection = LineChart.getRatioPosition(0, getWidth()-BORDER*2, x, fftLength );
		if (selection >= fftLength/2) 
			selectedBin = selection - fftLength/2;
		else
			selectedBin = selection + fftLength/2;
		System.out.println(x+" is fft bin "+selectedBin);//these co-ords are relative to the component
		System.out.println("Tuned to: " + (-1*binToFrequency(selectedBin)));
		if (nco != null)
			nco.setFrequency(-1*binToFrequency(selectedBin)); // flip the sign as we move the spectrum in the opposite direction
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
