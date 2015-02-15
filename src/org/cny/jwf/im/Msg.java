package org.cny.jwf.im;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.cny.jwf.im.pb.Msg.ImMsg;
import org.cny.jwf.util.Orm.Name;
import org.cny.jwf.util.Utils;

public class Msg implements Serializable {

	private static final long serialVersionUID = 26764532216064436L;
	// private static int IDC = 0;
	public static final int MS_REV = 0;
	public static final int MS_MARK = 1;
	public static final int MS_READED = 1 << 1;
	public static final int MS_DEL = 1 << 2;
	public static final int MS_SENDING = 1 << 3;
	public static final int MS_SENDED = 1 << 4;
	public static final int MS_SEND_ERR = 1 << 5;
	public String i;
	public String s;
	public String[] r;
	public int t;
	public String d;
	public byte[] c;
	public String a;
	public long time;
	public int status = MS_REV;

	public Msg() {
		// do nothing.
	}

	public Msg(String s, String r, int t, byte[] c, int status) {
		super();
		// this.i = "L" + IDC++;
		this.s = s;
		this.r = new String[] { r };
		this.t = t;
		this.c = c;
		this.time = new Date().getTime();
		this.status = status;
	}

	public Msg(ImMsg im) {
		this.i = im.getI();
		this.s = im.getS();
		List<String> ssl = im.getRList();
		this.r = ssl.toArray(new String[ssl.size()]);
		this.t = im.getT();
		this.d = im.getD();
		this.c = im.getC().toByteArray();
		this.a = im.getA();
		this.time = im.getTime();
	}

	@Name(name = "I")
	public void setI(String i) {
		this.i = i;
	}

	@Name(name = "S")
	public void setS(String s) {
		this.s = s;
	}

	@Name(name = "R")
	public void setRs(String r) {
		this.r = r.split(",");
	}

	@Name(name = "T")
	public void setT(int t) {
		this.t = t;
	}

	@Name(name = "D")
	public void setD(String d) {
		this.d = d;
	}

	@Name(name = "C")
	public void setCs(String c) {
		this.c = c.getBytes();
	}

	@Name(name = "A")
	public void setA(String a) {
		this.a = a;
	}

	@Name(name = "TIME")
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.toObjects());
	}

	public Object[] toObjects() {
		return new Object[] { this.i, this.s,
				this.r == null ? null : Utils.join(this.r), d, t,
				this.c == null ? null : new String(this.c), this.a, this.time,
				this.status };
	}
}
