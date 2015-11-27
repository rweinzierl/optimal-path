package rw.opath2.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import rw.opath2.core.EdgeCallback;

/**
 * 
 * Writes nodes to a text file
 * 
 */
public class PathWriter<TNode> implements EdgeCallback<TNode> {

	private NodeExternalizer<TNode> externalizer;
	private Writer w;

	public PathWriter(NodeExternalizer<TNode> externalizer, File outFile) throws Exception {
		this(externalizer, new FileOutputStream(outFile));
	}

	private PathWriter(NodeExternalizer<TNode> externalizer, OutputStream out) throws Exception {
		this.externalizer = externalizer;
		this.w = new OutputStreamWriter(out, "ASCII");
	}

	private void write(TNode node, TNode previousNode, double arrivalTimestamp) throws Exception {
		w.write(externalizer.getId(node));
		w.write(",");
		if (previousNode != null)
			w.write(externalizer.getId(previousNode));
		w.write(",");
		w.write(Double.toString(arrivalTimestamp));
		for (String column : externalizer.toExternalForm(node)) {
			w.write(",");
			w.write(column);
		}
		w.write("\n");
	}

	public void close() throws IOException {
		w.close();
	}

	@Override
	public void edge(TNode start, TNode end, double arrivalTimestamp) throws Exception {
		write(end, start, arrivalTimestamp);
	}

}
