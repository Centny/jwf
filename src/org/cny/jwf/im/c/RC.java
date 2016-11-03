package org.cny.jwf.im.c;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cny.jwf.im.IMC.MsgListener;
import org.cny.jwf.im.Msg;
import org.cny.jwf.im.PbSckIMC;
import org.cny.jwf.netw.bean.Conn;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RC implements EvnListener, MsgListener {
	private static Logger L = LoggerFactory.getLogger(RC.class);
	public Thread thr;
	public String r;
	public int rc = 0;
	public int ec = 0;
	public PbSckIMC imc;

	public RC(String host, int port) {
		this.imc = new PbSckIMC(this, this, host, port);
		this.thr = new Thread(this.imc);
		this.thr.setName("RC-" + this.imc);
		this.thr.start();
	}

	public void li(String type, String token) throws Exception {
		this.imc.wcon();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("token", token);
		args.put("ctype", type);
		Conn.Res res = imc.li(args, Conn.Res.class);
		if (res.code != 0) {
			System.err.println("login error:" + res.err);
			return;
		}
		this.r = res.res.r;

	}

	public void ur() throws Exception {
		this.imc.ur();
	}

	public void lo(String type, String token) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("token", token);
		args.put("ctype", type);
		this.imc.lo(args);
	}

	public void sms(String r, int t, String msg) throws IOException {
		this.imc.sms(new String[] { r }, t, msg.getBytes());
	}

	@Override
	public void onMsg(Msg m) {
		this.rc++;
		L.debug("receive message->{}->{}", m, new String(m.c));
	}

	@Override
	public void begCon(NetwRunnable nr) throws Exception {

	}

	@Override
	public void onCon(NetwRunnable nr, Netw nw) throws Exception {

	}

	@Override
	public void onErr(NetwRunnable nr, Throwable e) {
		this.ec++;
	}

	/**
	 * stop and wait thread exit.
	 * 
	 * @throws Exception
	 */
	public void stopw() throws Exception {
		this.imc.stop();
		this.imc.close();
		this.thr.join();
	}

}
