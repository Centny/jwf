package org.cny.jwf.im;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.cny.jwf.im.pb.Msg.ImMsg;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.junit.Test;

public class PbSckIMCTest {
	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
				"LL->%-6r [%p] %c - %m%n")));
	}
	PbSckIMC imc;

	@Test
	public void testSIMC() throws Exception {
		imc = new PbSckIMC(new EvnListener() {

			@Override
			public void onErr(NetwRunnable nr, Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onCon(NetwRunnable nr, Netw nw) {

			}
		}, new IMC.MsgListener() {

			@Override
			public void onMsg(ImMsg m) {
				System.err.println(new String(m.getC().toStringUtf8()));
				try {
					imc.writem(new String[] { m.getS() }, 0, ("收到-->" + m
							.getC().toStringUtf8()).getBytes());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "127.0.0.1", 9891);
		Thread thr = new Thread(imc);
		thr.start();
		imc.wcon();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("token", "abc");
		Cmd m;
		m = imc.li(args);
		System.err.println(m.toString());
		imc.writem(new String[] { "U-2" }, 0, "abcc节能".getBytes("UTF-8"));
		// Thread.sleep(300);
		m = imc.lo(args);
		// imc.sck.close();
		// System.err.println(m.toString());
		// Thread.sleep(1000);
		thr.join();
	}
}
