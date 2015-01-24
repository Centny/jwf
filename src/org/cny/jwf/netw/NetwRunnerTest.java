package org.cny.jwf.netw;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.cny.jwf.netw.NetwRunnable.EvnListener;
import org.cny.jwf.netw.NetwRunnable.MsgListener;
import org.junit.Test;

public class NetwRunnerTest {
	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
				"%-6r [%p] %c - %m%n")));
	}
	int mc = 0;

	static interface SS extends MsgListener, EvnListener {

	}

	@Test
	public void testNetw() throws Exception {
		PipedOutputStream po = new PipedOutputStream();
		PipedInputStream pi = new PipedInputStream(po);
		SS ml = new SS() {

			@Override
			public void onMsg(Bytes m) {
				System.out.println(m + "," + m.length());
				mc++;
				m.slice(1);
//				m.bys();
				m.length();
				m.offset();
				if (m.toString().equals("err")) {
					throw new RuntimeException("err");
				}
			}

			@Override
			public void onErr(Throwable e) {
				System.out.println(e);
			}
		};
		NetwRunner rw = new NetwRunner(po, pi, ml, ml);
		rw.setEvnL(ml);
		rw.setMsgL(ml);
		rw.setLimit(Netw.MAX_ML);
		po.write(NetwRunnable.H_MOD);
		po.flush();
		po.write(new byte[] { 0, 2, 'a', 'b' });
		po.flush();
		Thread thr = new Thread(rw);
		thr.start();
		//
		po.write(NetwRunnable.H_MOD);
		po.write(new byte[] { 0, 2, '1', '2' });
		po.write(NetwRunnable.H_MOD);
		po.write(new byte[] { 0, 4, '1', '2', '3', '4' });
		po.flush();
		rw.writem("abcddd".getBytes());
		// po.flush();
		while (mc < 4) {
			System.out.println("--->" + mc);
			Thread.sleep(1000);
		}
		rw.stop();
		rw.writem("abcddd".getBytes());
		// rw.stop();
		// po.close();
		System.out.println("--->");
		thr.join();

		//
		//
		System.out.println("---->01");
		po = new PipedOutputStream();
		pi = new PipedInputStream(po);
		rw = new NetwRunner(po, 102400, pi, ml, ml);
		thr = new Thread(rw);
		thr.start();
		rw.writem("err".getBytes());
		thr.join();
		//
		System.out.println("---->02");
		po = new PipedOutputStream();
		pi = new PipedInputStream(po);
		rw = new NetwRunner(po, 102400, pi, ml, ml);
		thr = new Thread(rw);
		thr.start();
		Thread.sleep(100);
		po.write(new byte[] { 1, 2, 3 });
		po.write(new byte[] { 0, 2, '1', '2', '3', '4', '5' });
		po.flush();
		rw.stop();
		rw.writem("ssss".getBytes());
		rw.writem("ssss".getBytes());
		rw.writem("ssss".getBytes());
		thr.join();
		//
		System.out.println("---->03");
		po = new PipedOutputStream();
		pi = new PipedInputStream(po);
		rw = new NetwRunner(po, 102400, pi, ml, ml);
		thr = new Thread(rw);
		thr.start();
		Thread.sleep(100);
		po.close();
		pi.close();
		thr.join();
		//
		try {
			rw.setLimit(10);
			rw.writem(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
		} catch (Exception e) {

		}
		try {
			rw.setLimit(9);
			List<byte[]> ms = new ArrayList<byte[]>();
			ms.add(new byte[] { 1, 2, 3, 4, 5 });
			ms.add(new byte[] { 1, 2, 3, 4, 5 });
			rw.writem(ms);
		} catch (Exception e) {

		}
		try {
			rw.setLimit(9);
			List<byte[]> ms = new ArrayList<byte[]>();
			rw.writem(ms);
		} catch (Exception e) {

		}
		try {
			rw.setLimit(9);
			List<byte[]> ms = null;
			rw.writem(ms);
		} catch (Exception e) {

		}
		Bytes.bstr(new byte[] {});
	}
}
