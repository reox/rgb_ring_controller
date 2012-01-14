package at.reox.rgbring;

public interface FullRGBService {

    void registerLED(LEDService led);

    void unregisterLED(LEDService led);

    void setCommunicator(Communicator com);

    void startWorkerThread();

    void setMasterService(LEDService master);

    void setMasterControl(boolean on);
}
