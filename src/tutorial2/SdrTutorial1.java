package tutorial2;
import org.jtransforms.fft.DoubleFFT_1D;
import tutorial2.MainWindow;
import tutorial1.signal.Oscillator;

public class SdrTutorial1 {

	public static void main(String[] args) throws InterruptedException {

		Oscillator osc = new Oscillator(192000, 70000);
		MainWindow window = new MainWindow("Test");
		
		int len = 1024;
		double v = 0.5;
		
		double[] buffer = new double[len];
		double[] psd = new double[len/2+1];
		for (int n=0; n< len; n++) {
			double value = osc.nextSample();
			buffer[n] = value;
		}
		
		DoubleFFT_1D fft;
		fft = new DoubleFFT_1D(len);
		fft.realForward(buffer);
		System.out.println(buffer[0]); // bin 0
		psd[0] = buffer[0];
		for (int k=1; k<len/2; k++) {
			System.out.println(Math.sqrt(buffer[2*k]*buffer[2*k] + buffer[2*k+1]*buffer[2*k+1]));
			psd[k] = Math.sqrt(buffer[2*k]*buffer[2*k] + buffer[2*k+1]*buffer[2*k+1]);
		}
		System.out.println(Math.sqrt(buffer[1]*buffer[1] + buffer[len/2]*buffer[len/2])); // bin n/2
		psd[len/2] = Math.sqrt(buffer[1]*buffer[1] + buffer[len/2]*buffer[len/2]);
		if (buffer != null) {
			window.setData(psd);
			window.setVisible(true); // causes window to be redrawn
		} 

	}
}
