package at.reox.rgbring;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;

public class SimpleCommunicator implements Communicator {
    Device dev;

    short idVendor = 0x16c0;
    short idProduct = 0x05dc;
    int bConfigurationValue = 1;
    int bInterfaceNumber = 0;

    public SimpleCommunicator() {
	// get a device instance with vendor id and product id
	initDevice();
	openDevice();
    }

    @Override
    public boolean isDeviceOpen() {
	openDevice();
	return dev.isOpen();
    }

    @Override
    public void sendData(byte[] data) {
	try {
	    // 0x40 = VENDOR | ENDPOINT_OUT | RECIP_DEVICE
	    int reqType = 0x40;
	    int request = 'r';
	    int value = 0;
	    int index = 0;
	    int timeout = -1;
	    this.dev.controlMsg(reqType, request, value, index, data, data.length, timeout, false);
	} catch (USBTimeoutException e) {
	    // We really dont care, because we will get timeouts..
	    e.printStackTrace();

	} catch (USBException e) {
	    initDevice();
	    System.err.println(e.getMessage());
	}
    }

    private void initDevice() {
	System.out.println("Init");
	USB.init();
	try {
	    if (dev != null) {
		dev.reset();
		dev = null;
	    }
	} catch (USBException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	this.dev = USB.getDevice(idVendor, idProduct);
	try {
	    dev.open(bConfigurationValue, bInterfaceNumber, -1);
	} catch (USBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void openDevice() {

	try {
	    if (!dev.isOpen()) {
		dev.open(bConfigurationValue, bInterfaceNumber, -1);
	    }
	} catch (USBException e) {
	    System.err.println(e.getMessage());
	}
    }

}
