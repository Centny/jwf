package org.cny.jwf.netw;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetwRunner extends NetwRW implements NetwRunnable {
	private static Logger L = LoggerFactory.getLogger(NetwRunner.class);
	//
	MsgListener msgl;
	EvnListener evnl;
	private boolean running;

	public NetwRunner(OutputStream out, InputStream in, MsgListener msg,
			EvnListener evn) {
		super(out, in);
		this.msgl = msg;
		this.evnl = evn;
	}

	public NetwRunner(OutputStream out, int sz, InputStream in,
			MsgListener msg, EvnListener evn) {
		super(out, sz, in);
		this.msgl = msg;
		this.evnl = evn;
	}

	@Override
	public void run() {
		L.debug("starting running Netw");
		this.running = true;
		try {
			this.run_c();
		} catch (Throwable e) {
			this.evnl.onErr(e);
		}
		this.running = false;
		L.debug("Netw stopped");
	}

	public void stop() {
		this.running = false;
		System.out.println(this.running);
	}

	private void run_c() throws Exception {
		while (this.running) {
			System.out.println(this.running);

			try {
				byte[] m = this.readm();
				this.msgl.onMsg(new Bytes(m));
			} catch (ModException e) {
				L.error(e.getMessage());
				continue;
			}
		}
	}

	@Override
	public void setEvnL(EvnListener l) {
		this.evnl = l;
	}

	@Override
	public void setMsgL(MsgListener l) {
		this.msgl = l;
	}

}
