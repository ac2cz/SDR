package tutorial6;

import javax.swing.JFrame;

import tutorial5.plot.LineChart;
import tutorial5.signal.Window;

public class WindowTest {
	
	public static void main(String[] args) {
	int len = 1025;
	double[] blackmanWin = Window.initBlackmanWindow(len);
	for (double b : blackmanWin)
		System.out.println(b);
	
	JFrame jf = new JFrame();
	LineChart lc = new LineChart("Test");
	jf.add(lc);
	jf.setBounds(500,200,500,300);
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	lc.setData(blackmanWin);
	jf.setVisible(true);
	}
	
	
}
