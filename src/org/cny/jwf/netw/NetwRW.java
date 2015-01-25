package org.cny.jwf.netw;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.List;

public class NetwRW extends BufferedOutputStream implements Netw {
	private int limit;
	private InputStream in;
	// private Writer out;
	private final ByteBuffer wbuf = ByteBuffer.allocate(5);
	private final byte[] hbuf = new byte[5];

	public NetwRW(OutputStream out, InputStream in) {
		super(out);
		this.in = in;
		this.limit = MAX_ML;
	}

	public NetwRW(OutputStream out, int sz, InputStream in) {
		super(out, sz);
		this.in = in;
		this.limit = MAX_ML;
	}

	@Override
	public void setLimit(int l) {
		this.limit = l;
	}

	@Override
	public int readw(byte[] buf) throws IOException {
		return this.readw(buf, 0, buf.length);
	}

	@Override
	public int readw(byte[] buf, int off, int len) throws IOException {
		int rlen = 0;
		while (len > 0) {
			rlen = this.in.read(buf, off, len);
			off += rlen;
			len -= rlen;
			rlen = 0;
		}
		return len;
	}

	@Override
	public void writem(byte[] m) throws IOException {
		this.writem(m, 0, m.length);
	}

	@Override
	public void writem(byte[] m, int off, int len) throws IOException {
		if (m == null || off < 0 || len < 1 || m.length < off + len) {
			throw new InvalidParameterException("parameter err:" + m + ","
					+ off + "," + len);
		}
		synchronized (this) {
			if (len > this.limit) {
				throw new IOException("message too large, must less "
						+ this.limit + ", but " + len);
			}
			this.wbuf.clear();
			this.wbuf.put(H_MOD);
			this.wbuf.putShort((short) len);
			this.out.write(this.wbuf.array());
			this.out.write(m, off, len);
			this.out.flush();
		}
	}

	@Override
	public void writem(List<byte[]> ms) throws IOException {
		if (ms == null || ms.isEmpty()) {
			throw new InvalidParameterException("data lise is null or empty");
		}
		synchronized (this) {
			short len = 0;
			for (byte[] m : ms) {
				if (m.length > this.limit) {
					throw new IOException("message too large, must less "
							+ this.limit + ", but " + m.length);
				}
				len += m.length;
			}
			if (len > this.limit) {
				throw new IOException("message too large, must less "
						+ this.limit + ", but " + len);
			}
			this.wbuf.clear();
			this.wbuf.put(H_MOD);
			this.wbuf.putShort(len);
			this.out.write(this.wbuf.array());
			for (byte[] m : ms) {
				this.out.write(m);
			}
			this.out.flush();
		}
	}

	private boolean valid_h(byte[] bys, int offs) {
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
		if (!this.valid_h(this.hbuf, 0)) {
			throw new ModException("reading invalid mod for data:"
					+ Bytes.bstr(this.hbuf));
		}
		short len = 0;
		len += this.hbuf[3] << 8;
		len += this.hbuf[4];
		byte[] tbuf = new byte[len];
		this.readw(tbuf);
		return tbuf;
	}
}
