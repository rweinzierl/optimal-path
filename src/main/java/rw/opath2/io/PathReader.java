package rw.opath2.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathReader<TNode> {

	private LineReader r;
	protected NodeExternalizer<TNode> externalizer;

	protected String currentNodeId;
	protected TNode currentNode;
	protected String previousNodeId;
	protected double timestamp;

	public PathReader(LineReader r, NodeExternalizer<TNode> externalizer) throws Exception {
		this.r = r;
		this.externalizer = externalizer;
	}

	public PathReader(File file, NodeExternalizer<TNode> externalizer) throws Exception {
		this.r = new ForwardReader(file);
		this.externalizer = externalizer;
	}

	public boolean readNode() throws Exception {
		String line;
		while ("".equals((line = r.readLine())))
			;
		if (line != null) {
			String[] columns0 = line.split(",");
			List<String> columns = new ArrayList<String>(columns0.length);
			for (String column : columns0)
				if (column.length() > 0)
					columns.add(column);
				else
					columns.add(null);
			if (line.endsWith(","))
				columns.add(null);
			Iterator<String> tok = columns.iterator();
			currentNodeId = tok.next();
			this.previousNodeId = tok.next();
			String timestamp0 = tok.next();
			if (timestamp0.length() > 0)
				timestamp = Double.parseDouble(timestamp0);
			int len = columns.size() - 3;
			String[] externalForm = new String[len];
			for (int i = 0; i < len; i++)
				externalForm[i] = tok.next();
			this.currentNode = externalizer.fromExternalForm(currentNodeId, externalForm);
			return true;
		} else {
			this.currentNodeId = null;
			this.currentNode = null;
			this.previousNodeId = null;
			this.timestamp = 0;
			return false;
		}
	}

	public TNode getPreviousNode() {
		if (previousNodeId != null)
			return externalizer.fromExternalForm(previousNodeId, null);
		else
			return null;
	}

	public void close() throws Exception {
		r.close();
	}

}
