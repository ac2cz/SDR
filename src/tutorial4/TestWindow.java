package tutorial4;

import javax.swing.JFrame;
import tutorial4.plot.LineChart;

@SuppressWarnings("serial")
public class TestWindow extends JFrame {

	LineChart lineChart;

	public TestWindow(String title) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 400);
		lineChart = new LineChart("DSP Results");
		add(lineChart);
	}

	public void setData(double[] data) {
		lineChart.setData(data);
	}
}
