package rw.opath2.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 
 * Reads lines of a text file in normal order. 
 *
 */
public class ForwardReader implements LineReader {

	private BufferedReader r;

	public ForwardReader(File file) throws Exception {
		this(file, "ASCII");
	}

	public ForwardReader(File file, String charset) throws Exception {
		r = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
	}

	public String readLine() throws Exception {
		return r.readLine();
	}

	public void close() throws Exception {
		r.close();
	}

}
