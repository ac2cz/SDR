package tutorialx.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import tutorial8.signal.Tools;

public class WavFile extends Source {
	AudioInputStream audioStream = null; // The stream of data from the wave file
	byte[] readBuffer;
	boolean stereo = false;
		
	public static final int READ_BUFFER_SIZE = 256;

	public WavFile(String fileName, int bufferSize, boolean stereo) throws UnsupportedAudioFileException, IOException {
		super(bufferSize);
		readBuffer = new byte[READ_BUFFER_SIZE * 4];
		
		File soundFile = new File(fileName);
		this.stereo = stereo;
		audioStream = AudioSystem.getAudioInputStream(soundFile);
		System.out.println("Wavefile: " + fileName);
		System.out.println("Format: " + audioStream.getFormat());
	}
	
	public void fillBuffer() throws IOException {
		if (!(audioStream.available() > 0)) throw new IndexOutOfBoundsException();
		if (buffer.getCapacity() <= readBuffer.length) throw new IndexOutOfBoundsException();
		audioStream.read(readBuffer, 0, readBuffer.length);
		super.fillBuffer(readBuffer, stereo);
	}

	public void close() {
		stop();
		try {
			audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		if (audioStream == null) {
			return; 
		} 
		while (running) {
			try {
				fillBuffer();
			} catch (IndexOutOfBoundsException e) {
				// wait for some data to be available
				try {Thread.sleep(1);} catch (InterruptedException e1) { e1.printStackTrace();}
			} catch (IOException e) {
				running = false;
				e.printStackTrace();
			}
		}
		
	}


}
