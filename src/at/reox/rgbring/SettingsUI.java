package at.reox.rgbring;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

public class SettingsUI extends JFrame implements ChangeListener {

    private static final long serialVersionUID = -8313330695481436336L;

    private JPanel panel;
    private JSlider red, green, blue;
    private JLabel redl, greenl, bluel;
    private JLabel device;

    private RGBService rgb;

    private JPanel color;

    private int counter = 0;

    private boolean snapmode = false;

    public SettingsUI() {
	panel = new JPanel(new MigLayout());
	this.setContentPane(panel);

	int maxvalue = 65535;
	int minvalue = 0;

	final int startcolor = 0x0100;

	redl = new JLabel(startcolor + "");
	greenl = new JLabel(startcolor + "");
	bluel = new JLabel(startcolor + "");

	redl.setText("0x" + Integer.toHexString(startcolor));
	greenl.setText("0x" + Integer.toHexString(startcolor));
	bluel.setText("0x" + Integer.toHexString(startcolor));

	red = new JSlider(minvalue, maxvalue, startcolor);
	red.setName("r");
	red.addChangeListener(this);

	green = new JSlider(minvalue, maxvalue, startcolor);
	green.setName("g");
	green.addChangeListener(this);

	blue = new JSlider(minvalue, maxvalue, startcolor);
	blue.setName("b");
	blue.addChangeListener(this);

	color = new JPanel();
	panel.add(color, "span 1 3, grow, h 50!, w 50!");
	color.setBackground(new Color(startcolor / 256, startcolor / 256, startcolor / 256));

	panel.add(new JLabel("Rot"));
	panel.add(redl, "w 50!");
	panel.add(red, "wrap");

	panel.add(new JLabel("Grün"));
	panel.add(greenl, "w 50!");
	panel.add(green, "wrap");

	panel.add(new JLabel("Blau"));
	panel.add(bluel, "w 50!");
	panel.add(blue, "wrap");

	final JCheckBox snap = new JCheckBox("Snap Sliders");
	snap.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		snapmode = snap.isSelected();
		red.setValue(startcolor);
		green.setValue(startcolor);
		blue.setValue(startcolor);
	    }

	});

	panel.add(snap, "span, wrap");

	device = new JLabel("-");
	panel.add(device, "span, wrap");

	this.pack();
	this.setVisible(true);
	this.setTitle("RGB-Ring GUI");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void startUi() {
	Thread devLookup = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    try {
			System.out.println("Checking for Device");
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

    public void setRGBService(RGBService rgb) {
	this.rgb = rgb;
    }

    private void updateRGB() {
	redl.setText("0x" + Integer.toHexString(red.getValue()));
	greenl.setText("0x" + Integer.toHexString(green.getValue()));
	bluel.setText("0x" + Integer.toHexString(blue.getValue()));

	int r = red.getValue();
	int g = green.getValue();
	int b = blue.getValue();

	color.setBackground(new Color(r / 256, g / 256, b / 256));

	// Send it to the device
	if (rgb.isDeviceAvailable()) {
	    rgb.sendRGBData(red.getValue(), green.getValue(), blue.getValue());
	}
    }

    private void updateDevice() {
	if (rgb.isDeviceAvailable()) {
	    device.setText("Device Available");
	} else {
	    String dot = "";
	    counter++;
	    for (int i = 0; i < counter % 4; i++) {
		dot += ". ";
	    }
	    device.setText("Device Unavailable " + dot);
	}
	this.repaint();
	this.validate();
	this.pack();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	if (snapmode) {
	    String name = ((JComponent) e.getSource()).getName();

	    if (name.equals("r")) {
		green.setValue(red.getValue());
		blue.setValue(red.getValue());
	    } else if (name.equals("g")) {
		red.setValue(green.getValue());
		blue.setValue(green.getValue());
	    } else if (name.equals("b")) {
		red.setValue(blue.getValue());
		green.setValue(blue.getValue());
	    }
	}

	updateRGB();
    }

}
