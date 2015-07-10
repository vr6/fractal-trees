/*******************************************************************************
 * Copyright (c) 2001 Venkat Reddy
 *******************************************************************************/
package vrp.draw;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class FractalTree extends Applet implements ItemListener,
		ActionListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	MainPanel mainPanel;

	@Override
	public void init() {
		setLayout(new BorderLayout());
		add("Center", this.mainPanel = new MainPanel(this));
		setSize(this.mainPanel.getSize());
	}

	@Override
	public void destroy() {
		remove(this.mainPanel);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		final String arg = ((Component) e.getItemSelectable()).getName();
		if ("Depth".equals(arg)) {
			this.mainPanel.pTree.setDepth((String) e.getItem());
			this.mainPanel.pTree.reset();
		} else if ("Branches".equals(arg)) {
			this.mainPanel.pTree.setBranches((String) e.getItem());
			this.mainPanel.pTree.reset();
		} else if ("Line".equals(arg)) {
			this.mainPanel.pTree.setLineType((String) e.getItem());
			this.mainPanel.pTree.repaint();
		} else if ("Spokes".equals(arg)) {
			this.mainPanel.pTree.setSpokes((String) e.getItem());
			this.mainPanel.pTree.repaint();
		} else if ("showCP".equals(arg)) {
			this.mainPanel.pTree.setShowCP(e.getStateChange());
			this.mainPanel.pTree.repaint();
		} else if ("Ratio".equals(arg)) {
			this.mainPanel.pTree.setRatio((String) e.getItem());
			this.mainPanel.pTree.repaint();
		} else if ("Zoom".equals(arg)) {
			this.mainPanel.pTree.setZoom((String) e.getItem());
			this.mainPanel.pTree.repaint();
		} else if ("Angle".equals(arg)) {
			this.mainPanel.pTree.setAngle((String) e.getItem());
			this.mainPanel.pTree.repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String action = e.getActionCommand();
		if (action.equals("SetValues")) {
			this.mainPanel.pTree.setValues();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.mainPanel.pTree.doDrag(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mainPanel.pTree.stopDrag();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mainPanel.pTree.setCP(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public static void main(String args[]) {
		AppletFrame.startApplet("vrp.draw.FractalTree", "Fractal Tree", args);
	}

	@Override
	public String getAppletInfo() {
		return "Fractal Tree by Venkat Reddy";
	}
}
