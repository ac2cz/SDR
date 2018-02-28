package tutorial2;

import javax.swing.JFrame;
import tutorial2.plot.LineChart;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	LineChart lineChart;

	public MainWindow(String title) {
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
