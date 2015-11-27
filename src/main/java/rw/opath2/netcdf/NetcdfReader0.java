package rw.opath2.netcdf;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import ucar.nc2.dataset.CoordinateAxis1DTime;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.grid.GeoGrid;
import ucar.nc2.dt.grid.GridDataset;

/**
 * 
 * nearest neighbour interpolation is used
 *
 */
public class NetcdfReader0 implements Closeable {

	private final GridCoordSystem coordinateSystem;
	private final long temporalResolution;
	private GridDataset gridDs;
	private final GeoGrid[] grids;

	private final long[] availableTimeStamps;
	private int indexOfCurrentTimeStamp;

	private float[][][] dataForCurrentTimeStamp;

	public NetcdfReader0(String location, String... variableNames) throws Exception {
		gridDs = GridDataset.open(location);
		grids = new GeoGrid[variableNames.length];
		for (int i = 0; i < variableNames.length; i++)
			grids[i] = (GeoGrid) gridDs.findGridByShortName(variableNames[i]);
		coordinateSystem = grids[0].getCoordinateSystem();
		CoordinateAxis1DTime timeAxis = coordinateSystem.getTimeAxis1D();
		temporalResolution = (long) timeAxis.getTimeResolution().getValueInSeconds() * 1000;
		availableTimeStamps = new long[timeAxis.getCalendarDates().size()];
		for (int i = 0; i < availableTimeStamps.length; i++)
			availableTimeStamps[i] = timeAxis.getCalendarDates().get(i).getMillis();
		indexOfCurrentTimeStamp = -1;
	}

	boolean timestampIsClose(long t, int i) {
		return Math.abs(availableTimeStamps[i] - t) <= temporalResolution / 2;
	}

	int getIndexOfClosestTimestamp(long t) {
		int ti = -1;
		for (int i = 0; i < availableTimeStamps.length; i++)
			if (timestampIsClose(t, i)) {
				ti = i;
				break;
			}
		return ti;
	}

	void moveToTimestamp(long t) throws Exception {
		if (indexOfCurrentTimeStamp == -1 || !timestampIsClose(t, indexOfCurrentTimeStamp)) {
			indexOfCurrentTimeStamp = getIndexOfClosestTimestamp(t);
			dataForCurrentTimeStamp = new float[grids.length][][];
			if (indexOfCurrentTimeStamp != -1)
				for (int i = 0; i < grids.length; i++)
					dataForCurrentTimeStamp[i] = (float[][]) grids[i].readYXData(indexOfCurrentTimeStamp, -1)
							.copyToNDJavaArray();
		}
	}

	public void getValues(long t, double lng, double lat, double[] out) throws Exception {
		moveToTimestamp(t);
		if (indexOfCurrentTimeStamp != -1) {
			int[] xyi = new int[2];
			grids[0].getCoordinateSystem().findXYindexFromLatLon(lat, lng, xyi);
			for (int i = 0; i < out.length; i++)
				out[i] = dataForCurrentTimeStamp[i][xyi[1]][xyi[0]];
		}
	}

	@Override
	public void close() throws IOException {
		gridDs.close();
	}

	public static void main(String[] args) throws Exception {
		NetcdfReader0 r = new NetcdfReader0("/tmp/mars-atls03-20141023100701-36279-5881.grib",
				"10_metre_U_wind_component_surface", "10_metre_V_wind_component_surface");
		long t = 1388840400000L;
		double lng = -110.1;
		double lat = 48.1;
		double[] uv = new double[2];
		r.getValues(t, lng, lat, uv);
		r.close();
		System.out.println(Arrays.toString(uv));
	}

}
