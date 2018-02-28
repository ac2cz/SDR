package tutorial2.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial2.signal.Tools;

public class WavFile {

	AudioInputStream audioStream = null; // The stream of data from the wave file
	byte[] readBuffer;
	double[] out;

	public WavFile(String fileName, int samples) throws UnsupportedAudioFileException, IOException {
		readBuffer = new byte[samples * 4];
		out = new double[readBuffer.length / 4];
		File soundFile = new File(fileName);
		audioStream = AudioSystem.getAudioInputStream(soundFile);
		System.out.println("Wavefile: " + fileName);
		System.out.println("Format: " + audioStream.getFormat());
	}

	public double[] read() throws IOException {
		if (audioStream == null || !(audioStream.available() > 0) )
			return null; 
		audioStream.read(readBuffer, 0, readBuffer.length);
		Tools.getDoublesFromBytes(out, readBuffer);
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
