package tutorial5.plot;

import java.awt.Graphics;
import javax.swing.JPanel;
import org.jtransforms.fft.DoubleFFT_1D;
import tutorial3.signal.Tools;

@SuppressWarnings("serial")
public class FFTPanel extends JPanel {

	double[] data;
	long centerFrequency = 0; // in Hz
	int sampleRate = 0;
	int fftLength;
	public static final int BORDER = 30;
	DoubleFFT_1D fft;
	double[] psdBuffer;
	double binBandwidth;
	int averageNum = 5;
	boolean firstRun = true;
	
	public FFTPanel(int rate, long freq, int length) {
		sampleRate = rate;
		centerFrequency = freq;
		fftLength = length;
		binBandwidth = sampleRate/fftLength;
		psdBuffer = new double[fftLength+1];
		fft = new DoubleFFT_1D(fftLength);
	}
	
	public int getCenterFreqkHz() {
		return (int) (centerFrequency/1000.0);
	}
	
	public void setData(double[] buffer) {
		fft.complexForward(buffer);
		for (int k=0; k<buffer.length/2; k++) {
			double psd = Tools.psd(buffer[2*k],buffer[2*k+1], binBandwidth);
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

		int graphWidth = getWidth()-BORDER*2;
		int graphHeight = getHeight()-BORDER*2;
		
		// Our FFT has a fixed vertical scale
		int maxValue = 10;
		int minValue = -120;
		
		// The zero point is where the labels will go and is along the bottom of the JPanel.
		int zeroPoint = getHeight()-BORDER;
		drawVerticalScale(gr, minValue, maxValue, graphHeight, zeroPoint);
		drawHorizontalScale(gr, graphWidth, zeroPoint);

		int lastx = BORDER, lasty = zeroPoint;

		int step = 1;  // use step to minimize the number of pixels we plot. Gives a cleaner display
		if (graphWidth > fftLength)
			step = 1;
		else
			step = fftLength/graphWidth;

		// Draw the FFT result, one half at a time
		// First the negative frequencies, which we will draw on the left
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
			int x = LineChart.getRatioPosition(fftLength/2, 0, i, graphWidth/2);
			x = x + BORDER + graphWidth/2;
			gr.drawLine(lastx, lasty, x, y);
			lastx = x;
			lasty = y;
		}
	}

	private void drawHorizontalScale(Graphics gr, int graphWidth, int zeroPoint) {
		int minFreqValue = (int) (getCenterFreqkHz()-sampleRate/2000);// half the bandwidth in kHz
		int maxFreqValue = (int) (getCenterFreqkHz()+sampleRate/2000);// half the bandwidth in kHz
		int labelWidth = 50; // allow 50 pixels per label
		int numLabels = (graphWidth) / labelWidth; 
		int increment = (maxFreqValue - minFreqValue) / numLabels;
		int label = getCenterFreqkHz()-increment*numLabels/2; //freq value for this label
		gr.drawLine(BORDER, getHeight()-BORDER, BORDER, BORDER); // axis line
		for (int v=0; v < numLabels; v++) {
			int pos = LineChart.getRatioPosition(maxFreqValue, minFreqValue, label, graphWidth);
			gr.drawString(""+label, pos+BORDER, zeroPoint+15); 
			gr.drawLine(pos+BORDER, zeroPoint, pos+BORDER, zeroPoint+5);
			label = label + increment;
		}
	}

	private void drawVerticalScale(Graphics gr, int minValue, int maxValue, int graphHeight, int zeroPoint) {
		gr.drawLine(BORDER, zeroPoint, getWidth()-BORDER, zeroPoint);
		int labelHeight = 30;
		int numberOfLabels = graphHeight/labelHeight;
		if (numberOfLabels != 0) {
			int label = minValue;
			int increment = (minValue - maxValue) / numberOfLabels;
			for (int v=0; v < numberOfLabels; v++) {
				int pos = LineChart.getRatioPosition(minValue, maxValue, label, graphHeight);
				gr.drawString(""+label, 3, pos); 
				label = label - increment;
			}
		}
	}
}
