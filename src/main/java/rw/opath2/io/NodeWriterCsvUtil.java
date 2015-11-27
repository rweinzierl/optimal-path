package rw.opath2.io;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import rw.opath2.standard.NodeWithTimestamp;

public class NodeWriterCsvUtil {

	public static <TNode> void writePath(List<NodeWithTimestamp<TNode>> path, NodeWriterCsv<TNode> wcsv, Writer w)
			throws Exception {
		w.write("point-id,timestamp-millis-since-1970");
		for (String header : wcsv.getHeaders())
			w.write("," + header);
		w.write("\n");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		for (NodeWithTimestamp<TNode> nodeWithTimestamp : path) {
			w.write(wcsv.getId(nodeWithTimestamp.node));
			w.write("," + df.format(new Date((long) nodeWithTimestamp.timestamp)));
			for (String value : wcsv.getValues(nodeWithTimestamp.node))
				w.write("," + value);
			w.write("\n");
		}
	}

	public static <TNode> void writePath(PathReader<TNode> pr, NodeWriterCsv<TNode> wcsv, Writer w) throws Exception {
		w.write("point-id,timestamp-millis-since-1970");
		for (String header : wcsv.getHeaders())
			w.write("," + header);
		w.write(",previous-point-id");
		w.write("\n");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		while (pr.readNode()) {
			w.write(wcsv.getId(pr.currentNode));
			w.write("," + df.format(new Date((long) pr.timestamp)));
			for (String value : wcsv.getValues(pr.currentNode))
				w.write("," + value);
			w.write(",");
			TNode previousNode = pr.getPreviousNode();
			if (previousNode != null)
				w.write(wcsv.getId(previousNode));
			w.write("\n");
		}
	}

}
