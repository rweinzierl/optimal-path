package rw.opath2.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 
 * Reads lines of a text file in backward order. 
 *
 */
public class BackReader implements LineReader {

	private BufferedReader r;

	public BackReader(File file, String charset) throws Exception {
		r = new BufferedReader(new InputStreamReader(new BackStream(file), charset));
	}

	public BackReader(File file) throws Exception {
		this(file, "ASCII");
	}

	public String readLine() throws Exception {
		String line = r.readLine();
		if (line != null) {
			int len = line.length();
			char[] chars = new char[len];
			for (int i = 0; i < len; i++)
				chars[i] = line.charAt(len - i - 1);
			return new String(chars);
		} else
			return null;
	}

	public void close() throws Exception {
		r.close();
	}

}
