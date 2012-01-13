package at.reox.rgbring;

public interface LEDService {

    void setLED(LED led);

    LED getLED();

    RGB getLEDState();

    void sendRGB(int red, int green, int blue);
}
