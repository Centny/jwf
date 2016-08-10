package org.cny.jwf.im.c;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.cny.jwf.im.IMC.MsgListener;
import org.cny.jwf.im.Msg;
import org.cny.jwf.im.PbSckIMC;
import org.cny.jwf.netw.bean.Con;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;

public class C implements EvnListener, MsgListener {
	protected Thread thr;
	protected PbSckIMC imc;
	protected Map<String, Integer> msg_r = new HashMap<String, Integer>();
	protected Map<String, Integer> msg_s = new HashMap<String, Integer>();
	protected String r;
	protected boolean running;
	protected String wd;

	public C(String host, int port) {
		this.imc = new PbSckIMC(this, this, host, port);
	}

	@Override
	public void onMsg(Msg m) {
		System.out.println("R:" + m.toString());
		Integer c = this.msg_r.get(m.s);
		if (c == null) {
			this.msg_r.put(m.s, 1);
		} else {
			c += 1;
			this.msg_r.put(m.s, c);
		}
	}

	@Override
	public void begCon(NetwRunnable nr) throws Exception {

	}

	@Override
	public void onCon(NetwRunnable nr, Netw nw) throws Exception {

	}

	@Override
	public void onErr(NetwRunnable nr, Throwable e) {
		e.printStackTrace();
	}

	public void run_s() throws Exception {
		this.running = true;
		Random r = new Random();
		while (running) {
			Thread.sleep(1000);
			String[] subs = new File(this.wd, this.r).list();
			if (subs.length < 1) {
				continue;
			}
			int len = r.nextInt(subs.length);
			for (int i = 0; i < len; i++) {
				this.imc.sms(subs, 0, ("Abc" + i).getBytes());
			}
			for (String sub : subs) {
				Integer c = this.msg_s.get(sub);
				if (c == null) {
					this.msg_s.put(sub, len);
				} else {
					c += len;
					this.msg_s.put(sub, c);
				}
			}
		}
	}

	public void start(String wd, String token) throws Exception {
		
		this.thr = new Thread(imc);
		this.thr.start();
		imc.wcon();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("token", token);
		args.put("ctype", "10");
		Con.Res res = imc.li(args, Con.Res.class);
		if (res.code != 0) {
			System.err.println("login error:" + res.err);
			return;
		}
		this.r = res.res.r;
		imc.ur();
		this.wd = wd;
		File wd_f = new File(wd);
		if (!wd_f.exists()) {
			wd_f.mkdirs();
		}
		Thread sthr = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					run_s();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		sthr.start();
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			while (!"E".equals(scanner.nextLine())) {
			}
		} catch (Exception e) {

		}
		if (scanner != null) {
			scanner.close();
		}
		this.running = false;
		//
		this.imc.stop();
		this.thr.join();
		sthr.join();
	}

	public static void main(String[] args) {
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		String token = args[3];
		String wd = args[4];
		try {
			new C(host, port).start(wd, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
