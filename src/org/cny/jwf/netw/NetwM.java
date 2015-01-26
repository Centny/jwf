package org.cny.jwf.netw;

import java.io.IOException;
import java.util.List;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;

public class NetwM implements Cmd {
	protected Netw nw;
	protected byte[] hb;
	protected int off;
	protected int len;

	public NetwM(Netw nw, byte[] bys) {
		this.nw = nw;
		this.hb = bys;
		this.off = 0;
		this.len = bys.length;
	}

	public NetwM(Netw nw, byte[] bys, int off, int len) {
		this.nw = nw;
		this.hb = bys;
		this.off = off;
		this.len = len;
	}

	public int length() {
		return this.len;
	}

	public int offset() {
		return this.off;
	}

	public byte[] bys() {
		return this.hb;
	}

	public Cmd slice(int off) {
		if (off >= this.len) {
			throw new IndexOutOfBoundsException();
		}
		return this.slice(this.off, this.len - off);
	}

	public Cmd slice(int off, int len) {
		if (off + len > this.len) {
			throw new IndexOutOfBoundsException();
		}
		return this.newM(this.hb, this.off + off, len);
	}

	public byte get(int idx) {
		return this.hb[this.off + idx];
	}

	@Override
	public String toString() {
		return new String(this.hb, this.off, this.len);
	}

	public String toBs() {
		return bstr(this.hb, this.off, this.len);
	}

	public static String bstr(byte[] bys) {
		return bstr(bys, 0, bys.length);
	}

	public static String bstr(byte[] bys, int offset, int len) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (bys.length > 0 && offset < bys.length && offset > -1 && len > 0) {
			sb.append(bys[offset]);
			for (int i = offset + 1; i < len + offset; i++) {
				sb.append(',');
				sb.append(bys[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void reset(int off, int len) {
		if (off < 0 || off >= this.hb.length || len < 0 || len > this.hb.length
				|| off + len > this.hb.length) {
			throw new IndexOutOfBoundsException();
		}
		this.off = off;
		this.len = len;
	}

	@Override
	public void setLimit(int l) {
		this.nw.setLimit(l);
	}

	@Override
	public int readw(byte[] buf) throws IOException {
		return this.nw.readw(buf);
	}

	@Override
	public int readw(byte[] buf, int off, int len) throws IOException {
		return this.nw.readw(buf, off, len);
	}

	@Override
	public byte[] readm() throws IOException, ModException {
		return this.nw.readm();
	}

	@Override
	public Cmd readM() throws IOException, ModException {
		return this.nw.readM();
	}

	@Override
	public Cmd newM(byte[] m, int off, int len) {
		return this.nw.newM(m, off, len);
	}

	@Override
	public void writem(byte[] m) throws IOException {
		this.nw.writem(m);
	}

	@Override
	public void writem(byte[] m, int offs, int len) throws IOException {
		this.nw.writem(m, offs, len);
	}

	@Override
	public void writem(List<byte[]> ms) throws IOException {
		this.nw.writem(ms);
	}

	@Override
	public void writeM(List<Cmd> ms) throws IOException {
		this.nw.writeM(ms);
	}

	@Override
	public void writeM(Cmd b) throws IOException {
		this.nw.writeM(b);
	}

	@Override
	public short shortv(int off) {
		return (short) ((this.hb[this.off + off] << 8) + (this.hb[this.off
				+ off + 1]));
	}

	@Override
	public void forward(int len) {
		this.off += len;
		this.len -= len;
	}

	@Override
	public <T> T V(Class<T> cls) {
		return null;
	}

}
