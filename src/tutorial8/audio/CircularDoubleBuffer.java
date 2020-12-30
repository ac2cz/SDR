package tutorial8.audio;

/**
 * A circular double buffer with a write pointer and a read pointer.  
 * The write pointer points to the value that is about to be written.
 * The read pointer points to the value that will be read next. 
 * 
 * @author g0kla@arrl.net
 *
 */
public class CircularDoubleBuffer {
	double[] doubles;
	int writePointer = 0;
	int readPointer = 0;

	public CircularDoubleBuffer(int size) {
		doubles = new double[size];
		System.out.println("Created circular Double buffer with " + doubles.length + " doubles");
	}
	
	private int incPointer(int pointer, int amount) {
		int p = pointer + amount;
		if (p >= doubles.length) {
			// We need to wrap around the array
			p = p % doubles.length;
		}
		return p;
	}
	
	/**
	 * Add data at the write pointer.  This only changes the write pointer and throws an error if it reaches the read pointer
	 * @param d
	 * @return
	 */
	public boolean add(double d) {
		int p = incPointer(writePointer, 1);
		if (p == readPointer)
			throw new IndexOutOfBoundsException("Attempt to Write past the read pointer");
		doubles[writePointer] = d; // we write to the write pointer
		writePointer = p;  // Increment so we are ready to write next time
		return true;
	}
	
	/**
	 * Read a double from the read pointer. It can't equal the write pointer because data has not yet been written there.
	 * The pointer needs to be incremented after the read.
	 * @return
	 */
	public double read() {
		if (readPointer == writePointer)
			throw new IndexOutOfBoundsException("Attempt to read past write pointer");
		double d = doubles[readPointer];
		int p = incPointer(readPointer, 1);
		readPointer = p;
		return d;
	}
}
