package tutorialx.audio;

import com.g0kla.rtlsdr4java.ComplexBuffer;
import com.g0kla.rtlsdr4java.Listener;

public class RtlSource extends Source implements Listener<ComplexBuffer> {
	
	public RtlSource(int bufferSize) {
		super(bufferSize);
	}

	@Override
	public void receive(ComplexBuffer t) {
		float[] IQbuffer = t.getSamples();
		for (float f : IQbuffer) {
			buffer.add(f);
		}
		//System.out.println("Got samples:" + IQbuffer.length);
	}
	
	@Override
	public void run() {
		// We don't need a run method as this is called from the RTL thread
	}	
}
