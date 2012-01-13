package at.reox.rgbring;

public class Controller {

    public Controller(int number, int pins) {
	super();
	this.number = number;
	this.pins = pins;
    }

    int number;
    int pins;

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public int getPins() {
	return pins;
    }

    public void setPins(int pins) {
	this.pins = pins;
    }

}
