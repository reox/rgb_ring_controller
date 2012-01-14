package at.reox.rgbring.test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CircleTest extends JFrame {
    public CircleTest() {
	this.setVisible(true);
	this.setSize(500, 500);

	JPanel panel = new JPanel(null);

	int r = 100;
	int phi = 90;

	for (int i = 0; i < 10; i++) {
	    JPanel x = new JPanel();
	    x.setBackground(Color.red);
	    x.setBounds((int) (r * Math.cos(phi * (Math.PI / 180))) + 100,
		(int) (r * Math.sin(phi * (Math.PI / 180))) + 100, 4, 4);

	    panel.add(x);
	    phi = (phi + 36) % 360;
	}

	this.setContentPane(panel);
    }

    public static void main(String[] args) {
	CircleTest c = new CircleTest();
    }
}
