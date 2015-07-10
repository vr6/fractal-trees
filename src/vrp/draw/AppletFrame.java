/*******************************************************************************
 * Copyright (c) 2001 Venkat Reddy
 *******************************************************************************/
package vrp.draw;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppletFrame extends Frame {
	private static final long serialVersionUID = 1L;

	public static void startApplet(String className, String title,
			String args[]) {
		Applet a;
		Dimension appletSize;
		try {
			a = (Applet) Class.forName(className).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}
		a.init();
		a.start();
		final AppletFrame f = new AppletFrame(title);
		f.add("Center", a);
		appletSize = a.getSize();
		f.pack();
		f.setSize(appletSize);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.show();
	}

	public AppletFrame(String name) {
		super(name);
	}

	@Override
	public void processEvent(AWTEvent e) {
		if (e.getID() == Event.WINDOW_DESTROY) {
			System.exit(0);
		}
	}
}

