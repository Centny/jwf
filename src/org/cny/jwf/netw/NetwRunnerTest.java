package org.cny.jwf.netw;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.cny.jwf.netw.r.NetwRunnable.MsgListener;
import org.cny.jwf.netw.r.NetwRunner;
import org.cny.jwf.netw.r.NetwVer;
import org.cny.jwf.netw.r.NetwVer.NetwVable;
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

	static class Br extends RWRunnerv {

		public Br(OutputStream out, int sz, InputStream in, MsgListener msg,
				EvnListener evn) {
			super(out, sz, in, msg, evn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Msg V2B(NetwVer nv, Object o) {
			System.out.println("--->V2B");
			try {
				return new NetwMv(nv, o.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T B2V(Msg bys, Class<T> cls) {
			System.out.println("--->B2V");
			try {
				return (T) new String(bys.bys(), bys.offset(), bys.length());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	@Test
	public void testNetw() throws Exception {
		PipedOutputStream po = new PipedOutputStream();
		PipedInputStream pi = new PipedInputStream(po);
		SS ml = new SS() {

			@Override
			public void onMsg(NetwRunnable nr, Msg m) {
				System.out.println(m + "," + m.length());
				mc++;
				m.slice(1);
				// m.bys();
				m.length();
				m.offset();
				m.get(0);
				NetwVable vv = (NetwVable) m;
				if (vv.V(String.class).equals("err")) {
					throw new RuntimeException("err");
				}
			}

			@Override
			public void onErr(NetwRunnable nr, Throwable e) {
				System.out.println(e);
			}

			@Override
			public void onCon(NetwRunnable nr, Netw nw) {
				nw.setLimit(Netw.MAX_ML);
			}
		};
		NetwRunner nr = new Br(po, 10240, pi, ml, ml);
		Thread thr = new Thread(nr);
		thr.start();
		nr.wcon();
		NetwVer rw = (NetwVer) nr.netw();
		po.write(Netw.H_MOD);
		po.flush();
		po.write(new byte[] { 0, 2, 'a', 'b' });
		po.flush();
		//
		po.write(Netw.H_MOD);
		po.write(new byte[] { 0, 2, '1', '2' });
		po.write(Netw.H_MOD);
		po.write(new byte[] { 0, 4, '1', '2', '3', '4' });
		po.flush();
		rw.writem("abcddd".getBytes());
		rw.writev("---->");
		List<byte[]> tms = new ArrayList<byte[]>();
		tms.add("sssss".getBytes());
		rw.writem(tms);
		// po.flush();
		while (mc < 6) {
			System.out.println("--->" + mc);
			Thread.sleep(1000);
		}
		nr.stop();
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
		nr = new Br(po, 102400, pi, ml, ml);
		thr = new Thread(nr);
		thr.start();
		nr.wcon();
		rw = (NetwVer) nr.netw();
		rw.writem("err".getBytes());
		thr.join();
		//
		System.out.println("---->02");
		po = new PipedOutputStream();
		pi = new PipedInputStream(po);
		nr = new Br(po, 102400, pi, ml, ml);
		thr = new Thread(nr);
		thr.start();
		nr.wcon();
		rw = (NetwVer) nr.netw();
		Thread.sleep(100);
		po.write(new byte[] { 1, 2, 3 });
		po.write(new byte[] { 0, 2, '1', '2', '3', '4', '5' });
		po.flush();
		nr.stop();
		rw.writem("ssss".getBytes());
		rw.writem("ssss".getBytes());
		rw.writem("ssss".getBytes());
		thr.join();
		//
		System.out.println("---->03");
		po = new PipedOutputStream();
		pi = new PipedInputStream(po);
		nr = new Br(po, 102400, pi, ml, ml);
		thr = new Thread(nr);
		thr.start();
		nr.wcon();
		rw = (NetwVer) nr.netw();
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
			rw.setLimit(10);
			rw.writem(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, 0, 13);
		} catch (Exception e) {
		}
		try {
			rw.setLimit(9);
			List<byte[]> ms = new ArrayList<byte[]>();
			ms.add(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
			rw.writem(ms);
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
		NetwM.bstr(new byte[] {});
	}
}
