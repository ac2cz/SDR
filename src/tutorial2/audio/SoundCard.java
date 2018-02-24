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
	int DEFAULT_READ_BUFFER_SIZE; 
	byte[] data;
	double[] out;

	public SoundCard(int sampleRate, int samples) throws LineUnavailableException {	
		DEFAULT_READ_BUFFER_SIZE = samples * 4; 
		out = new double[DEFAULT_READ_BUFFER_SIZE / 4];
		data = new byte[DEFAULT_READ_BUFFER_SIZE];
		audioFormat = getAudioFormat(sampleRate);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(audioFormat);
		targetDataLine.start();
	}

	public double[] read() {
		targetDataLine.read(data, 0, data.length);
		for (int i = 0; i < out.length; i++) {
			byte[] ab = {data[4*i],data[4*i+1]};
			int value =  Tools.littleEndian2(ab,16)/2^15;
			out[i] = value;
		}
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
		System.out.println("SC Format " + af);
		return af;
	}
}
