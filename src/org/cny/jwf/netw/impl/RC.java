package org.cny.jwf.netw.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RC implements CmdListener {

	private static final Logger L = LoggerFactory.getLogger(RC.class);
	protected final Map<Short, CmdListener> hs = new HashMap<Short, CmdListener>();
	protected final Netw rw;
	protected short ei_cc = 1;

	public static class CmdL implements CmdListener {
		Cmd m = null;
		Exception e;

		@Override
		public void onCmd(NetwRunnable nr, Cmd m) {
			synchronized (this) {
				this.setM(m);
				this.notifyAll();
			}
		}

		public void setM(Cmd m) {
			this.m = m;
		}

		public void setE(Exception e) {
			this.e = e;
		}
	}

	public RC(Netw rw) {
		this.rw = rw;
	}

	public <T> T exec(byte m, Cmd args, Class<T> cls) throws Exception {
		Cmd cmd = this.exec(m, args);
		return cmd.V(cls);
	}

	public Cmd exec(byte m, Cmd args) throws Exception {
		CmdL cl = new CmdL();
		synchronized (cl) {
			this.exec(m, args, cl);
			cl.wait();
		}
		if (cl.e == null) {
			return cl.m;
		} else {
			throw cl.e;
		}
	}

	protected short next_ei() {
		return this.ei_cc++;
	}

	public void exec(byte m, Cmd args, CmdListener l) throws Exception {
		synchronized (this) {
			byte[] bs = new byte[] { (byte) (this.ei_cc >> 8),
					(byte) this.ei_cc, 0, m };
			List<Cmd> ms = new ArrayList<Cmd>();
			ms.add(this.rw.newM(bs, 0, bs.length));
			ms.add(args);
			short tei = this.next_ei();
			try {
				this.hs.put(tei, l);
				this.rw.writeM(ms);
			} catch (Exception e) {
				this.hs.remove(tei);
				throw e;
			}
		}

	}

	@Override
	public void onCmd(NetwRunnable nr, Cmd m) {
		try {
			this.onCmd_(nr, m);
		} catch (Exception e) {
			L.warn("RC error:", e);
		}
	}

	public void onCmd_(NetwRunnable nr, Cmd m) throws Exception {
		if (m.length() < 2) {
			L.warn("RC receive invalid command for data less 2:" + m.toBs());
			return;
		}
		synchronized (this) {
			Short mark = m.shortv(0);
			if (this.hs.containsKey(mark)) {
				try {
					m.forward(3);
					this.hs.get(mark).onCmd(nr, m);
				} catch (Exception e) {
					throw e;
				} finally {
					this.hs.remove(mark);
				}
			} else {
				L.warn("RC response handler not found by mark:" + mark);
			}
		}
	}

	public void clear(Exception e) {
		synchronized (this) {
			for (CmdListener s : this.hs.values()) {
				if (!(s instanceof CmdL)) {
					continue;
				}
				CmdL l = (CmdL) s;
				Exception te = e;
				if (te == null) {
					te = new Exception("RC have bean cleared");
				}
				synchronized (l) {
					l.setE(te);
					l.notifyAll();
				}
			}
			L.debug("clear {} command success", this.hs.size());
			this.hs.clear();
		}
	}
}
