package tutorial6.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial5.signal.Tools;

public class WavFile {

	AudioInputStream audioStream = null; // The stream of data from the wave file
	byte[] readBuffer;
	double[] out;
	boolean stereo = false;

	public WavFile(String fileName, int samples, boolean stereo) throws UnsupportedAudioFileException, IOException {
		readBuffer = new byte[samples * 4];
		if (stereo)
			out = new double[samples * 2]; 
		else 
			out = new double[samples];
		
		File soundFile = new File(fileName);
		this.stereo = stereo;
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
			int step = 4;
			if (stereo) step = 2;
			byte[] ab = {readBuffer[step*i],readBuffer[step*i+1]};
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
