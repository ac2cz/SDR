package tutorialx;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class TestUSB {

	public static void main(String[] args) {
		int result = LibUsb.init(null);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
		
		// Read the USB device list
	    DeviceList list = new DeviceList();
	    result = LibUsb.getDeviceList(null, list);
	    if (result < 0) throw new LibUsbException("Unable to get device list", result);

	    try {
	        // Iterate over all devices and scan for the right one
	        for (Device device: list)
	        {
	            DeviceDescriptor descriptor = new DeviceDescriptor();
	            result = LibUsb.getDeviceDescriptor(device, descriptor);
	            if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
	            
            	System.out.println(descriptor);
	        }
	    }
	    finally {
	        // Ensure the allocated device list is freed
	    	// Note that we need to not free ths list before we have opened the device that we want, otherwise that fails
	        LibUsb.freeDeviceList(list, true);
	    }
	}
}
