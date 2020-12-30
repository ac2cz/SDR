package tutorial8.signal;

/**
 * This is a circular buffer of doubles, suitable for storing signals in a DSP processing stream.
 * 
 * We use two pointers to remember where we are in the array.
 * At any point in time there is a virtual array that starts from the readPointer and ends at the writePointer.
 * The writePointer is the position of the last entry.  It is incremented when a new value is added.  This purges the last entry in the array
 * The readPointer is the beginning of the virtual array.  It is always "behind" the writePointer.  This is the position
 * that we read from if we read element [0].  We purge data from the array by increasing the readPointer.  It can not go past the write pointer.
 * 
 * In use, we ignore the virtual array and the complexity of the implementation.  We simply write data using add() and read data using get()
 * 
 * @author chris.e.thompson g0kla/ac2cz
 *
 */
public class CircularDoubleBuffer {
	double[] doubles;
	int bufferSize = 0;; 
	int readPointer = 0;
	int writePointer = 0;
	
	public CircularDoubleBuffer(int size) {
		doubles = new double[size];
		bufferSize = size;
//		readPointer = bufferSize - 1; // initialize this to the end of the array, otherwise we can not write data to it
		System.out.println("Created circular Double buffer with " + bufferSize + " bytes");
	}

	public int getReadPointer() { return readPointer; }
	public int getWritePointer() { return writePointer; }
	
	/**
	 * How many bytes can we add without the end pointer reaching the start pointer
	 * @return
	 */
	public int getCapacity() {
		if (readPointer == 0 && writePointer == 0) {
			return bufferSize;  // Special case when we have not added any data at all
		}
		int size = 0;
		if (writePointer > readPointer)  // Then we have the distance to the end of the array plus the start pointer
			size = bufferSize - writePointer + readPointer;
		else {  // writePointer < readPointer 
			// we only have the distance from the end pointer to the start pointer
			size = readPointer - writePointer;
		}
		return size;	
	}

	/**
	 * Add two doubles to the buffer, such as IQ samples
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean add(double one, double two) {
		if (writePointer+1 == readPointer) {
			throw new IndexOutOfBoundsException("Write pointer has reached the read pointer");
		} 
		if (writePointer+2 == readPointer) {
			throw new IndexOutOfBoundsException("Write pointer one byte from read pointer when writing two doubles");
		} 
		add(one);
		add(two);
		return true;
	}
	
	/**
	 * Add data from the end pointer.  This only changes the end pointer and throws and error if it reaches the start pointer
	 * @param o
	 * @return
	 */
	public boolean add(double o) {
			doubles[writePointer] = o;
			writePointer++;
			if (writePointer == bufferSize)
				writePointer = 0;

			if (writePointer == readPointer) { // then we have caught up with it
				writePointer--;
				if (writePointer == -1)
					writePointer = bufferSize-1;
				throw new IndexOutOfBoundsException("Write pointer has reached read pointer");
			}
		return true;
	}
	
	private int incPointer(int pointer, int amount) {
		int p = pointer + amount;
		if (p >= bufferSize) {
			// We need to wrap around the array
			p = p % bufferSize;
		}
		return p;
	}
	
	/**
	 * This returns the ith element of the virtual array starting from the readPointer.
	 * Typically the readPointer is then incremented if the data is "consumed"
	 * An error is generated if it will go past the end pointer
	 */
	public double get(int i) {
		if (i > size())
			throw new IndexOutOfBoundsException("Attempt to read past the write pointer");
		int p = incPointer(readPointer, i);
		return doubles[p];
	}
		
	/** 
	 * Set the start position to a new point.  
	 */
	public void incReadPointer(int amount) {
		// snapshot the value to avoid failing the check due to a race condition
		int e = writePointer;
		if (e > readPointer ) {
			// then the readPointer needs to remain less than the end pointer after the increment
			if (readPointer + amount >= e)
				throw new IndexOutOfBoundsException("Attempt to move read pointer " + readPointer + " past write pointer " + e);
		} else {
			// if it wraps then it needs to stay less, otherwise we are fine
			if (readPointer + amount >= bufferSize) {
				int testPointer = incPointer(readPointer, amount);
				if (testPointer >= writePointer)
					throw new IndexOutOfBoundsException("Attempt to wrap read pointer " + readPointer + " past write pointer " + e);
			}
		}
		readPointer = incPointer(readPointer, amount);
	}
	
	/**
	 * This returns the size of the virtual array.
	 */
	public int size() {
		int size = 0;
		int e = writePointer; // snapshot the end pointer to avoid a race condition in the checks below.  The size can only grow if the end pointer moves, so this is safe
		if (e >= readPointer)
			size = e - readPointer;
		else {
			size = bufferSize - readPointer; // distance from start to end of the real array
			size = size + e;  //  add the distance from the start to the write pointer
		}
		return size;	
	}
}
