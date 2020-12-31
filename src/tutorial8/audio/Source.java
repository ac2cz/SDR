package tutorial8.audio;

import tutorial8.signal.Tools;

public abstract class Source implements Runnable {
	CircularDoubleBuffer buffer;
	boolean running = true;
	
	protected Source(int bufferSize) {
		buffer = new CircularDoubleBuffer(bufferSize);
	}
	
	public void fillBuffer(byte[] readBuffer, boolean stereo) {
		int step = 4;
		if (stereo) step = 2;
		
		for (int i = 0; i < readBuffer.length/step; i++) {// 4 bytes for each sample. 2 in each stereo channel.
			byte[] ab = {readBuffer[step*i],readBuffer[step*i+1]};
			double value =  Tools.littleEndian2(ab,16);
			value = value /32768.0;
			buffer.add(value);
		}
	}
	
	public int read(double[] readBuffer) { 
		int doublesRead = 0;

		// We block until we have read readBuffer length bytes, assuming we are still running
		while (running && doublesRead < readBuffer.length) { // 2 bytes for each sample
			if (buffer.size() > 2) {// if we have at least one set of bytes, then read them
				try {	
					readBuffer[doublesRead] = buffer.read();
					doublesRead+=1;
				} catch (IndexOutOfBoundsException e) {
					// If this happens, we are in an unusual situation.  We waited until the circularBuffer contains readBuffer.length of data
					// then we started to read it one byte at a time.  However, we have moved the read (start) pointer as far as the end
					// pointer, so we have run out of data.
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(0, 1); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		return doublesRead;
	}
	
	protected void stop() {
		running = false;
	}
}
