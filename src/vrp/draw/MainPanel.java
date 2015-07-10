/*******************************************************************************
 * Copyright (c) 2001 Venkat Reddy
 *******************************************************************************/
package vrp.draw;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

public class MainPanel extends Panel {
	private static final long serialVersionUID = 1L;
	public TreePanel pTree;

	MainPanel(EventListener listener) {
		final ItemListener il = (ItemListener) listener;
		final MouseMotionListener mml = (MouseMotionListener) listener;
		final MouseListener ml = (MouseListener) listener;

		setLayout(new BorderLayout());

		add("Center", this.pTree = new TreePanel());
		this.pTree.addMouseMotionListener(mml);
		this.pTree.addMouseListener(ml);
		this.pTree.setBackground(Color.white);
		this.pTree.setSize(600, 600);

		final Panel p = new Panel();

		final Checkbox cb = new Checkbox("", true);
		cb.setName("showCP");
		cb.addItemListener(il);
		p.add(cb);

		Choice c = new Choice();
		c.setName("Spokes");
		c.addItemListener(il);
		for (int i = 1; i < 10; i++) {
			c.addItem("" + i);
		}
		c.select(2);
		p.add(c);

		c = new Choice();
		c.setName("Depth");
		c.addItemListener(il);
		for (int i = 1; i < 10; i++) {
			c.addItem("" + i);
		}
		c.select(2);
		p.add(c);

		c = new Choice();
		c.setName("Branches");
		c.addItemListener(il);
		for (int i = 2; i < 10; i++) {
			c.addItem("" + i);
		}
		c.select(0);
		p.add(c);

		c = new Choice();
		c.setName("Line");
		c.addItemListener(il);
		c.addItem("Straight");
		c.addItem("Curve");
		c.select(0);
		p.add(c);

		TextField t = new TextField(10);
		t.setName("Ratio");
		p.add(t);
		this.pTree.ratioFld = t;

		t = new TextField(10);
		t.setName("Zoom");
		p.add(t);
		this.pTree.zoomFld = t;

		t = new TextField(10);
		t.setName("Angle");
		p.add(t);
		this.pTree.angleFld = t;

		final Button b = new Button("Set");
		b.setActionCommand("SetValues");
		b.addActionListener((ActionListener) il);
		p.add(b);

		p.setBackground(Color.lightGray);

		add("North", p);
		setSize(600, 660);
		this.pTree.reset();
	}
}
