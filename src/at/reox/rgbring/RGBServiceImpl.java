package at.reox.rgbring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;

public class RGBServiceImpl implements RGBService {

    Device dev;

    public RGBServiceImpl() {
	// get a device instance with vendor id and product id
	initDevice();
	openDevice();
    }

    private void initDevice() {
	short idVendor = 0x16c0;
	short idProduct = 0x05dc;

	USB.init();
	this.dev = USB.getDevice(idVendor, idProduct);
    }

    private void openDevice() {
	int bConfigurationValue = 1;
	int bInterfaceNumber = 0;

	try {
	    if (!dev.isOpen()) {
		dev.open(bConfigurationValue, bInterfaceNumber, -1);
	    }
	} catch (USBException e) {
	    System.err.println(e.getMessage());
	}
    }

    @Override
    public boolean isDeviceAvailable() {
	openDevice();
	return dev.isOpen();
    }

    private int lr = 0, lg = 0, lb = 0;

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

	// We choose [R, B, G] Structure NO THIS IS NO MISTAKE!
	// Also we have [Ch HI, Ch LO]

	// Channel 00 -> 15: RBG
	// Channel 16 -> 31: GRB

	byte[] single_rbg = { r_hi, r_lo, b_hi, b_lo, g_hi, g_lo };
	byte[] single_grb = { g_hi, g_lo, r_hi, r_lo, b_hi, b_lo };

	byte[] data = new byte[6 * LEDConfig.getLedCount() + 4]; // +4 because
								 // of
								 // unused
								 // pins
	List<Integer> reserved = new ArrayList<Integer>();
	reserved.add(0);
	reserved.add(1);
	reserved.add(32);
	reserved.add(33);
	for (int i = 0; i < data.length; i++) {
	    if (reserved.contains(i)) {
		// Unused Pin is the Last on the Controller
		// so just set it to 0
		data[i] = 0;
	    } else {
		// We have two different controller setups...
		if (i < data.length / 2) {
		    data[i] = single_rbg[i % single_rbg.length];
		} else {
		    data[i] = single_grb[i % single_grb.length];
		}
	    }
	}
	System.out.println(data.length + ": " + Arrays.toString(data));

	return data;
    }

    @Override
    public void sendRGBData(int red, int green, int blue) {
	// If we had the request allready...
	if (lr == red && lg == green && lb == blue) {
	    return;
	}
	lr = red;
	lg = green;
	lb = blue;

	byte[] rgb = convertRGBData(red, green, blue);
	try {
	    // 0x40 = VENDOR | ENDPOINT_OUT | RECIP_DEVICE
	    int reqType = 0x40;
	    int request = 'r';
	    int value = 0;
	    int index = 0;
	    int timeout = -1;
	    this.dev.controlMsg(reqType, request, value, index, rgb, rgb.length, timeout, false);
	} catch (USBTimeoutException e) {
	    // We really dont care, because we will get timeouts..
	    e.printStackTrace();

	} catch (USBException e) {
	    initDevice();
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
