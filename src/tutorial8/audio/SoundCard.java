package tutorial8.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundCard extends Source {
	AudioFormat audioFormat;
	TargetDataLine targetDataLine; 
	byte[] readBuffer;
	boolean stereo = false;
	int errorCount = 0;
	
	public static final int READ_BUFFER_SIZE = 1024;
	
	public SoundCard(int sampleRate, int buffersize, boolean stereo) throws LineUnavailableException {
		super(buffersize);
		readBuffer = new byte[READ_BUFFER_SIZE * 4];
		audioFormat = getAudioFormat(sampleRate);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(audioFormat);
		targetDataLine.start();
		this.stereo = stereo;
	}

	private void fillBuffer() throws LineUnavailableException {
		if (targetDataLine != null) {
			while (running && targetDataLine.available() < targetDataLine.getBufferSize()*0.5)
				try {
					Thread.sleep(0, 1);  // without this, the audio can be choppy
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			targetDataLine.read(readBuffer, 0, readBuffer.length);
			try {
				super.fillBuffer(readBuffer, stereo);
			} catch (IndexOutOfBoundsException e) {
			/*	 We get this error if the circularBuffer is not being emptied fast enough.  We are filling it by reading 
				 data from the sound card as fast as it is available (real time).  The circularBuffer throws the IndexOutOfBounds
				 error from the add method only when end pointer had reached the start pointer.  This means the circularBuffer 
				 is full and the next write would destroy data.  We choose to throw away this data rather than overwrite the 
				 older data.  Is does not matter. We do not pop up a message to the user unless we accumulate a number of these issues
			*/
				errorCount++;
				if (errorCount % 10 == 0) {
					System.err.println("Missed audio from the sound card, Buffers missed: " + errorCount + " with capacity: " + buffer.getCapacity());
					if (errorCount % 100 == 0) {
						System.err.println("Cant keep up with audio from soundcard: " + e.getMessage());
					}

				}
			}
		}
	}

	public void close() {
		stop();
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

	public void run() {
		while (running) {
			try {
				fillBuffer();
			} catch (IndexOutOfBoundsException e) {
				System.err.println("No fill, buffer full");
				Thread.yield();
			} catch (LineUnavailableException e) {
				running = false;
				e.printStackTrace();
			}
		}
	}
}
