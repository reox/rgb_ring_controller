package at.reox.rgbring;

import java.util.Arrays;
import java.util.TreeMap;

public class FullRGBServiceImpl implements FullRGBService {
    private TreeMap<Integer, LEDService> leds;

    private Communicator com;

    public FullRGBServiceImpl() {
	leds = new TreeMap<Integer, LEDService>();
    }

    private byte[] lastSend = new byte[64];

    private void tick() {
	byte[] data = new byte[64];
	Arrays.fill(data, (byte) 0x0);

	for (LEDService l : leds.values()) {
	    RGB rgb = l.getLEDState();
	    LED led = l.getLED();

	    int contr = led.getController().getNumber() << 4;

	    int red = ((contr | led.getRed()) * 2);
	    int green = ((contr | led.getGreen()) * 2);
	    int blue = ((contr | led.getBlue()) * 2);

	    // These Values are 12Bit, we need 16!
	    int rc = rgb.red << 4;
	    int gc = rgb.green << 4;
	    int bc = rgb.blue << 4;

	    if (masterControl) {
		rc = master.getLEDState().red;
		gc = master.getLEDState().green;
		bc = master.getLEDState().blue;
	    }

	    data[red] = (byte) (rc >> 8);
	    data[red + 1] = (byte) (rc & 0xFF);

	    data[green] = (byte) (gc >> 8);
	    data[green + 1] = (byte) (gc & 0xFF);

	    data[blue] = (byte) (bc >> 8);
	    data[blue + 1] = (byte) (bc & 0xFF);
	}
	// System.out.println(Arrays.toString(data));
	if (!Arrays.equals(lastSend, data)) {
	    lastSend = data;
	    com.sendData(data);
	}
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

    @Override
    public void setMasterService(LEDService master) {
	this.master = master;
    }

    private boolean masterControl = false;
    private LEDService master;

    @Override
    public void setMasterControl(boolean on) {
	masterControl = on;
    }
}
