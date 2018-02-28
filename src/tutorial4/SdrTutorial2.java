package tutorial4;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.jtransforms.fft.DoubleFFT_1D;
import tutorial4.audio.SoundCard;
import tutorial4.audio.WavFile;
import tutorial4.signal.Tools;
import tutorial4.audio.AudioSink;
import tutorial4.signal.FirFilter;
import tutorial2.MainWindow;

public class SdrTutorial2 {

	public static void main(String[] args) throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException {
		int sampleRate = 192000;
		int len = 4096;
		//WavFile soundCard = new WavFile("ecars_net_7255_HDSDR_20180225_174354Z_7255kHz_RF.wav", len, false);
		SoundCard soundCard = new SoundCard(sampleRate, len, false);
		MainWindow window = new MainWindow("Test Tool");
		AudioSink sink = new AudioSink(sampleRate);
		FirFilter lowPass = new FirFilter();

		double[] psdBuffer = new double[len/2];		
		double binBandwidth = sampleRate/len;
		boolean readingData = true;
		int averageNum = 10;

		while (readingData) {
			double[] buffer = soundCard.read();
			
			if (buffer != null) {
				for (int d=0; d < buffer.length; d++) {
					buffer[d] = lowPass.filter(buffer[d]);
				}
				sink.write(buffer);
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
