package at.reox.rgbring;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

public class SettingsUI implements ChangeListener {

    private JPanel panel;
    private JPanel preview;
    private JSlider red, green, blue;
    private JLabel redl, greenl, bluel;

    private LEDService rgb;

    private JPanel color;
    private JCheckBox snap;

    private Color[] clickableColors = { new Color(100, 100, 100), new Color(200, 200, 200),
	Color.black, Color.red, Color.blue, Color.green, Color.cyan, Color.magenta, Color.yellow };

    private boolean snapmode = false;

    public SettingsUI() {
	panel = new JPanel(new MigLayout());
	preview = new JPanel(new MigLayout());

	int maxvalue = 4095;
	int minvalue = 0;

	final int startcolor = 0x100;

	redl = new JLabel(startcolor + "");
	greenl = new JLabel(startcolor + "");
	bluel = new JLabel(startcolor + "");

	snap = new JCheckBox("Snap Sliders");

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
	panel.add(color, "span 1 3, grow, h 30!, w 30!");
	preview.add(color, "grow, h 30!, w 30!");
	color.setBackground(new Color(startcolor >> 4, startcolor >> 4, startcolor >> 4));

	panel.add(new JLabel("Rot"));
	panel.add(redl, "w 50!");
	panel.add(red, "wrap");

	panel.add(new JLabel("Grün"));
	panel.add(greenl, "w 50!");
	panel.add(green, "wrap");

	panel.add(new JLabel("Blau"));
	panel.add(bluel, "w 50!");
	panel.add(blue, "wrap");

	snap.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		snapmode = snap.isSelected();
		red.setValue(startcolor);
		green.setValue(startcolor);
		blue.setValue(startcolor);
	    }

	});

	panel.add(snap, "span");
    }

    public void setLEDService(LEDService rgb) {
	this.rgb = rgb;
    }

    private void updateRGB() {
	redl.setText("0x" + Integer.toHexString(red.getValue()));
	greenl.setText("0x" + Integer.toHexString(green.getValue()));
	bluel.setText("0x" + Integer.toHexString(blue.getValue()));

	int r = red.getValue();
	int g = green.getValue();
	int b = blue.getValue();

	// The Color in Java is 8 Bit...
	color.setBackground(new Color((r >> 4) % 256, (g >> 4) % 256, (b >> 4) % 256));

	rgb.sendRGB(red.getValue(), green.getValue(), blue.getValue());
    }

    private void updateRGB(Color c) {
	red.setValue(c.getRed() << 4);
	green.setValue(c.getGreen() << 4);
	blue.setValue(c.getBlue() << 4);

	updateRGB();
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

    public JComponent getUI() {
	return panel;
    }

    public JComponent getPreview() {
	return preview;
    }

    public void setRGB(RGB data) {
	red.setValue(data.red);
	green.setValue(data.green);
	blue.setValue(data.blue);

	updateRGB();
    }

    public LEDService getService() {
	return rgb;
    }

    public void showPanel() {

	JPanel clickColors = new JPanel(new MigLayout());
	for (final Color c : clickableColors) {
	    final JPanel tcolor = new JPanel();
	    tcolor.setBackground(c);
	    tcolor.addMouseListener(new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		    if (tcolor.isEnabled()) {
			snap.setSelected(false);
			snapmode = false;
			updateRGB(c);
		    }
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	    });
	    clickColors.add(tcolor, "h 30!, w 30!");
	}
	panel.add(clickColors, "span, wrap");

    }
}
