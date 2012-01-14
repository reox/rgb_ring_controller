package at.reox.rgbring;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class SettingsFrame extends JFrame implements CallBack {

    /**
     * 
     */
    private static final long serialVersionUID = -3891970602245864133L;
    private JPanel panel, circle, setter;
    private CardLayout led;

    private List<SettingsUI> uis;

    SettingsFrame() {
	circle = new JPanel(null);
	panel = new JPanel(new MigLayout("center"));
	setter = new JPanel(led = new CardLayout());

	this.setContentPane(panel);
    }

    public void setLEDFrames(final SettingsUI master, final List<SettingsUI> uis) {
	this.uis = uis;
	panel.add(new JLabel("Master Control"), "split 2");
	final JCheckBox mOn = new JCheckBox("activate");
	((MasterLEDService) (master.getService())).registerCallBack((CallBack) (this));

	mOn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		for (Component c : master.getUI().getComponents()) {
		    c.setEnabled(mOn.isSelected());
		}
	    }
	});

	for (Component c : master.getUI().getComponents()) {
	    c.setEnabled(mOn.isSelected());
	}

	panel.add(mOn, "wrap");
	panel.add(master.getUI(), "span, wrap, center");

	double phi = 15 * (Math.PI / 180);
	int r = 100;
	int pref = 2 * r + 2 * 20;
	circle.setPreferredSize(new Dimension(pref, pref));

	for (final SettingsUI s : uis) {
	    int x = (int) (r * Math.sin(phi) + r - 30 / 2) + 20;
	    int y = (int) (r * Math.cos(phi) + r - 30 / 2) + 20;
	    s.getPreview().setBounds(x, y, 30, 30);
	    phi = (phi + (36 * Math.PI / 180));
	    circle.add(s.getPreview());
	    System.out.println(x + " " + y);
	    setter.add(s.getUI(), s.toString());
	    s.getPreview().addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    led.show(setter, s.toString());
		    for (SettingsUI s : uis) {
			s.getPreview().setBorder(null);
		    }
		    s.getPreview().setBorder(BorderFactory.createLineBorder(Color.red, 1));
		}
	    });
	}

	panel.add(circle, "grow, wrap, center");
	panel.add(setter, "center");
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

    @Override
    public void callMe(RGB data) {

	for (SettingsUI u : uis) {
	    u.setRGB(data);
	}
    }
}
