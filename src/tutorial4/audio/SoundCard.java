package tutorial4.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import tutorial3.signal.Tools;

public class SoundCard {

	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	public static final int DEFAULT_READ_BUFFER_SIZE = 4096 * 4; 
	byte[] data = new byte[DEFAULT_READ_BUFFER_SIZE];
	double[] out = new double[DEFAULT_READ_BUFFER_SIZE / 4];
	boolean stereo = false;
	
	public SoundCard(int sampleRate, boolean stereo) throws LineUnavailableException {		
		audioFormat = getAudioFormat(sampleRate);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(audioFormat);
		targetDataLine.start();
		this.stereo = stereo;
	}

	public double[] read() {
		targetDataLine.read(data, 0, data.length);
		for (int i = 0; i < out.length; i++) {
			int step = 4;
			if (stereo) step = 2;
			byte[] ab = {data[step*i],data[step*i+1]};
			double value =  Tools.littleEndian2(ab,16)/32768.0;
			out[i] = value;
		}
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
	
	/**
	 * Converts an array of doubles to audio bytes.  We assume that the input is mono.  The stereo flag just tells us if we should copy
	 * the data to both channels or not
	 * 
	 * @param audioData
	 * @param storedSamples
	 * @param stereo
	 * @param audioDataBytes
	 */
	static public void getBytesFromDoubles(final double[] audioData, final int storedSamples, boolean stereo, byte[] audioDataBytes) {
		int k = 0;
		if (!stereo) {
			for (int i = 0; i < storedSamples; i++) {
				// saturation
				audioData[i] = Math.min(1.0, Math.max(-1.0, audioData[i]));

				// scaling and conversion to integer
				int sample = (int) Math.round((audioData[i] + 1.0) * 32767.5) - 32768;

				byte high = (byte) ((sample >> 8) & 0xFF);
				byte low = (byte) (sample & 0xFF);
				audioDataBytes[k] = low;
				audioDataBytes[k + 1] = high;
				k = k + 2;
			}
		} else {
			// STEREO
			for (int i = 0; i < storedSamples; i++) {
				// saturation
				audioData[i] = Math.min(1.0, Math.max(-1.0, audioData[i]));

				// scaling and conversion to integer
				int sample = (int) Math.round((audioData[i] + 1.0) * 32767.5) - 32768;

				byte high = (byte) ((sample >> 8) & 0xFF);
				byte low = (byte) (sample & 0xFF);
				audioDataBytes[k] = low;
				audioDataBytes[k + 1] = high;
				// Put the same in the other channel
				audioDataBytes[k + 2] = low;
				audioDataBytes[k + 3] = high;
				k = k + 4;
			}				
		}

		//return audioDataBytes;
	}
}
