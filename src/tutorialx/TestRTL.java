package tutorialx;

import javax.sound.sampled.LineUnavailableException;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import com.g0kla.rtlsdr4java.ComplexBuffer;
import com.g0kla.rtlsdr4java.DeviceException;
import com.g0kla.rtlsdr4java.Listener;
import com.g0kla.rtlsdr4java.R820TTunerController;
import com.g0kla.rtlsdr4java.RTL2832TunerController;
import com.g0kla.rtlsdr4java.TunerType;

public class TestRTL {
	
	public static void main(String[] args) throws DeviceException, LineUnavailableException, InterruptedException {
//		UsbDevice usb = new UsbDevice( (short)0x0bda,(short)0x2838);
//		
//		RtlSource rtlDataListener = new RtlSource(1);
//		usb.addListener(rtlDataListener); // can't call till init
//		
//		while(true)
//			;
//		//usb.exit();
		
		short vendorId = (short)0x0bda;
		short productId = (short)0x2838;
		
		int result = LibUsb.init(null);  // use the default context
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);

		DeviceList list = new DeviceList();
		result = LibUsb.getDeviceList(null, list);
		if (result < 0) throw new LibUsbException("Unable to get device list", result);
		
		try
		{
			// Iterate over all devices and scan for the right one
			for (Device device: list)
			{
				DeviceDescriptor descriptor = new DeviceDescriptor();

				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
				if (descriptor.idVendor() == vendorId && descriptor.idProduct() == productId) {
					System.out.println(descriptor);

					TunerType tunerType = TunerType.UNKNOWN;
					try {
						tunerType = RTL2832TunerController.identifyTunerType( device );
						System.out.println("Tuner:"+tunerType);
						R820TTunerController rtl = new R820TTunerController(device, descriptor);
						rtl.init(); // have to call this after the constructor as the shadowRegister needs to be init
						DataSink listener = new DataSink();
						rtl.addListener(listener);
						Thread.sleep(2000);
						rtl.removeListener(listener);
						Thread.sleep(1000);
						rtl.release();
					} catch( DeviceException e ) {
						//Log.println( "couldn't determine RTL2832 tuner type: " + e );
					}
					break;
				}
			}
		}
		finally
		{
			// Ensure the allocated device list is freed
			// Note that we need to not free ths list before we have opened the device that we want, otherwise that fails
			LibUsb.freeDeviceList(list, true);
		}
		Thread.sleep(1000);
		LibUsb.exit(null);
		System.out.println("Exit ...");
	}
	
	protected static class DataSink implements Listener<ComplexBuffer> {

		@Override
		public void receive(ComplexBuffer t) {
			System.out.println("Got data");
			
		}
		
	}
}

