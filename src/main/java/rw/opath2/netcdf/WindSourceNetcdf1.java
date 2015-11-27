package rw.opath2.netcdf;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;

import rw.netcdf.NetcdfReader1;
import rw.opath2.wind.WindSource;

public class WindSourceNetcdf1 implements WindSource {

	NetcdfReader1[] readers;

	public WindSourceNetcdf1(File rootFile, FileFilter[] fileFilters, int indexZ, String[] variableNames)
			throws Exception {
		int len = variableNames.length;
		readers = new NetcdfReader1[len];
		for (int i = 0; i < len; i++)
			readers[i] = new NetcdfReader1(rootFile, fileFilters[i], indexZ, variableNames[i]);
	}

	private double[] tmp = new double[1];

	@Override
	public void getUVWind(long t, double lng, double lat, double[] out) throws Exception {
		for (int i = 0; i < readers.length; i++) {
			if (!readers[i].moveToTimestamp(t))
				throw new NoDataException("No data for timestamp " + new Date(t));
			out[i] = readers[i].getValues(lng, lat, tmp)[0];
		}
	}

	@Override
	public void close() throws IOException {
		for (NetcdfReader1 reader : readers)
			reader.close();
	}

}
