package tutorial2.source;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import tutorial2.signal.Tools;

public class WavFile {

	AudioInputStream audioStream = null; // The stream of data from the wave file
	public static final int DEFAULT_READ_BUFFER_SIZE = 256 * 4; 
	byte[] readBuffer = new byte[DEFAULT_READ_BUFFER_SIZE];
	double[] out = new double[readBuffer.length / 4];

	public WavFile(String fileName) throws UnsupportedAudioFileException, IOException {
		File soundFile = null;
		soundFile = new File(fileName);
		audioStream = AudioSystem.getAudioInputStream(soundFile);
		System.out.println("Wavefile: " + fileName);
		System.out.println("Format: " + audioStream.getFormat());
	}

	public double[] read() throws IOException {
		if (audioStream == null) 
			return null; 
		else if (!(audioStream.available() > 0)) 
			return null;
		audioStream.read(readBuffer, 0, readBuffer.length);
		for (int i = 0; i < out.length; i++) {// 4 bytes for each sample. 2 in each stereo channel.
			byte[] ab = {readBuffer[4*i],readBuffer[4*i+1]};
			double value =  Tools.littleEndian2(ab,16);
			value = value /32768.0;
			out[i] = value;
		}
		return out;
	}

	public void close() {
		try {
			audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
