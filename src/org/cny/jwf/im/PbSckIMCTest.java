package org.cny.jwf.im;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.cny.jwf.netw.NetwM;
import org.cny.jwf.netw.bean.Con;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbSckIMCTest {
	// static {
	// Logger rootLogger = Logger.getRootLogger();
	// rootLogger.setLevel(Level.DEBUG);
	// rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
	// "LL->%-6r [%p] %c - %m%n")));
	// }
	PbSckIMC imc;
	int msg_c = 0;

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

			@Override
			public void begCon(NetwRunnable nr) throws Exception {

			}

		}, new IMC.MsgListener() {

			@Override
			public void onMsg(Msg m) {
				// System.err.println("Rec=>" + m.getC().toStringUtf8());
				msg_c++;
				try {
					imc.sms(new String[] { m.s }, 0, "C-中文->".getBytes());
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
		args.put("ctype", "20");

		Con.Res res = imc.li(args, Con.Res.class);
		Assert.assertEquals(0, res.code);
		imc.li(args);
		imc.li(args, new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {

			}
		});
		imc.sms(new String[] { res.res.r }, 0, "abcc节能".getBytes("UTF-8"));
		// Thread.sleep(300);
		Thread.sleep(1000);
		imc.lo(args);
		imc.lo(args, Con.Res.class);
		imc.lo(args, new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {

			}
		});
		imc.rcClear(new Exception());
		imc.close();
		imc.close();
		// System.err.println(m.toString());
		Thread.sleep(1000);
		thr.join();
		System.err.println("Rec " + msg_c + " message");

		try {
			imc.B2V(new NetwM(null, new byte[11]), null);
		} catch (Exception e) {

		}
		try {
			imc.create(null, (byte) 0, (byte[]) null);
		} catch (Exception e) {

		}
		try {
			imc.create(new String[] {}, (byte) 0, (byte[]) null);
		} catch (Exception e) {

		}
		try {
			imc.create(new String[] { "ss" }, (byte) 0, (byte[]) null);
		} catch (Exception e) {

		}
		try {
			imc.create(new String[] { "ss" }, (byte) 0, new byte[] {});
		} catch (Exception e) {

		}
		imc.rc = null;
		imc.rcClear(new Exception());
	}

	@Test
	public void testLog() {
		Logger l = LoggerFactory.getLogger(this.getClass());
		l.debug("DEBUG----->");
		l.info("DEBUG----->");
		l.error("DEBUG----->");
	}
}
