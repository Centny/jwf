package org.cny.jwf.netw.r;

public interface Converter {
	<T> T B2V(Cmd bys, Class<T> cls);

	Cmd V2B(NetwVer nv, Object o);
}