package rw.opath2.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 
 * Reads bytes of a file in backward order.
 *
 */
public class BackStream extends InputStream {

	private final RandomAccessFile raf;
	private byte[] buf;

	private long bufStart;

	private long readPointer;

	public BackStream(File file) throws IOException {
		this(file, 8096);
	}

	public BackStream(File file, int bufSize) throws IOException {
		raf = new RandomAccessFile(file, "r");
		readPointer = raf.length();
		buf = new byte[bufSize];
		bufStart = readPointer;
	}

	@Override
	public int read() throws IOException {
		if (readPointer <= 0)
			return -1;
		else {
			if (readPointer <= bufStart) {
				int len = Math.min(buf.length, (int) bufStart);
				bufStart = bufStart - len;
				raf.seek(bufStart);
				raf.readFully(buf, 0, len);
			}
			--readPointer;
			int pos = (int) (readPointer - bufStart);
			return buf[pos] & 0xff;
		}
	}

	@Override
	public void close() throws IOException {
		raf.close();
	}

}
