package at.reox.rgbring;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class SettingsFrame extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -3891970602245864133L;
    private JPanel panel;

    SettingsFrame() {
	panel = new JPanel(new MigLayout("fill"));
	device = new JLabel();
	panel.add(device, "span, wrap");

	this.setContentPane(panel);
    }

    public void setLEDFrames(SettingsUI master, List<SettingsUI> uis) {
	panel.add(master.getUI(), "span, wrap, center");

	int c = 1;
	for (SettingsUI s : uis) {
	    String options = "";
	    if (c++ % 3 == 0) {
		options = "wrap";
	    }
	    panel.add(s.getUI(), options);
	}
    }

    private int counter = 0;
    private JLabel device;

    private void updateDevice() {
	// if (service.isDeviceAvailable()) {
	// device.setText("Device Available");
	// } else {
	// String dot = "";
	// counter++;
	// for (int i = 0; i < counter % 4; i++) {
	// dot += ". ";
	// }
	// device.setText("Device Unavailable " + dot);
	// }
    }

    public void startUi() {
	Thread devLookup = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    try {
			updateDevice();
			Thread.sleep(1000);
		    } catch (InterruptedException e) {
			// Ignore
		    }
		}
	    }
	});
	devLookup.start();
    }

}
