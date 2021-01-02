package tutorialx.audio;

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
	
	/**
	 * This returns the capacity we have to save more data.  It is the distance from the write pointer
	 * to the read pointer.
	 * @return
	 */
	public int getCapacity() { // how many bytes can we add without the end pointer reaching the start pointer
		return doubles.length-size();	
	}
	
	/**
	 * This returns the size of the virtual array.  This tells us how much data we have available to read or how much data
	 * is stored
	 * @return
	 */
	public int size() {
		int size = 0;
		int e = writePointer; // snapshot the end pointer to avoid a race condition in the checks below.  The size can only grow if the write pointer moves, so this is safe
		if (e >= readPointer)
			size = e - readPointer;
		else {
			size = doubles.length - readPointer; // distance from start to end of the real array
			size = size + e;  //  add the distance from the start to the write pointer
		}
		return size;	
	}
}
