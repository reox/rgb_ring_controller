package at.reox.rgbring;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	final Main m = new Main();
	m.initLEDs();
	m.init();
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		m.setTray();

	    }
	});

    }

    List<LED> leds;

    private void initLEDs() {
	Controller one = new Controller(0, 16);
	Controller two = new Controller(1, 16);

	// Channel 00 -> 15: RBG
	// Channel 16 -> 31: GRB

	leds = new ArrayList<LED>();
	leds.add(new LED(one, 2, 1, 3));
	leds.add(new LED(one, 5, 4, 6));
	leds.add(new LED(one, 8, 7, 9));
	leds.add(new LED(one, 11, 10, 12));
	leds.add(new LED(one, 14, 13, 15));
	leds.add(new LED(two, 2, 1, 3));
	leds.add(new LED(two, 5, 4, 6));
	leds.add(new LED(two, 8, 7, 9));
	leds.add(new LED(two, 11, 10, 12));
	leds.add(new LED(two, 14, 13, 15));
    }

    private boolean isVisible = false;
    private JFrame window;

    private void init() {
	SettingsFrame sui = new SettingsFrame();
	sui.startUi();
	sui.setResizable(false);
	sui.setTitle("RGB-Ring GUI");

	List<SettingsUI> frames = new ArrayList<SettingsUI>();
	FullRGBService serv = new FullRGBServiceImpl();
	serv.setCommunicator(new SimpleCommunicator());
	for (LED l : leds) {
	    SettingsUI s = new SettingsUI();
	    LEDService ls = new LEDServiceImpl();
	    ls.setLED(l);
	    s.setLEDService(ls);
	    frames.add(s);
	    serv.registerLED(ls);
	}

	SettingsUI master = new SettingsUI();
	LEDService masterS = new LEDServiceImpl();
	masterS.setLED(new LED(new Controller(0, 0), 0, 0, 0)); // pseudo service
	master.setLEDService(masterS);

	sui.setLEDFrames(master, frames);
	sui.pack();
	serv.startWorkerThread();
	window = sui;
    }

    private void setTray() {
	final TrayIcon trayIcon;

	if (SystemTray.isSupported()) {

	    SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");

	    MouseListener mouseListener = new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {
		    if (isVisible) {
			window.setVisible(false);
			isVisible = false;
		    } else {
			window.setVisible(true);
			isVisible = true;
		    }
		}
	    };

	    ActionListener exitListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.out.println("Exiting...");
		    System.exit(0);
		}
	    };

	    PopupMenu popup = new PopupMenu();
	    MenuItem defaultItem = new MenuItem("Exit");
	    defaultItem.addActionListener(exitListener);
	    popup.add(defaultItem);

	    trayIcon = new TrayIcon(image, "RGB Ring Control", popup);

	    trayIcon.setImageAutoSize(true);
	    trayIcon.addMouseListener(mouseListener);

	    try {
		tray.add(trayIcon);
	    } catch (AWTException e) {
		System.err.println("TrayIcon could not be added.");
	    }
	}
    }
}
