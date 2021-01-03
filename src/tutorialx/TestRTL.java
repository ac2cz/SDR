package tutorialx;

import javax.sound.sampled.LineUnavailableException;

import com.g0kla.rtlsdr4java.ComplexBuffer;
import com.g0kla.rtlsdr4java.DeviceException;
import com.g0kla.rtlsdr4java.Listener;
import tutorialx.device.UsbDevice;

public class TestRTL {
	
	public static void main(String[] args) throws DeviceException, LineUnavailableException, InterruptedException {
		int sampleRate = 240000;
		UsbDevice usb = new UsbDevice( (short)0x0bda,(short)0x2838, sampleRate);
		
		DataSink rtlSource = new DataSink();
		usb.addListener(rtlSource); // can't call till init
		
		while(true)
			;
		//usb.exit();
		
	}
	
	protected static class DataSink implements Listener<ComplexBuffer> {

		@Override
		public void receive(ComplexBuffer t) {
			float[] IQbuffer = t.getSamples();
			System.out.println("Got samples " + IQbuffer.length);
			
		}
		
	}
}

