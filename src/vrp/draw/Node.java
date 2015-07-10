/*******************************************************************************
 * Copyright (c) 2001 Venkat Reddy
 *******************************************************************************/
package vrp.draw;

public class Node {
	public static int d = 10;
	public double x;
	public double y;
	public Node[] brc = null;

	Node(double x, double y) {
		this.x = x;
		this.y = y;
	}

	Node(Node t) {
		this.x = t.x;
		this.y = t.y;
		this.brc = t.brc;
	}

	public Node rotate(Node n, double theta) {
		return new Node((n.x + ((this.x - n.x) * Math.cos(theta)))
				- ((this.y - n.y) * Math.sin(theta)), n.y
				+ ((this.x - n.x) * Math.sin(theta))
				+ ((this.y - n.y) * Math.cos(theta)));
	}

	public boolean isNear(Node t) {
		return (Math.abs(this.x - t.x) < d) && (Math.abs(this.y - t.y) < d);
	}

	public void moveTo(Node t) {
		this.x = t.x;
		this.y = t.y;
	}

	public double distance(Node t) {
		return Math.sqrt(((this.x - t.x) * (this.x - t.x))
				+ ((this.y - t.y) * (this.y - t.y)));
	}

	public double angle(Node t) {
		double a = Math.atan((this.y - t.y) / sz(this.x - t.x));
		if (t.x > this.x)
			a += Math.PI;
		return a;
	}

	public Node locate(double r, double theta) {
		final double tx = this.x + (r * Math.cos(theta));
		final double ty = this.y + (r * Math.sin(theta));
		return new Node(tx, ty);
	}

	public double sz(double d) {
		return d == 0 ? 0.000000001 : d;
	}

	@Override
	public String toString() {
		String tmp = "x=" + this.x + ";" + "y=" + this.y + ";";
		tmp += this.brc == null ? "brc=null" : "brc.length=" + this.brc.length;
		return tmp;
	}

}
