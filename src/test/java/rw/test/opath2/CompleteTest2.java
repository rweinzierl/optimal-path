package rw.test.opath2;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;
import rw.netcdf.FileFilterRegex;
import rw.opath2.core.NodeUtil;
import rw.opath2.core.PathFinder;
import rw.opath2.core.Topology;
import rw.opath2.grid.isea.IseaReader;
import rw.opath2.grid.isea.NodeIsea;
import rw.opath2.io.NodeExternalizer;
import rw.opath2.io.PathBackReader;
import rw.opath2.io.PathWriter;
import rw.opath2.kml.KmlPathVisualizer;
import rw.opath2.netcdf.WindSourceNetcdf1;
import rw.opath2.standard.BreakConditionTargetNode;
import rw.opath2.standard.NodeWithTimestamp;
import rw.opath2.wind.TravelTimeCalculatorFactoryWind0;
import rw.opath2.wind.WindSource;

public class CompleteTest2 extends TestCase {

	public void test() throws Exception {
		long start = System.currentTimeMillis();
		System.setProperty("isea.dir", "/data/eclipse-workspace/movebank/src/main/java/org/movebank/analysis/wind");
		WindSource r = new WindSourceNetcdf1(new File("/tmp/bart/pl/850"), new FileFilter[] {
				new FileFilterRegex(".*131\\.128.*\\.grib"), new FileFilterRegex(".*132\\.128.*\\.grib") }, 0,
				new String[] { "U_velocity_isobaric", "V_velocity_isobaric" });
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		long ts = df.parse("2014-01-01 00:00:00").getTime();
		pathFinder.init(startNode, ts);
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
		KmlPathVisualizer.toKml(path, new File("/tmp/path3.kml"), true);

	}

}
