package rw.test.opath2;

import java.io.File;
import java.util.List;

import rw.opath2.core.OneStepTravelTimeCalculatorFactory;
import rw.opath2.core.PathFinder;
import rw.opath2.core.Topology;
import rw.opath2.core.TravelTimeCalculatorFactory;
import rw.opath2.io.NodeExternalizer;
import rw.opath2.io.PathBackReader;
import rw.opath2.io.PathWriter;
import rw.opath2.standard.BreakConditionTargetNode;
import rw.opath2.standard.NodeIJ;
import rw.opath2.standard.NodeWithTimestamp;
import rw.opath2.standard.RectangularToplology;

public class GridTest3 {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		File file = new File("/tmp/path.csv");
		Topology<NodeIJ> topology = RectangularToplology.TOPLOLOGY_4_NEIGHBOURS;
		NodeIJ startNode = new NodeIJ(-150, 0);
		NodeIJ endNode = new NodeIJ(150, 0);
		TravelTimeCalculatorFactory<NodeIJ> travelTimeCalculatorFactory = new OneStepTravelTimeCalculatorFactory<NodeIJ>() {
			@Override
			protected double travelTime(NodeIJ node1, NodeIJ node2, double startTimestamp) {
				int di = node1.i - node2.i;
				int dj = node1.j - node2.j;
				double d = Math.sqrt(di * di + dj * dj);
				double distFromOrigin = Math.sqrt(node1.i * node1.i + node1.j * node1.j);
				return distFromOrigin >= 100 ? d : d * 100;
			}
		};
		NodeExternalizer<NodeIJ> externalizer = NodeIJ.externalizer;
		final PathWriter<NodeIJ> pathWriter = new PathWriter<NodeIJ>(externalizer, file);
		PathFinder<NodeIJ> pathFinder = new PathFinder<NodeIJ>(topology, travelTimeCalculatorFactory, pathWriter,
				new BreakConditionTargetNode<NodeIJ>(endNode));
		pathFinder.init(startNode, 0);
		pathFinder.run();
		pathWriter.close();
		PathBackReader<NodeIJ> pathReader = new PathBackReader<NodeIJ>(file, externalizer);
		List<NodeWithTimestamp<NodeIJ>> path = pathReader.findPathToTarget(endNode);
		pathReader.close();
		for (NodeWithTimestamp<NodeIJ> node : path) {
			System.out.println(node);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000.0 + " seconds");
	}

}
