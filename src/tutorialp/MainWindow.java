package tutorialp;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import tutorialp.plot.LineChart;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	LineChart lineChart;
	LineChart lineChart2;
	LineChart lineChart3;

	public MainWindow(String title) {
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 600);
		setLayout(new BorderLayout());
		lineChart = new LineChart("Source");
		lineChart2 = new LineChart("FIR");
		lineChart3 = new LineChart("Polyphase");
		add(lineChart, BorderLayout.NORTH);
		add(lineChart2, BorderLayout.CENTER);
		add(lineChart3, BorderLayout.SOUTH);
		lineChart.setPreferredSize(new Dimension(100, 200));
		lineChart3.setPreferredSize(new Dimension(100, 200));
	}

	public void setData(double[] data) {
		lineChart.setData(data);
	}
	public void setData2(double[] data) {
		lineChart2.setData(data);
	}
	public void setData3(double[] data) {
		lineChart3.setData(data);
	}
}
