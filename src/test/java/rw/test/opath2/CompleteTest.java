package rw.test.opath2;

import java.io.File;
import java.util.List;

import rw.opath2.core.NodeUtil;
import rw.opath2.core.PathFinder;
import rw.opath2.core.Topology;
import rw.opath2.grid.isea.IseaReader;
import rw.opath2.grid.isea.NodeIsea;
import rw.opath2.io.NodeExternalizer;
import rw.opath2.io.PathBackReader;
import rw.opath2.io.PathWriter;
import rw.opath2.kml.KmlPathVisualizer;
import rw.opath2.netcdf.NetcdfReader0Wind;
import rw.opath2.standard.BreakConditionTargetNode;
import rw.opath2.standard.NodeWithTimestamp;
import rw.opath2.wind.TravelTimeCalculatorFactoryWind0;
import rw.opath2.wind.WindSource;

public class CompleteTest {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setProperty("isea.dir", "/data/eclipse-workspace/movebank/src/main/java/org/movebank/analysis/wind");
		WindSource r = new NetcdfReader0Wind("/tmp/mars-atls03-20141023100701-36279-5881.grib",
				"10_metre_U_wind_component_surface", "10_metre_V_wind_component_surface");
		//FIXME: duplicate timestamps in output appear to be bug!
		List<NodeIsea> cells = IseaReader.getGridCells(8);
		NodeIsea.addSecondOrderNeighbours(cells);
		Topology<NodeIsea> topology = NodeIsea.TOPOLOGY;
		NodeIsea startNode = NodeUtil.findClosestNode(26, 68, cells);
		NodeIsea endNode = NodeUtil.findClosestNode(23, -32, cells);
		File file = new File("/tmp/path.csv");
		TravelTimeCalculatorFactoryWind0<NodeIsea> cf = new TravelTimeCalculatorFactoryWind0<>(10, 3600, r);
		NodeExternalizer<NodeIsea> externalizer = NodeIsea.EXTERNALIZER;
		final PathWriter<NodeIsea> pathWriter = new PathWriter<NodeIsea>(externalizer, file);
		PathFinder<NodeIsea> pathFinder = new PathFinder<>(topology, cf, pathWriter,
				new BreakConditionTargetNode<NodeIsea>(endNode));

		pathFinder.init(startNode, 1388840400000L);
		pathFinder.run();
		pathWriter.close();
		PathBackReader<NodeIsea> pathReader = new PathBackReader<NodeIsea>(file, externalizer);
		List<NodeWithTimestamp<NodeIsea>> path = pathReader.findPathToTarget(endNode);
		pathReader.close();
		for (NodeWithTimestamp<NodeIsea> node : path) {
			System.out.println(node);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000.0 + " seconds");
		KmlPathVisualizer.toKml(path, new File("/tmp/path.kml"), true);
	}

}
