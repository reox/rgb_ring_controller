package at.reox.rgbring;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	SettingsUI sui = new SettingsUI();
	RGBServiceImpl rgb = new RGBServiceImpl();
	sui.setRGBService(rgb);
	sui.startUi();
	sui.setResizable(false);
    }
}
