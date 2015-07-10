/*******************************************************************************
 * Copyright (c) 2001 Venkat Reddy
 *******************************************************************************/
package vrp.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.geom.QuadCurve2D;

public class TreePanel extends Panel {
	private static final long serialVersionUID = 1L;
	public int gen = 3;
	private int num = 2;
	private int spokes = 3;
	private int showCP = ItemEvent.SELECTED;
	private double ratio = 0.5;
	private double zoom = 100.0;
	private double angle = 0.5;
	public TextField ratioFld;
	public TextField zoomFld;
	public TextField angleFld;

	public double a = 0.5;
	public double t = 0.3;
	private final double rx = 200, ry = 200;
	public Node root = new Node(this.rx, this.ry);
	public Node fork = new Node(this.rx + this.zoom, this.ry);
	public Node bough = new Node(this.rx + ((1 - this.ratio) * this.zoom),
			this.ry).rotate(this.fork, this.angle * Math.PI);
	public Node cp = new Node(this.root);
	private final int sy = 600;
	private final int sx = 0;
	public int drag = 0;
	private final Color[] clr = new Color[9];
	private int curve = 0;
	private final QuadCurve2D.Double quad = new QuadCurve2D.Double();
	private boolean settingValues = false;

	TreePanel() {
		super();
		this.clr[0] = new Color(0, 0, 0);
		this.clr[1] = new Color(0, 0, 255);
		;
		this.clr[2] = Color.magenta;
		this.clr[3] = new Color(255, 0, 0);
		this.clr[4] = new Color(0, 128, 0);
		this.clr[5] = new Color(255, 0, 0);
		this.clr[6] = new Color(0, 128, 255);
		this.clr[7] = new Color(0, 0, 255);
		this.clr[8] = new Color(128, 0, 128);
	}

	public double sz(double d) {
		return d == 0 ? 0.0000001 : d;
	}

	public double sz(int i) {
		return sz((double) i);
	}

	public void reset() {
		final double t1 = this.fork.angle(this.root);
		double t2 = this.bough.angle(this.fork);
		final double r1 = this.root.distance(this.fork);
		final double r2 = this.fork.distance(this.bough);
		this.a = r2 / sz(r1);
		this.t = Math.abs(t2 - t1);

		if (this.settingValues) {
			this.fork.moveTo(new Node(this.root.x + (this.zoom * Math.cos(t1)),
					this.root.y + (this.zoom * Math.sin(t1))));
			this.t = this.angle * Math.PI;
			t2 = this.t + t1;
			this.bough.moveTo(new Node(this.fork.x
					+ (this.ratio * this.zoom * Math.cos(t2)), this.fork.y
					+ (this.ratio * this.zoom * Math.sin(t2))));
			this.a = this.ratio;
		} else {
			this.angle = this.t;
			this.zoom = r1;
			this.ratio = this.a;

			this.ratioFld.setText("" + this.ratio);
			this.zoomFld.setText("" + this.zoom);
			this.angleFld.setText("" + (this.angle / Math.PI));
		}

		buildTree(this.fork, r1, t1, 1);
		repaint();
	}

	public void setValues() {
		this.ratio = Double.parseDouble(this.ratioFld.getText());
		this.zoom = Double.parseDouble(this.zoomFld.getText());
		this.angle = Double.parseDouble(this.angleFld.getText());
		this.settingValues = true;
		reset();
		this.settingValues = false;
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		double k = 0;
		if (this.curve != 0)
			k = 0.5;
		for (int i = 0; i < this.spokes; i++) {
			drawTree(g2d, this.root, this.fork, 0, (i * 2 * Math.PI)
					/ this.spokes);
			g2d.setPaint(Color.black);
			final Node t = this.fork.rotate(this.root, (i * 2 * Math.PI)
					/ this.spokes);
			if (this.curve == 0) {
				g2d.drawLine(sx(this.root.x), sy(this.root.y), sx(t.x), sy(t.y));
			} else {
				final Node t1 = this.fork.rotate(this.root,
						((i - 1) * 2 * Math.PI) / this.spokes);
				this.quad.setCurve(sx((t1.x - (k * (t1.x - this.root.x)))),
						sy((t1.y - (k * (t1.y - this.root.y)))),
						sx(this.root.x), sy(this.root.y),
						sx((t.x - (k * (t.x - this.root.x)))),
						sy((t.y - (k * (t.y - this.root.y)))));
				(g2d).draw(this.quad);
			}
		}
		final int d = Node.d;
		if (this.showCP == ItemEvent.SELECTED) {
			g2d.setPaint(Color.black);
			g.fillOval((this.sx + (int) this.root.x) - (d / 2), this.sy
					- (int) this.root.y - (d / 2), d, d);
			g2d.setPaint(Color.blue);
			g.fillOval((this.sx + (int) this.fork.x) - (d / 2), this.sy
					- (int) this.fork.y - (d / 2), d, d);
			g2d.setPaint(Color.yellow);
			g.fillOval((this.sx + (int) this.bough.x) - (d / 2), this.sy
					- (int) this.bough.y - (d / 2), d, d);
		}
	}

	private void buildTree(Node rn, double r, double tr, int g) {
		if (++g > this.gen)
			return;
		rn.brc = new Node[this.num];
		for (int i = 0; i < this.num; i++) {
			final double ti = (tr + this.t)
					- ((i * 2 * this.t) / (this.num - 1));
			rn.brc[i] = rn.locate(r * this.a, ti);
			buildTree(rn.brc[i], r * this.a, ti, g);
		}
	}

	public void trace(String str) {
		System.out.println(str);
	}

	private void drawTree(Graphics2D g2d, Node rn, Node node, int gn, double ang) {
		if (node.brc == null)
			return;
		final Node n = node.rotate(this.root, ang);
		final Node r = rn.rotate(this.root, ang);
		for (int i = 0; i < this.num; i++) {
			g2d.setPaint(this.clr[gn]);

			final Node b = node.brc[i].rotate(this.root, ang);
			if (this.curve != 1) { // 0 = line, 1 = curve, 2 = both
				g2d.drawLine(sx(n.x), sy(n.y), sx(b.x), sy(b.y));
			}
			if (this.curve != 0) {
				this.quad.setCurve(sx((n.x - ((n.x - r.x) / 2))),
						sy((n.y - ((n.y - r.y) / 2))), sx(n.x), sy(n.y),
						sx((n.x - ((n.x - b.x) / 2))),
						sy((n.y - ((n.y - b.y) / 2))));
				(g2d).draw(this.quad);
			}
		}
		++gn;
		for (int i = 0; i < this.num; i++) {
			drawTree(g2d, node, node.brc[i], gn, ang);
		}
	}

	public int sx(double x) {
		return this.sx + (int) x;
	}

	public int sy(double y) {
		return this.sy - (int) y;
	}

	public double gx(int x) {
		return x - this.sx;
	}

	public double gy(int y) {
		return this.sy - y;
	}

	public void setLineType(String type) {
		if ("Straight".equals(type)) {
			this.curve = 0;
		} else if ("Curve".equals(type)) {
			this.curve = 1;
		}
	}

	public void setCP(int x, int y) {
		this.cp = new Node(gx(x), gy(y));
	}

	public void setDepth(String gen) {
		this.gen = Integer.parseInt(gen);
	}

	public void setBranches(String b) {
		this.num = Integer.parseInt(b);
	}

	public void setSpokes(String v) {
		this.spokes = Integer.parseInt(v);
	}

	public void setShowCP(int v) {
		this.showCP = v;
		trace("" + this.showCP);
	}

	public void setRatio(String v) {
		this.ratio = Double.parseDouble(v);
	}

	public void setZoom(String v) {
		this.zoom = Double.parseDouble(v);
	}

	public void setAngle(String v) {
		this.angle = Double.parseDouble(v);
	}

	public void doDrag(int dx, int dy) {
		final Node dp = new Node(gx(dx), gy(dy));
		switch (this.drag) {
		case 0:
			if (this.root.isNear(dp)) {
				translate(new Node(dp.x - this.root.x, dp.y - this.root.y));
				this.drag = 1;
			} else if (this.fork.isNear(dp)) {
				moveFork(dp);
				this.drag = 2;
			} else if (this.bough.isNear(dp)) {
				this.bough.moveTo(dp);
				this.drag = 3;
			} else {

			}
			break;
		case 1:
			translate(new Node(dp.x - this.root.x, dp.y - this.root.y));
			break;
		case 2:
			moveFork(dp);
			break;
		case 3:
			this.bough.moveTo(dp);
			break;
		}

		if (this.drag > 0) {
			reset();
		}
	}

	private void moveFork(Node dp) {
		final double rt = this.fork.distance(this.root)
				/ this.bough.distance(this.fork);
		final double rt2 = dp.distance(this.root) / rt;
		final double angb = this.bough.angle(this.fork);
		Node nb = new Node(dp.x + (rt2 * Math.cos(angb)), dp.y
				+ (rt2 * Math.sin(angb)));
		nb = nb.rotate(dp, dp.angle(this.root) - this.fork.angle(this.root));
		this.bough.moveTo(nb);
		this.fork.moveTo(dp);
	}

	public void stopDrag() {
		this.drag = 0;
	}

	public void translate(Node to) {
		this.root.x += to.x;
		this.root.y += to.y;
		this.fork.x += to.x;
		this.fork.y += to.y;
		this.bough.x += to.x;
		this.bough.y += to.y;
		reset();
	}

	public void rotate(double theta) {
		this.fork.x = (this.root.x + ((this.fork.x - this.root.x) * Math
				.cos(theta))) - ((this.fork.y - this.root.y) * Math.sin(theta));
		this.fork.y = this.root.y
				+ ((this.fork.x - this.root.x) * Math.sin(theta))
				+ ((this.fork.y - this.root.y) * Math.cos(theta));
		this.bough.x = (this.root.x + ((this.bough.x - this.root.x) * Math
				.cos(theta)))
				- ((this.bough.y - this.root.y) * Math.sin(theta));
		this.bough.y = this.root.y
				+ ((this.bough.x - this.root.x) * Math.sin(theta))
				+ ((this.bough.y - this.root.y) * Math.cos(theta));
		reset();
	}
}
