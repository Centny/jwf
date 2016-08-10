package org.cny.jwf.netw.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cny.jwf.netw.NetwM;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwBase;

public class OBDC implements NetwBase {
	protected byte m;
	protected NetwBase nb;

	public OBDC(NetwBase nb, byte m) {
		this.nb = nb;
		this.m = m;
	}

	@Override
	public void writeM(List<Cmd> ms) throws IOException {
		List<Cmd> tms = new ArrayList<Cmd>();
		tms.add(new NetwM(null, new byte[] { this.m }, 0, 1));
		tms.addAll(ms);
		this.nb.writeM(tms);
	}

	@Override
	public void setLimit(int l) {
		this.nb.setLimit(l);
	}

	@Override
	public int readw(byte[] buf, int off, int len) throws IOException {
		return this.nb.readw(buf, off, len);
	}
}
