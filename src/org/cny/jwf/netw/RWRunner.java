package org.cny.jwf.netw;

import java.io.InputStream;
import java.io.OutputStream;

import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.Netw.ModException;
import org.cny.jwf.netw.r.NetwRunner;

public class RWRunner extends NetwRunner {
	OutputStream out;
	int sz;
	InputStream in;
	NetwRW rw;

	public RWRunner(OutputStream out, int sz, InputStream in, MsgListener msg,
			EvnListener evn) {
		super(msg, evn);
		this.out = out;
		this.sz = sz;
		this.in = in;
	}

	@Override
	protected Netw createNetw() throws ModException {
		this.rw = new NetwRW(this.out, this.sz, this.in);
		return this.rw;
	}

	@Override
	public Netw netw() {
		return this.rw;
	}

}
