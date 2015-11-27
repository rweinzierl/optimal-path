package rw.opath2.wind;

import java.io.Closeable;

public interface WindSource extends Closeable {

	public void getUVWind(long t, double lng, double lat, double[] out) throws Exception;

}
