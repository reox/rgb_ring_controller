package at.reox.rgbring.test;

import org.junit.Ignore;
import org.junit.Test;

import at.reox.rgbring.RGBService;
import at.reox.rgbring.RGBServiceImpl;

public class RGBTest {

    RGBService rgb = new RGBServiceImpl();

    @Ignore
    @Test
    public void simpleTest() {
	if (rgb.isDeviceAvailable()) {
	    rgb.sendRGBData(0xffff, 0, 0);
	}
    }

    @Test
    public void initTest() throws InterruptedException {
	if (rgb.isDeviceAvailable()) {

	    // RED
	    rgb.sendRGBData(0xffff, 0, 0);
	    Thread.sleep(1000);
	    // GREEN
	    rgb.sendRGBData(0, 0xffff, 0);
	    Thread.sleep(1000);
	    // BLUE
	    rgb.sendRGBData(0, 0, 0xffff);
	    Thread.sleep(1000);
	}
    }
}
