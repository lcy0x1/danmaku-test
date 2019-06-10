package page;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

import io.Writer;
import main.Printer;
import util.P;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static int x = 0, y = 0;
	public static Rectangle rect = null, crect = null;
	public static Font font = null;

	public static String fontType = "Dialog";
	public static int fontStyle = Font.PLAIN;
	public static final int fontSize = 24;
	public static MainFrame F;

	private static Page mainPanel = null;

	public static void changePanel(Page p) {
		F.FchangePanel(p);
	}

	public static Page getPanel() {
		return mainPanel;
	}

	public static void timer(int t) {
		if (mainPanel != null && !F.changingPanel)
			mainPanel.timer(t);
	}

	protected static void resized() {
		F.Fresized();
	}

	private static void setFonts(int f) {
		List<Object> ks = new ArrayList<>();
		List<Object> fr = new ArrayList<>();
		font = new Font(fontType, fontStyle, f);
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				ks.add(key);
				fr.add(fontRes);
			}
		}
		for (int i = 0; i < ks.size(); i++)
			UIManager.put(ks.get(i), fr.get(i));
	}

	private boolean settingsize = false, changingPanel = false;

	public MainFrame(String ver) {
		super(Page.get(0, "title") + " Ver " + ver);
		setLayout(null);
		addListener();
		sizer();
	}

	public void initialize() {
		F = this;
		changePanel(new MainPage());
		Fresized();
	}

	public void sizer() {
		if (crect == null) {
			P screen = new P(Toolkit.getDefaultToolkit().getScreenSize());
			rect = new P(0, 0).toRectangle(new P(screen.x, screen.y));
			setBounds(rect);
			setVisible(true);
			int nx = rect.width - getRootPane().getWidth();
			int ny = rect.height - getRootPane().getHeight();
			crect = rect;
			if (nx != x || ny != y) {
				x = nx;
				y = ny;
			}
		} else {
			rect = crect;
			setBounds(rect);
			setVisible(true);
		}
	}

	private void addListener() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (mainPanel != null)
					mainPanel.windowActivated();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				if (mainPanel != null)
					mainPanel.exitAll();
				Writer.logClose(true);
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				if (mainPanel != null)
					mainPanel.windowDeactivated();
			}
		});

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == KeyEvent.KEY_PRESSED && mainPanel != null)
					mainPanel.keyPressed((KeyEvent) event);
				if (event.getID() == KeyEvent.KEY_RELEASED && mainPanel != null)
					mainPanel.keyReleased((KeyEvent) event);
				if (event.getID() == KeyEvent.KEY_TYPED && mainPanel != null)
					mainPanel.keyTyped((KeyEvent) event);
			}
		}, AWTEvent.KEY_EVENT_MASK);

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == MouseEvent.MOUSE_PRESSED && mainPanel != null)
					mainPanel.mousePressed((MouseEvent) event);
				if (event.getID() == MouseEvent.MOUSE_RELEASED && mainPanel != null)
					mainPanel.mouseReleased((MouseEvent) event);
				if (event.getID() == MouseEvent.MOUSE_CLICKED && mainPanel != null)
					mainPanel.mouseClicked((MouseEvent) event);
			}
		}, AWTEvent.MOUSE_EVENT_MASK);

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == MouseEvent.MOUSE_MOVED && mainPanel != null)
					mainPanel.mouseMoved((MouseEvent) event);
				if (event.getID() == MouseEvent.MOUSE_DRAGGED && mainPanel != null)
					mainPanel.mouseDragged((MouseEvent) event);
			}
		}, AWTEvent.MOUSE_MOTION_EVENT_MASK);

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == MouseEvent.MOUSE_WHEEL && mainPanel != null)
					mainPanel.mouseWheel((MouseEvent) event);
			}
		}, AWTEvent.MOUSE_WHEEL_EVENT_MASK);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				setCrect();
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				Fresized();
				setCrect();
			}
		});
	}

	private void FchangePanel(Page p) {
		if (p == null)
			return;
		changingPanel = true;
		if (mainPanel != null)
			if (p.getFront() == mainPanel)
				mainPanel.leave();
			else
				mainPanel.exit();
		add(p);
		if (mainPanel != null)
			remove(mainPanel);
		mainPanel = p;
		validate();
		p.renew();
		repaint();
		changingPanel = false;
	}

	private void Fresized() {
		if (settingsize)
			return;
		settingsize = true;
		int w = getRootPane().getWidth();
		int h = getRootPane().getHeight();
		try {
			setFonts(Page.size(w, h, fontSize));
		} catch (ConcurrentModificationException cme) {
			Printer.p("MainFrame", 217, "Failed to set Font");
		}
		if (mainPanel != null)
			mainPanel.componentResized(w, h);
		settingsize = false;
	}

	private void setCrect() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle r = getBounds();
		if ((r.x + r.width) >= 0 && r.y >= 0 && r.x < d.width && (r.y - r.height) < d.height)
			crect = r;
	}
}
