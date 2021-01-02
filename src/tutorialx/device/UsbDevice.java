package tutorialx.device;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import com.g0kla.rtlsdr4java.ComplexBuffer;
import com.g0kla.rtlsdr4java.DeviceException;
import com.g0kla.rtlsdr4java.Listener;
import com.g0kla.rtlsdr4java.R820TTunerController;
import com.g0kla.rtlsdr4java.RTL2832TunerController;
import com.g0kla.rtlsdr4java.TunerType;

public class UsbDevice {
	public static final int INTERFACE_0 = 0;
	public DeviceHandle deviceHandle;
	public Device device;
	public DeviceDescriptor deviceDescriptor;
	R820TTunerController rtl;

	public UsbDevice(short vendorId, short productId) throws DeviceException {
		// Initialize the default context
		int result = LibUsb.init(null);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);

		rtl = findDevice(vendorId, productId);
		if (rtl == null) throw new DeviceException("RTL not available,  is it plugged in?  Does it have the updated drivers?");
	}

	public void setTunedFrequency( long frequency ) throws DeviceException {
		rtl.setTunedFrequency(frequency);
	}

	public void addListener(Listener<ComplexBuffer> listener) {
		rtl.addListener(listener);
	}

	public void exit() {
		System.out.println("Trying to exit the device");
		try {
			rtl.release();
		} catch (DeviceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(300); // wait for the library to be ready to exit or we get a crash
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LibUsb.exit(null);
	}

	R820TTunerController findDevice(short vendorId, short productId) throws DeviceException {
		// Read the USB device list
		DeviceList list = new DeviceList();
		int result = LibUsb.getDeviceList(null, list);
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
					//	            	ConfigDescriptor config = new ConfigDescriptor();
					//	            	result = LibUsb.getConfigDescriptor(device, (byte) 0, config);
					//					if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read config descriptor", result);
					//	            	System.out.println(config);
					this.device = device;
					this.deviceDescriptor = descriptor;
					TunerType tunerType = TunerType.UNKNOWN;
					tunerType = RTL2832TunerController.identifyTunerType( device );
					System.out.println("Found tuner: " + tunerType);
					rtl = new R820TTunerController(device, descriptor);
					rtl.init(); // have to call this after the constructor as the shadowRegister needs to be init
					return rtl;
				}
			}
		}
		finally
		{
			// Ensure the allocated device list is freed
			// Note that we need to not free ths list before we have opened the device that we want, otherwise that fails
			LibUsb.freeDeviceList(list, true);
		}
		return null;
	}
}
