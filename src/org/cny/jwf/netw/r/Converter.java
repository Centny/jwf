package org.cny.jwf.netw.r;

public interface Converter {
	<T> T B2V(Msg bys, Class<T> cls);

	Msg V2B(NetwVer nv, Object o);
}