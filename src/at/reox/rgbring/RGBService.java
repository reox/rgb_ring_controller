package at.reox.rgbring;

public interface RGBService {

    public boolean isDeviceAvailable();

    public void sendRGBData(int red, int green, int blue);
}
