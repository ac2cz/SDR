package tutorial7.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import tutorial5.signal.Tools;

public class SoundCard {

	AudioFormat audioFormat;
	TargetDataLine targetDataLine; 
	byte[] readBuffer;
	double[] out;
	boolean stereo = false;
	
	public SoundCard(int sampleRate, int samples, boolean stereo) throws LineUnavailableException {
		readBuffer = new byte[samples * 4];
		if (stereo)
			out = new double[samples * 2]; 
		else 
			out = new double[samples];
		audioFormat = getAudioFormat(sampleRate);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(audioFormat);
		targetDataLine.start();
		this.stereo = stereo;
	}

	public double[] read() {
		targetDataLine.read(readBuffer, 0, readBuffer.length);
		Tools.getDoublesFromBytes(out, readBuffer, stereo);
		return out;
	}

	public void stop() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	static AudioFormat getAudioFormat(int sampleRate) {
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat af = new AudioFormat(sampleRate,sampleSizeInBits,channels,signed,bigEndian); 
		System.out.println("SC Format " + af);
		return af;
	}
}
