package org.cny.jwf.netw;

import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.Netw;

public class NetwM implements Msg {
	Netw nw;
	byte[] hb;
	int offs;
	int len;

	public NetwM(Netw nw, byte[] bys) {
		this.nw = nw;
		this.hb = bys;
		this.offs = 0;
		this.len = bys.length;
	}

	public NetwM(Netw nw, byte[] bys, int offs, int len) {
		this.nw = nw;
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

	public Msg slice(int offs) {
		return this.slice(offs, this.len - (offs - this.offs));
	}

	public Msg slice(int offs, int len) {
		return new NetwM(this.nw, this.hb, offs, len);
	}

	@Override
	public Netw netw() {
		return this.nw;
	}

	public byte get(int idx) {
		return this.hb[idx];
	}

	@Override
	public String toString() {
		return new String(this.hb, this.offs, this.len);
	}

//	public String toBs() {
//		return bstr(this.hb, this.offs, this.len);
//	}

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
