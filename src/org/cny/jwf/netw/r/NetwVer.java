package org.cny.jwf.netw.r;

import java.io.IOException;

public interface NetwVer extends Netw, Converter {
	void writev(Object v) throws IOException;

	// void setConverter(Converter c);
	//
	// Converter getConverter();

	public static interface NetwVable {
		public <T> T V(Class<T> cls);
	}
}
