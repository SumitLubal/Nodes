package FileShare;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class WindowProperty {
	static GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
	static GraphicsDevice vc = ge.getDefaultScreenDevice();

	public static Rectangle getResolution() {
		DisplayMode dc = vc.getDisplayMode();
		return new Rectangle(dc.getWidth(),dc.getHeight());
	}

	public static int getHeight() {
		Rectangle r = getResolution();
		return r.height;
	}

	public static int getWidth() {
		Rectangle r = getResolution();
		return r.width;
	}

	public static int getMidX() {
		return getWidth() / 2;
	}

	public static int getMidY() {
		return getHeight() / 2;
	}
}
