package org.cny.jwf.netw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.NetwVer;

public abstract class NetwRWv extends NetwRW implements NetwVer {

	public NetwRWv(OutputStream out, int sz, InputStream in) {
		super(out, sz, in);
	}

	@Override
	public void writev(Object v) throws IOException {
		this.writeM(this.V2B(this, v));
	}

	@Override
	public Msg readM() throws IOException, ModException {
		return new NetwMv(this, this.readm());
	}
}
