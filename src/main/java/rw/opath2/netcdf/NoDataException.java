package rw.opath2.netcdf;

public class NoDataException extends RuntimeException {

	public NoDataException(String message) {
		super(message);
	}

	public NoDataException(Throwable cause) {
		super(cause);
	}

}
