package org.cny.jwf.netw.r;

import java.io.EOFException;
import java.net.SocketException;

import org.cny.jwf.netw.r.Netw.ModException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NetwRunner implements NetwRunnable {
	private static Logger L = LoggerFactory.getLogger(NetwRunner.class);
	//
	protected CmdListener msgl;
	protected EvnListener evnl;
	protected boolean running;

	public NetwRunner(CmdListener msg, EvnListener evn) {
		this.msgl = msg;
		this.evnl = evn;
	}

	@Override
	public void run() {
		L.debug("starting running Netw");
		try {
			this.run_c();
		} catch (Throwable e) {
			L.error("running err:", e);
		}
		this.running = false;
		L.debug("Netw stopped");
	}

	public void stop() {
		this.running = false;
	}

	public void run_c() throws Exception {
		this.evnl.begCon(this);
		Netw nw = this.createNetw();
		this.evnl.onCon(this, nw);
		this.running = true;
		while (this.running) {
			try {
				Cmd cmd = nw.readM();
				// L.debug("read on command:{}", cmd.toBs());
				this.msgl.onCmd(this, cmd);
			} catch (SocketException e) {
				this.evnl.onErr(this, e);
				this.running = false;
				L.debug("run_c ->SocketException:{}", e.getMessage());
			} catch (EOFException e) {
				this.evnl.onErr(this, e);
				this.running = false;
				L.debug("run_c ->EOFException:{}", e.getMessage());
			} catch (ModException e) {
				L.error(e.getMessage());
				L.debug("run_c ->ModException:{}", e.getMessage());
			} catch (Exception e) {
				this.evnl.onErr(this, e);
				L.debug("run_c ->Exception:{}", e.getMessage());
			}
		}
		this.running = false;
	}

	public void wcon() throws InterruptedException {
		while (true) {
			if (this.running) {
				break;
			} else {
				Thread.sleep(200);
			}
		}
	}

	protected abstract Netw createNetw() throws Exception;
}
