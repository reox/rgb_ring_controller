package at.reox.rgbring;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;

public class RGBServiceImpl implements RGBService {

    Device dev;

    public RGBServiceImpl() {
	// get a device instance with vendor id and product id
	short idVendor = 0x16c0;
	short idProduct = 0x05dc;

	this.dev = USB.getDevice(idVendor, idProduct);
	openDevice();
    }

    private void openDevice() {
	int bConfigurationValue = 1;
	int bInterfaceNumber = 0;

	try {
	    dev.open(bConfigurationValue, bInterfaceNumber, -1);
	} catch (USBException e) {
	    System.err.println(e.getMessage());
	}
    }

    @Override
    public boolean isDeviceAvailable() {
	openDevice();
	return dev.isOpen();
    }

    private byte[] convertRGBData(int red, int green, int blue) {
	// Hack arround here to get the Low and High bytes
	String r = pad(Integer.toHexString(red), 4);
	String g = pad(Integer.toHexString(green), 4);
	String b = pad(Integer.toHexString(blue), 4);

	byte r_hi = (byte) Integer.parseInt(r.substring(0, 2), 16);
	byte r_lo = (byte) Integer.parseInt(r.substring(2, 4), 16);

	byte g_hi = (byte) Integer.parseInt(g.substring(0, 2), 16);
	byte g_lo = (byte) Integer.parseInt(g.substring(2, 4), 16);

	byte b_hi = (byte) Integer.parseInt(b.substring(0, 2), 16);
	byte b_lo = (byte) Integer.parseInt(b.substring(2, 4), 16);

	// We choose [R, G, B] Structure
	// Also we have [Ch HI, Ch LO]
	byte[] single_led = { r_hi, r_lo, g_hi, g_lo, b_hi, b_lo };

	byte[] data = new byte[single_led.length * LEDConfig.getLedCount()];
	for (int i = 0; i < data.length; i++) {
	    data[i] = single_led[i % single_led.length];
	}

	return data;
    }

    @Override
    public void sendRGBData(int red, int green, int blue) {

	byte[] rgb = convertRGBData(red, green, blue);
	try {
	    // 0x40 = VENDOR | ENDPOINT_OUT | RECIP_DEVICE
	    int reqType = 0x40;
	    int request = 's';
	    int value = 0;
	    int index = 0;
	    int timeout = 5;
	    this.dev.controlMsg(reqType, request, value, index, rgb, rgb.length, timeout, false);
	} catch (USBTimeoutException e) {
	    // We really dont care, because we will get timeouts..
	} catch (USBException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public int ledCount() {
	return 16;
    }

    private String pad(String in, int maxlength) {
	String out = "";
	for (int i = 0; i < maxlength - in.length(); i++) {
	    out += new String("0");
	}
	out += in;
	return out;
    }

}
