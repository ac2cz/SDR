package tutorial8.audio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircularDoubleBufferTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testAdd() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		buffer.add(1.0);
		double d = buffer.read();
		System.out.println(d);
		assertEquals(1.0, d);
	}
	
	@Test
	void testRead() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		Random r = new Random();
		double[] data = new double[1000];
		for (int i=0; i<data.length; i++) {
			data[i] = r.nextDouble();
			buffer.add(data[i]);
			double d = buffer.read();
			assertEquals(data[i], d);
		}
		
	}
	
	@Test
	void testGetCapacity() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		buffer.add(1.0);
		int c = buffer.getCapacity();
		assertEquals(4, c);

		buffer.add(2.0);
		c = buffer.getCapacity();
		assertEquals(3, c);

		buffer.add(3.0);
		c = buffer.getCapacity();
		assertEquals(2, c);

		double d = buffer.read();
		System.out.println(d);
		assertEquals(1.0, d);
		
		c = buffer.getCapacity();
		assertEquals(3, c);
	}
	
	@Test
	void testSize() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		int s = buffer.size();
		int c = buffer.getCapacity();
		assertEquals(0, s);
		assertEquals(5-c, s);
		
		buffer.add(1.0);
		s = buffer.size();
		assertEquals(1, s);
		c = buffer.getCapacity();
		assertEquals(5-c, s);
		
		buffer.add(2.0);
		s = buffer.size();
		assertEquals(2, s);
		c = buffer.getCapacity();
		assertEquals(5-c, s);
		
		buffer.add(3.0);
		s = buffer.size();
		assertEquals(3, s);
		c = buffer.getCapacity();
		assertEquals(5-c, s);
		
		double d = buffer.read();
		s = buffer.size();
		assertEquals(2, s);
		c = buffer.getCapacity();
		assertEquals(5-c, s);
		
	}
	
	@Test
	void testReadFail() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		buffer.add(1.0);
		buffer.add(2.0);
		buffer.add(3.0);
		double d = buffer.read();
		System.out.println(d);
		assertEquals(1.0, d);

		d = buffer.read();
		System.out.println(d);
		assertEquals(2.0, d);

		d = buffer.read();
		System.out.println(d);
		assertEquals(3.0, d);

		try {
			d = buffer.read();
			fail("Failed to detect read issue"); // should never get here.  If we do we fail.
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Read Failed, as expected");
		}
	}
	
	@Test
	void testWriteFail() {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		buffer.add(1.0);
		buffer.add(2.0);
		buffer.add(3.0);
		double d = buffer.read();
		System.out.println(d);
		assertEquals(1.0, d);

		d = buffer.read();
		System.out.println(d);
		assertEquals(2.0, d);

		d = buffer.read();
		System.out.println(d);
		assertEquals(3.0, d);

		try {
			buffer.add(4.0);
			buffer.add(5.0);
			buffer.add(6.0);
			buffer.add(7.0);
			buffer.add(8.0);
			buffer.add(9.0);
			buffer.add(10.0);

			fail("Failed to detect write issue");// should never get here.  If we do we fail.
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Write Failed, as expected");
		}
	}

	@Test
	void testThreads() throws InterruptedException {
		CircularDoubleBuffer buffer = new CircularDoubleBuffer(5);
		Random r = new Random();
//		double[] data = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
		double[] data = new double[100]; 
		for (int i=0; i<100; i++) {
			data[i] = r.nextDouble();
		}
		
		// Now make a thread that writes the data into the buffer and another thread that reads from it
		Thread writeThread = new Thread() {
			  public void run() {
				  for (int i=0; i<data.length; i++) {
						buffer.add(data[i]);
						System.out.println("WROTE:"+i+ ": " + data[i]);
						try { Thread.sleep(10); } catch (InterruptedException e) { System.err.println("stopped");}
						
					}
			  }
			 };
		
		Thread readThread = new Thread() {
			  public void run() {
				  int i = 0;
				  while (i < data.length) {
					  try {
						double d = buffer.read();
						System.out.println("READ:"+i+ ": " + d);
						assertEquals(data[i], d);
						i++;
					  } catch (IndexOutOfBoundsException e) {
						  // wait for data to be available.  
						  try { Thread.sleep(1); } catch (InterruptedException e1) { }
					  }
					}
			  }
			 };
		
		writeThread.start();
		readThread.start();
		writeThread.join();
	}
	
	
	
}
