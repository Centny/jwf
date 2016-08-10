package org.cny.jwf.netw;

import java.io.IOException;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwVer;

public abstract class NetwRWv extends NetwRW implements NetwVer {


	public NetwRWv(NetwBase rwb) {
		super(rwb);
	}

	@Override
	public void writev(Object v) throws IOException {
		this.writeM(this.V2B(this, v));
	}

	@Override
	public Cmd newM(byte[] m, int off, int len) {
		return new NetwMv(this, m, off, len);
	}
}
