package org.cny.jwf.netw;

import java.io.InputStream;
import java.io.OutputStream;

import org.cny.jwf.netw.r.Converter;
import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.Netw.ModException;
import org.cny.jwf.netw.r.NetwVer;

public abstract class RWRunnerv extends RWRunner implements Converter {

	class NetwRWv_i extends NetwRWv {

		public NetwRWv_i(OutputStream out, int sz, InputStream in) {
			super(out, sz, in);
		}

		@Override
		public <T> T B2V(Msg bys, Class<T> cls) {
			return RWRunnerv.this.B2V(bys, cls);
		}

		@Override
		public Msg V2B(NetwVer nv, Object o) {
			return RWRunnerv.this.V2B(nv, o);
		}

	}

	public RWRunnerv(OutputStream out, int sz, InputStream in, MsgListener msg,
			EvnListener evn) {
		super(out, sz, in, msg, evn);
	}

	@Override
	protected Netw createNetw() throws ModException {
		this.rw = new NetwRWv_i(this.out, this.sz, this.in);
		return this.rw;
	}

}
