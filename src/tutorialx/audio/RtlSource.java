package tutorialx.audio;

import com.g0kla.rtlsdr4java.ComplexBuffer;
import com.g0kla.rtlsdr4java.Listener;

public class RtlSource extends Source implements Listener<ComplexBuffer> {
	int bufferSize;

	public RtlSource(int bufferSize) {
		super(bufferSize);
		this.bufferSize = bufferSize;
	}
	
	int count = 0;

	@Override
	public void receive(ComplexBuffer t) {
		float[] IQbuffer = t.getSamples();
		for (float f : IQbuffer) {
			buffer.add(f);
		}
		count++;
		if (count == 50) {
			count = 0;
			System.err.println("Buffer capacity: "+ buffer.getCapacity() + " filled percent:" + 100*buffer.size()/(double)bufferSize);
		}
		//System.out.println("Got samples:" + IQbuffer.length);
	}
	
	@Override
	public void run() {
		// We don't need a run method as this is called from the RTL-SDR which is already a seperate thread	
	}	
}
