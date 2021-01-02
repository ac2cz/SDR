package tutorialx.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import tutorialx.signal.Tools;

public class AudioSink {
	
	AudioFormat audioFormat;
	SourceDataLine sourceDataLine;
	boolean stereo = true;
	
	public AudioSink(int sampleRate) throws LineUnavailableException {		
		audioFormat = SoundCard.getAudioFormat(sampleRate);
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
		sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
	}
	
	public void write(double[] f) {
		byte[] audioData = new byte[f.length*audioFormat.getFrameSize()];
		boolean stereo = false;
		if (audioFormat.getChannels() == 2) stereo = true;
		// assume we copy MONO stream of data to stereo channels
		Tools.getBytesFromDoubles(f, f.length, stereo, audioData); 
		write(audioData);
	}
	
	public void write(byte[] myData) {
		sourceDataLine.write(myData, 0, myData.length);
	}

	public void stop() {
		sourceDataLine.drain();
		sourceDataLine.close();
	}
}
