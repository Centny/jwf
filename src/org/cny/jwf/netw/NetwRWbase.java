package org.cny.jwf.netw;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.List;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwBase;

public class NetwRWbase extends BufferedOutputStream implements NetwBase {
	protected int limit;
	protected InputStream in;
	protected OutputStream out;
	// private Writer out;
	protected final ByteBuffer wbuf = ByteBuffer.allocate(5);

	public NetwRWbase(OutputStream out, int sz, InputStream in, int limit) {
		super(out, sz);
		this.out = out;
		this.in = in;
		this.limit = limit;
	}

	@Override
	public void setLimit(int l) {
		this.limit = l;
	}

	@Override
	public int readw(byte[] buf, int off, int len) throws IOException {
		int rlen = 0;
		int toff = off;
		int tlen = len;
		while (tlen > 0) {
			rlen = this.in.read(buf, toff, tlen);
			if (rlen < 0) {
				throw new EOFException();
			}
			toff += rlen;
			tlen -= rlen;
			rlen = 0;
		}
		return len;
	}

	@Override
	public void writeM(List<Cmd> ms) throws IOException {
		if (ms == null || ms.isEmpty()) {
			throw new InvalidParameterException("data lise is null or empty");
		}
		synchronized (this.out) {
			short len = 0;
			for (Cmd m : ms) {
				int mlen = m.length();
				if (mlen > this.limit) {
					throw new IOException("message too large, must less "
							+ this.limit + ", but " + mlen);
				}
				len += mlen;
			}
			if (len > this.limit) {
				throw new IOException("message too large, must less "
						+ this.limit + ", but " + len);
			}
			this.wbuf.clear();
			this.wbuf.put(Netw.H_MOD);
			this.wbuf.putShort(len);
			this.out.write(this.wbuf.array());
			for (Cmd m : ms) {
//				System.err.println("->" + Utils.join(m.bys(), ","));
				this.out.write(m.bys(), m.offset(), m.length());
			}
			this.out.flush();
		}
	}
}
