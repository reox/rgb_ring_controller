package at.reox.rgbring;

public interface Communicator {

    public boolean isDeviceOpen();

    public void sendData(byte[] data);
}
