package org.cny.jwf.netw;

import org.cny.jwf.netw.r.Converter;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.Netw.ModException;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwVer;

public abstract class RWRunnerv extends RWRunner implements Converter {

	public class NetwRWv_i extends NetwRWv {

		public NetwRWv_i(NetwBase rwb) {
			super(rwb);
		}

		@Override
		public <T> T B2V(Cmd bys, Class<T> cls) {
			return RWRunnerv.this.B2V(bys, cls);
		}

		@Override
		public Cmd V2B(NetwVer nv, Object o) {
			return RWRunnerv.this.V2B(nv, o);
		}

	}

	public RWRunnerv(CmdListener msg, EvnListener evn) {
		super(msg, evn);
	}

	@Override
	protected Netw createNetw() throws ModException {
		this.rw = new NetwRWv_i(this.createNetwBase());
		return this.rw;
	}

	protected abstract NetwBase createNetwBase();

}
