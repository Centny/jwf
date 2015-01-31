package org.cny.jwf.netw.impl;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Converter;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;

public class RCv extends RC {
	protected Converter c;

	public RCv(Netw rw, Converter c) {
		super(rw);
		this.c = c;
	}

	public void exec(byte m, Object args, CmdListener l) throws Exception {
		this.exec(m, this.c.V2B(null, args), l);
	}

	public Cmd exec(byte m, Object args) throws Exception, InterruptedException {
		return this.exec(m, this.c.V2B(null, args));
	}

	public <T> T exec(byte m, Object args, Class<T> cls) throws Exception,
			InterruptedException {
		return this.exec(m, this.c.V2B(null, args), cls);
	}
}
