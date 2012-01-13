package at.reox.rgbring;

import java.util.Arrays;
import java.util.TreeMap;

public class FullRGBServiceImpl implements FullRGBService {
    private TreeMap<Integer, LEDService> leds;

    private Communicator com;

    public FullRGBServiceImpl() {
	leds = new TreeMap<Integer, LEDService>();
    }

    private void tick() {
	byte[] data = new byte[64];
	Arrays.fill(data, (byte) 0x0);

	for (LEDService l : leds.values()) {
	    int seq = l.getLED().getControlSequence();
	    // System.out.println(seq + "" + l.getLEDState().toString());
	    byte[] temp = convertRGBData(l.getLEDState(), l.getLED());
	    for (int i = seq * 2; i < (seq * 2) + 6; i++) {
		data[i] = temp[i - (seq * 2)];
	    }
	}
	// System.out.println(Arrays.toString(data));
	com.sendData(data);
    }

    private byte[] convertRGBData(RGB rgb, LED led) {
	byte r_hi = (byte) (rgb.red >> 8);
	byte r_lo = (byte) (rgb.red & 0xFF);

	byte g_hi = (byte) (rgb.green >> 8);
	byte g_lo = (byte) (rgb.green & 0xFF);

	byte b_hi = (byte) (rgb.blue >> 8);
	byte b_lo = (byte) (rgb.blue & 0xFF);

	byte[] data = new byte[6];

	data[(led.getRed() % 3) * 2] = r_hi;
	data[((led.getRed() % 3) * 2) + 1] = r_lo;

	data[(led.getGreen() % 3) * 2] = g_hi;
	data[((led.getGreen() % 3) * 2) + 1] = g_lo;

	data[(led.getBlue() % 3) * 2] = b_hi;
	data[((led.getBlue() % 3) * 2) + 1] = b_lo;

	return data;
    }

    @Override
    public void registerLED(LEDService led) {
	leds.put(led.getLED().getControlSequence(), led);
    }

    @Override
    public void unregisterLED(LEDService led) {
	leds.remove(led);
    }

    @Override
    public void setCommunicator(Communicator com) {
	this.com = com;
    }

    @Override
    public void startWorkerThread() {
	Thread t = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    tick();
		    try {
			Thread.sleep(100);
		    } catch (InterruptedException e) {
		    }
		}
	    }
	});
	t.start();
    }
}
