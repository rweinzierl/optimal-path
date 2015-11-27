package rw.opath2.io;

public interface LineReader {
	
	public String readLine() throws Exception;

	public void close() throws Exception;
	
}
