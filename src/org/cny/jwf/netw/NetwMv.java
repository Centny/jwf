package org.cny.jwf.netw;

import org.cny.jwf.netw.r.NetwVer;

public class NetwMv extends NetwM implements NetwVer.NetwVable {
	private final NetwVer ver;

	public NetwMv(NetwVer ver, byte[] bys) {
		super(ver, bys);
		this.ver = ver;
	}

	public NetwMv(NetwVer ver, byte[] bys, int offs, int len) {
		super(ver, bys, offs, len);
		this.ver = ver;
	}

	@Override
	public <T> T V(Class<T> cls) {
		return this.ver.B2V(this, cls);
	}
}
