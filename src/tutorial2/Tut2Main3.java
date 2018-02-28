package tutorial2;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.jtransforms.fft.DoubleFFT_1D;

import tutorial2.audio.SoundCard;
import tutorial2.audio.WavFile;
import tutorial2.signal.Tools;

public class Tut2Main3 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 8000;
		int len = 256;
		//WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", len);
		SoundCard soundCard = new SoundCard(sampleRate, len);
		MainWindow window = new MainWindow("Test Tool");

		double[] psdBuffer = new double[len/2];
		double binBandwidth = sampleRate/len;
		boolean readingData = true;
		int averageNum = 10;

		while (readingData) {
			double[] buffer = soundCard.read();
			if (buffer != null) {

				DoubleFFT_1D fft;
				fft = new DoubleFFT_1D(len);

				fft.realForward(buffer);
				double psd = Tools.psd(buffer[0],buffer[0], binBandwidth);
				psdBuffer[0] = Tools.average(psdBuffer[0],psd, averageNum); // bin 0
				for (int k=1; k<len/2; k++) {
					psd = Tools.psd(buffer[2*k],buffer[2*k+1], binBandwidth);
					psdBuffer[k] = Tools.average(psdBuffer[k],psd, averageNum); // bin k
				}
				// finally deal with bin n/2
				psd = Tools.psd(buffer[1],buffer[len/2],binBandwidth);
				psdBuffer[len/2-1] = Tools.average(psdBuffer[len/2-1],psd, averageNum); 
				
				window.setData(psdBuffer);
				window.setVisible(true); // causes window to be redrawn
			} else 
				readingData = false;
		}
	}
}
