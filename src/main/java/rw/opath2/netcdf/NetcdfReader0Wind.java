package rw.opath2.netcdf;

import rw.opath2.wind.WindSource;

/*
	download data from http://apps.ecmwf.int/datasets/data/interim_full_daily/
	select months
	Select time: Select all
	Select Step: 0
	Select parameter: 10 metre U wind component, 10 metre V wind component
	Retrive Grib
	Retrive now
 */

public class NetcdfReader0Wind extends NetcdfReader0 implements WindSource {

	public NetcdfReader0Wind(String location, String variableNameUWind, String variableNameVWind) throws Exception {
		super(location, variableNameUWind, variableNameVWind);
	}

	@Override
	public void getUVWind(long t, double lng, double lat, double[] out) throws Exception {
		getValues(t, lng, lat, out);
	}

}
