package org.cny.jwf.netw;

import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunner;

public abstract class RWRunner extends NetwRunner {
	protected Netw rw;

	public RWRunner(CmdListener msg, EvnListener evn) {
		super(msg, evn);
	}

	@Override
	protected abstract Netw createNetw() throws Exception;

	@Override
	public Netw netw() {
		return this.rw;
	}

}
