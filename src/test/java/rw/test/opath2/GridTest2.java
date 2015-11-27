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

public class GridTest2 {

	public static void main(String[] args) throws Exception {
		Topology<NodeIJ> topology = RectangularToplology.TOPLOLOGY_4_NEIGHBOURS;
		TravelTimeCalculatorFactory<NodeIJ> travelTimeCalculatorFactory = new OneStepTravelTimeCalculatorFactory<NodeIJ>() {
			@Override
			protected double travelTime(NodeIJ node1, NodeIJ node2, double startTimestamp) {
				int di = node1.i - node2.i;
				int dj = node1.j - node2.j;
				return Math.sqrt(di * di + dj * dj);
			}
		};
		File file = new File("/tmp/path.csv");
		NodeExternalizer<NodeIJ> externalizer = NodeIJ.externalizer;
		final PathWriter<NodeIJ> pathWriter = new PathWriter<NodeIJ>(externalizer, file);
		NodeIJ startNode = new NodeIJ(0, 0);
		NodeIJ endNode = new NodeIJ(4, 8);
		PathFinder<NodeIJ> pathFinder = new PathFinder<NodeIJ>(topology, travelTimeCalculatorFactory, pathWriter,
				new BreakConditionTargetNode<NodeIJ>(endNode));
		pathFinder.init(startNode, 0);
		pathFinder.run();
		pathWriter.close();
		PathBackReader<NodeIJ> pathReader = new PathBackReader<NodeIJ>(file, externalizer);
		List<NodeWithTimestamp<NodeIJ>> path = pathReader.findPathToTarget(endNode);
		pathReader.close();
		System.out.println(path);
	}

}
