package org.cny.jwf.netw;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetwRunner extends NetwRW implements NetwRunnable {
	private static Logger L = LoggerFactory.getLogger(NetwRunner.class);
	//
	MsgListener l;
	private boolean running;

	public NetwRunner(OutputStream out, InputStream in, MsgListener l) {
		super(out, in);
		this.l = l;
	}

	public NetwRunner(OutputStream out, int sz, InputStream in, MsgListener l) {
		super(out, sz, in);
		this.l = l;
	}

	@Override
	public void run() {
		L.debug("starting running Netw");
		this.running = true;
		try {
			this.run_c();
		} catch (Throwable e) {
			this.l.onErr(e);
		}
		this.running = false;
		L.debug("Netw stopped");
	}

	public void stop() {
		this.running = false;
		System.out.println(this.running);
	}

	private void run_c() throws Exception{
		while (this.running) {
			System.out.println(this.running);
			byte[] m;
			try {
				m = this.readm();
				this.l.onMsg(m);
			} catch (ModException e) {
				L.error(e.getMessage());
				continue;
			}
		}
	}

	@Override
	public void setListener(MsgListener l) {
		this.l = l;
	}

	public MsgListener getListener() {
		return l;
	}

}
