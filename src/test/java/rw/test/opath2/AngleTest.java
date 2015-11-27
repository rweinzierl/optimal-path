package rw.test.opath2;

import java.util.Map;
import java.util.TreeMap;

import rw.opath2.core.Topology;
import rw.opath2.standard.NodeIJ;
import rw.opath2.standard.RectangularToplology;

public class AngleTest {

	public static void main(String[] args) {
		NodeIJ center = new NodeIJ(0, 0);
		Topology<NodeIJ> toplogy = RectangularToplology.TOPLOLOGY_24_NEIGHBOURS;
		TreeMap<Double, NodeIJ> nodeByAngle = new TreeMap<Double, NodeIJ>();
		for (NodeIJ node : toplogy.getNeighbours(center))
			nodeByAngle.put(Math.toDegrees(Math.atan2(node.j, node.i)), node);
		Double lastAngle = null;
		for (Map.Entry<Double, NodeIJ> entry : nodeByAngle.entrySet()) {
			Double angle = entry.getKey();
			System.out.println(entry.getValue().i + "," + entry.getValue().j + " "
					+ +(lastAngle != null ? (angle - lastAngle) : Double.NaN) + " " + angle);
			lastAngle = angle;
		}
		System.out.println();
		for (int i = 1; i < 10; i++) {
			double maxAngle = Math.toDegrees(Math.atan2(i, 1)) - 90;
			System.out.println(i + " " + maxAngle);
		}
	}

}
