package org.cny.jwf.netw;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwBase;

public abstract class NetwRW implements Netw {
	private final byte[] hbuf = new byte[5];
	protected NetwBase rwb;

	public NetwRW(NetwBase rwb) {
		super();
		this.rwb = rwb;
	}

	@Override
	public int readw(byte[] buf) throws IOException {
		return this.readw(buf, 0, buf.length);
	}

	@Override
	public void writem(byte[] m, int offs, int len) throws IOException {
		this.writeM(this.newM(m, offs, len));
	}

	@Override
	public void writem(byte[] m) throws IOException {
		this.writem(m, 0, m.length);
	}

	@Override
	public void writem(List<byte[]> ms) throws IOException {
		if (ms == null || ms.isEmpty()) {
			throw new InvalidParameterException("data lise is null or empty");
		}
		List<Cmd> tms = new ArrayList<Cmd>();
		for (byte[] m : ms) {
			tms.add(this.newM(m, 0, m.length));
		}
		this.writeM(tms);
	}

	@Override
	public void writeM(Cmd b) throws IOException {
		List<Cmd> ms = new ArrayList<Cmd>();
		ms.add(b);
		this.writeM(ms);
	}

	public static boolean valid_h(byte[] bys, int offs) {
		for (int i = 0; i < H_MOD.length; i++) {
			if (H_MOD[i] == bys[i]) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public byte[] readm() throws IOException, ModException {
		this.readw(this.hbuf);
		int len = 0;
		len += (this.hbuf[3] & 0xff) << 8;
		len += (this.hbuf[4] & 0xff);
		if (!valid_h(this.hbuf, 0) || len < 1) {
			throw new ModException("reading invalid mod for data:"
					+ NetwM.bstr(this.hbuf));
		}
		byte[] tbuf = new byte[len];
		this.readw(tbuf);
		return tbuf;
	}

	@Override
	public Cmd readM() throws IOException, ModException {
		byte[] m = this.readm();
		return this.newM(m, 0, m.length);
	}

	@Override
	public void setLimit(int l) {
		this.rwb.setLimit(l);
	}

	@Override
	public int readw(byte[] buf, int off, int len) throws IOException {
		return this.rwb.readw(buf, off, len);
	}

	@Override
	public void writeM(List<Cmd> ms) throws IOException {
		this.rwb.writeM(ms);
	}
}
