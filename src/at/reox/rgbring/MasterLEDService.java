package at.reox.rgbring;

public class MasterLEDService extends LEDServiceImpl {

    public boolean isMasterServiceOn() {
	return masterModus;
    }

    boolean masterModus = false;

    public void setMasterMode(boolean on) {
	masterModus = on;
    }

    CallBack cb;

    public void registerCallBack(CallBack c) {
	this.cb = c;
    }

    @Override
    public void sendRGB(int red, int green, int blue) {
	// TODO Auto-generated method stub
	super.sendRGB(red, green, blue);
	cb.callMe(this.getLEDState());
    }
}
