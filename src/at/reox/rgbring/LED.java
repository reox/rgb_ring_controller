package at.reox.rgbring;

public class LED {

    // Stores the Int of the Pin
    public LED(Controller controller, int red, int green, int blue) {
	super();
	this.controller = controller;
	this.red = red;
	this.green = green;
	this.blue = blue;
    }

    private Controller controller;
    private int red;
    private int green;
    private int blue;

    public int getRed() {
	return red;
    }

    public void setRed(int red) {
	this.red = red;
    }

    public int getGreen() {
	return green;
    }

    public void setGreen(int green) {
	this.green = green;
    }

    public int getBlue() {
	return blue;
    }

    public void setBlue(int blue) {
	this.blue = blue;
    }

    public Controller getController() {
	return controller;
    }

    public void setController(Controller controller) {
	this.controller = controller;
    }

    public int getControlSequence() {
	return (controller.getNumber() << 4 | min(red, green, blue));
    }

    private int min(int a, int b, int c) {
	int m = a;
	if (m > b)
	    m = b;
	if (m > c)
	    return c;
	return m;
    }
}
