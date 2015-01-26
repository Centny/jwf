package org.cny.jwf.im;

import org.cny.jwf.netw.r.Cmd;

public class Msg {
	public String s;
	public String[] r;
	public String d;
	public byte t;
	public byte[] c;
	public Cmd cmd;

	public Msg(String[] r, byte t, byte[] c) {
		this.r = r;
		this.t = t;
		this.c = c;
	}

}
