package org.cny.jwf.netw;

public class Bytes {
	NetwRunnable nr;
	byte[] hb;
	int offs;
	int len;

	public Bytes(NetwRunnable nr, byte[] bys) {
		this.nr = nr;
		this.hb = bys;
		this.offs = 0;
		this.len = bys.length;
	}

	public Bytes(NetwRunnable nr, byte[] bys, int offs, int len) {
		this.nr = nr;
		this.hb = bys;
		this.offs = offs;
		this.len = len;
	}

	public int length() {
		return this.len;
	}

	public int offset() {
		return this.offs;
	}

	public byte[] bys() {
		return this.hb;
	}

	public Bytes slice(int offs) {
		return this.slice(offs, this.len - (offs - this.offs));
	}

	public Bytes slice(int offs, int len) {
		return new Bytes(this.nr, this.hb, offs, len);
	}

	@Override
	public String toString() {
		return new String(this.hb, this.offs, this.len);
	}

	public String toBs() {
		return bstr(this.hb, this.offs, this.len);
	}

	public byte get(int idx) {
		return this.hb[idx];
	}

	public <T> T V(Class<T> cls) {
		return this.nr.getConverter().B2V(this, cls);
	}

	public static String bstr(byte[] bys) {
		return bstr(bys, 0, bys.length);
	}

	public static String bstr(byte[] bys, int offset, int len) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (bys.length > 0 && offset < bys.length && offset > -1 && len > 0) {
			for (int i = offset; i < len + offset; i++) {
				sb.append(',');
				sb.append(bys[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
