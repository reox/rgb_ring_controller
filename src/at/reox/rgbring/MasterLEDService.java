package at.reox.rgbring;

public class MasterLEDService extends LEDServiceImpl {

    public boolean isMasterServiceOn() {
	return masterModus;
    }

    boolean masterModus = false;

    public void setMasterMode(boolean on) {
	masterModus = on;
    }
}
