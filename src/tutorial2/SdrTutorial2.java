package tutorial2;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.jtransforms.fft.DoubleFFT_1D;
import tutorial2.signal.Tools;
import tutorial2.source.SoundCard;
import tutorial2.source.WavFile;

public class SdrTutorial2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 8000;
		//WavFile soundCard = new WavFile("cw_signals.wav");
		SoundCard soundCard = new SoundCard(sampleRate);
		MainWindow window = new MainWindow("Test Tool");

		int FFT_LENGTH = 256;
		double[] psdBuffer = new double[FFT_LENGTH/2];

		double binBandwidth = sampleRate/FFT_LENGTH;
		boolean readingData = true;
		int averageNum = 10;

		while (readingData) {
			double[] buffer = soundCard.read();
			if (buffer != null) {

				DoubleFFT_1D fft;
				fft = new DoubleFFT_1D(buffer.length);

				fft.realForward(buffer);
				psdBuffer[0] = Tools.average(psdBuffer[0],
						Tools.psd(buffer[0], buffer[0], binBandwidth), 
						averageNum); // bin 0
				for (int k=1; k<buffer.length/2; k++)
					psdBuffer[k] = Tools.average(psdBuffer[k], 
							Tools.psd(buffer[2*k],buffer[2*k+1], binBandwidth), 
							averageNum);
				psdBuffer[buffer.length/2-1] = Tools.average(psdBuffer[buffer.length/2-1], 
						Tools.psd(buffer[1],buffer[buffer.length/2],binBandwidth), 
						averageNum); // bin n/2
				window.setData(psdBuffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		}
	}
}
