package at.reox.rgbring;

public class RGB {

    public RGB(int red, int green, int blue) {
	super();
	this.red = red;
	this.green = green;
	this.blue = blue;
    }

    int red;
    int green;
    int blue;

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

    public String toString() {
	return "[" + red + "|" + green + "|" + blue + "]";
    }
}
