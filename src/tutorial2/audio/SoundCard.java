package tutorial2.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import tutorial2.signal.Tools;

public class SoundCard {

	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	byte[] readBuffer;
	double[] out;

	public SoundCard(int sampleRate, int samples) throws LineUnavailableException {	
		readBuffer = new byte[samples * 4];
		out = new double[readBuffer.length / 4];
		audioFormat = getAudioFormat(sampleRate);
		System.out.println("Source Format: " + audioFormat);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(audioFormat);
		targetDataLine.start();
	}

	public double[] read() {
		targetDataLine.read(readBuffer, 0, readBuffer.length);
		Tools.getDoublesFromBytes(out, readBuffer);
		return out;
	}

	public void stop() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	private AudioFormat getAudioFormat(int sampleRate) {
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat af = new AudioFormat(sampleRate,sampleSizeInBits,channels,signed,bigEndian); 
		return af;
	}
}
