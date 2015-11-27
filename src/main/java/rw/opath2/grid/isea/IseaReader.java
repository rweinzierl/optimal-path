package rw.opath2.grid.isea;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

/*
 * http://webpages.sou.edu/~sahrk/dgg/isea/gen/isea3h.html 
 */

public class IseaReader {

	public static List<NodeIsea> getGridCells(int resolution) throws Exception {
		String name = "isea3h" + resolution + ".csv.gz";
		return fromCsv(new GZIPInputStream(new FileInputStream(new File(getIseaDir(), name))));
	}

	private static String getIseaDir() {
		String iseaDir = System.getProperty("isea.dir");
		System.out.println("Using isea dir: " + iseaDir);
		return iseaDir;
	}

	private static List<NodeIsea> fromCsv(InputStream in) throws Exception {
		TreeMap<Integer, NodeIsea> cellByNumber = new TreeMap<Integer, NodeIsea>();
		TreeMap<Integer, List<Integer>> neigboursByNumber = new TreeMap<Integer, List<Integer>>();
		int len;
		byte[] buf = new byte[8192];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
		for (String line : new String(out.toByteArray(), "ASCII").split("\n")) {
			String[] values = line.split(",");
			NodeIsea gridCell = new NodeIsea();
			int number = Integer.parseInt(values[0]);
			gridCell.number = number;
			gridCell.lng = Double.parseDouble(values[1]);
			gridCell.lat = Double.parseDouble(values[2]);
			cellByNumber.put(number, gridCell);
			List<Integer> neighbours = new ArrayList<Integer>();
			for (int i = 3; i < values.length; i++)
				neighbours.add(Integer.parseInt(values[i]));
			neigboursByNumber.put(number, neighbours);
		}
		for (Map.Entry<Integer, NodeIsea> entry : cellByNumber.entrySet()) {
			NodeIsea gridCell = entry.getValue();
			List<Integer> neighbours = neigboursByNumber.get(entry.getKey());
			for (int i = 0; i < neighbours.size(); i++) 
				gridCell.neighbours.add(cellByNumber.get(neighbours.get(i)));
		}
		return new ArrayList<NodeIsea>(cellByNumber.values());
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("isea.dir", "/data/eclipse-workspace/movebank/src/main/java/org/movebank/analysis/wind");
		List<NodeIsea> cells = getGridCells(2);
		System.out.println(cells.size());
	}

}
