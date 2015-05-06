package org.cny.jwf.netw.bean;

public class Con {
	// public String cid;
	public String r;
	public String token;
	public String s;
	public byte t;
	public int c;

	// public void setCid(String cid) {
	// this.cid = cid;
	// }

	public void setR(String r) {
		this.r = r;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setS(String s) {
		this.s = s;
	}

	public void setT(byte t) {
		this.t = t;
	}

	public void setC(int c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "R(" + this.r + "),S(" + this.s + "),T(" + this.t + "),C("
				+ this.c + "),Token(" + this.token + ")";
	}

	public static class Res {
		public int code = -1;
		public Con res = new Con();
		public String err = null;

		public void setCode(int code) {
			this.code = code;
		}

		public void setRes(Con res) {
			this.res = res;
		}

		public void setErr(String err) {
			this.err = err;
		}

	}
}
