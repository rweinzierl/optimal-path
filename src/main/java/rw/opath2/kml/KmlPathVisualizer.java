package rw.opath2.kml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import rw.opath2.core.NodeLngLat;
import rw.opath2.standard.NodeWithTimestamp;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.gx.Track;

public class KmlPathVisualizer {

	public static <TNode extends NodeLngLat> void toKml(List<NodeWithTimestamp<TNode>> path, File kmlFile,
			boolean openInGoogleEarth) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Kml kml = new Kml();
		Track track = kml.createAndSetPlacemark().createAndSetTrack();
		for (NodeWithTimestamp<? extends NodeLngLat> node : path) {
			track.addToWhen(df.format(new Date((long) node.timestamp)));
			track.addToCoord(node.node.getLng() + "," + node.node.getLat());
		}
		kml.marshal(kmlFile);
		if (openInGoogleEarth)
			Runtime.getRuntime().exec("cmd /c \"" + kmlFile.getAbsolutePath() + "\"").waitFor();
	}
}
