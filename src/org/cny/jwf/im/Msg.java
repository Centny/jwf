package org.cny.jwf.im;

import java.util.Base64;

import org.cny.jwf.netw.r.Cmd;

public class Msg {
	public String id;
	public String s;
	public String[] r;
	public String d;
	public byte t;
	public byte[] cdata;
	public Cmd cmd;

	/**
	 * the empty constructor.
	 */
	public Msg() {
		//nothing init.
	}

	public Msg(String[] r, byte t, byte[] c) {
		this.r = r;
		this.t = t;
		this.cdata = c;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String[] getR() {
		return r;
	}

	public void setR(String[] r) {
		this.r = r;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public byte getT() {
		return t;
	}

	public void setT(byte t) {
		this.t = t;
	}

	public String getC() {
		return Base64.getEncoder().encodeToString(this.cdata);
	}

	public void setC(String c) {
		this.cdata = Base64.getDecoder().decode(c);
	}

	public byte[] getCdata() {
		return cdata;
	}

	public void setCdata(byte[] cdata) {
		this.cdata = cdata;
	}

}
