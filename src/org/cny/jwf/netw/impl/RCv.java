package org.cny.jwf.netw.impl;

import java.io.IOException;

import org.cny.jwf.netw.r.Converter;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;

public class RCv extends RC {
	protected Converter c;

	public RCv(Netw rw, Converter c) {
		super(rw);
		this.c = c;
	}

	public void exec(byte m, Object args, CmdListener l) throws IOException {
		this.exec(m, this.c.V2B(null, args), l);
	}
}
