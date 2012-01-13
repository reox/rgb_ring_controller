package at.reox.rgbring;

public class LEDServiceImpl implements LEDService {

    private LED led;
    private RGB current;

    public LEDServiceImpl() {
	current = new RGB(0x0200, 0x0200, 0x0200);
    }

    @Override
    public void setLED(LED led) {
	this.led = led;

    }

    @Override
    public void sendRGB(int red, int green, int blue) {
	current.setRed(red);
	current.setGreen(green);
	current.setBlue(blue);
    }

    @Override
    public RGB getLEDState() {
	return current;
    }

    @Override
    public LED getLED() {
	return led;
    }

}
