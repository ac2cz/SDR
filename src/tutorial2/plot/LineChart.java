package tutorial2.plot;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LineChart extends JPanel {

	double[] data;
	public static final int BORDER = 30;
	private final int LABEL_WIDTH = 30;

	public LineChart(String chartTitle) {

	}
	public LineChart(String chartTitle, double[] data) {
		this.data = data;
	}

	public void setData(double[] data) {
		this.data = data;
		repaint();
	}

	public void paintComponent(Graphics gr) {
		super.paintComponent( gr ); // call superclass's paintComponent  
		Graphics2D g2 = (Graphics2D) gr;

		double maxValue = -99999;
		double minValue = 99999;
		for (int n=0; n < data.length; n++) {
			if (data[n] > maxValue) maxValue = data[n];
			if (data[n] < minValue) minValue = data[n];
		}

		gr.drawLine(BORDER, getHeight()-BORDER, BORDER, BORDER);

		// The zero point is the middle of the JPanel.  We have aqn equal BORDER top and bottom.
		int zeroPoint = getHeight()/2;
		gr.drawLine(BORDER, zeroPoint, getWidth()-BORDER, zeroPoint);

		int step = data.length / 16; // plot 16 labels
		int lastx = BORDER, lasty = zeroPoint;

		for (int n=0; n < data.length; n++) {
			// We have values from -1 to 1.  We want to scale them so that +1 is the top of the 
			// JPanel and -1 is the bottom.  The top is at x position BORDER.  The bottom is at 
			// X position getHeight()-BORDER
			int y = getRatioPosition(minValue, maxValue, data[n], getHeight()-BORDER*2);
			int x = getRatioPosition(data.length,0,n,getWidth()-BORDER*2);
			x = x + BORDER;
			//Ellipse2D.Double circle = new Ellipse2D.Double(x+BORDER-2, y+BORDER-2, 4, 4);
			//g2.fill(circle);
			gr.drawLine(lastx, lasty, x, y);
			lastx = x;
			lasty = y;
			if (n % step == 0)
				g2.drawString(""+n, x, zeroPoint+15);
		}
	}

	/**
	 * Given a dimension in pixesls and a min and max value, calculate the 
	 * pixel position for a value
	 * @param min
	 * @param max
	 * @param value
	 * @param dimension
	 * @return
	 */
	public static int getRatioPosition(double min, double max, double value, int dimension) {
		if (max == min) return 0;
		double ratio = (max - value) / (max - min);
		int position = (int)Math.round(dimension * ratio);
		return position;
	}
}
