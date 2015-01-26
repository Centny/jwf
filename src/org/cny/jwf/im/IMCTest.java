package org.cny.jwf.im;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.cny.jwf.im.IMC.MsgListener;
import org.cny.jwf.netw.NetwMv;
import org.cny.jwf.netw.NetwRWbase;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.cny.jwf.netw.r.NetwVer;
import org.junit.Test;

public class IMCTest {
	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
				"LL->%-6r [%p] %c - %m%n")));
	}
	int mc = 0;

	public static class Br extends IMC {
		PipedOutputStream po;
		PipedInputStream pi;

		public Br(MsgListener l) throws IOException {
			super(l);
			this.po = new PipedOutputStream();
			this.pi = new PipedInputStream(this.po);
		}

		@Override
		public Cmd V2B(NetwVer nv, Object o) {
			// System.out.println("--->V2B");
			try {
				return new NetwMv(nv, o.toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T B2V(Cmd bys, Class<T> cls) {
			// System.out.println("--->B2V");
			try {
				if (cls.equals(Msg.class)) {
					return (T) new Msg(new String[] { "aa" }, (byte) 0,
							"message-abc".getBytes());
				} else {
					return (T) new String(bys.bys(), bys.offset(), bys.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public void onCon(NetwRunnable nr, Netw nw) {
		}

		@Override
		public void onErr(NetwRunnable nr, Throwable e) {
			System.err.println(e.getMessage());
		}

		@Override
		protected NetwBase createNetwBase() {
			NetwBase nb = new NetwRWbase(this.po, 1024, this.pi, 102400);
			nb.setLimit(100000);
			try {
				Thread.sleep(300);
			} catch (Exception e) {

			}
			return nb;
		}

	}

	public static class BrErr extends Br {

		public BrErr(MsgListener l) throws IOException {
			super(l);
		}

		@Override
		protected NetwBase createNetwBase() {
			throw new RuntimeException();
			// return super.createNetwBase();
		}

	}

	@Test
	public void testIm() throws Exception {
		Br br = new Br(new MsgListener() {

			@Override
			public void onMsg(Msg m) {
				System.out.println("Msg->" + m.r);
				System.out.println("Msg->" + new String(m.c));
			}
		});
		Thread thr = new Thread(br);
		thr.start();
		br.wcon();
		br.writem(new String[] { "a" }, (byte) 0, "abc".getBytes());
		br.li("abc-li", new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {
				System.out.println("LI->" + m + "," + m.toBs());
			}
		});
		br.lo("abc-lo", new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {
				System.out.println("LI->" + m);
			}
		});
		br.lo("err", new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {
				throw new RuntimeException();
			}
		});

		// mod error
		br.po.write(new byte[] { 1, 2, 3 });
		br.po.write(new byte[] { 0, 2 });
		// RC data len error
		br.po.write(Netw.H_MOD);
		br.po.write(new byte[] { 0, 2, IMC.MK_NRC, 100 });
		// RC mark error
		br.po.write(Netw.H_MOD);
		br.po.write(new byte[] { 0, 3, IMC.MK_NRC, 100, 2 });
		//
		Thread.sleep(200);
		br.stop();

		// /
		br.writem(new String[] { "a" }, (byte) 0, "abc".getBytes());
		thr.join();
	}

	@Test
	public void testErr() throws Exception {
		Br br = new BrErr(new MsgListener() {

			@Override
			public void onMsg(Msg m) {
			}
		});
		Thread thr = new Thread(br);
		thr.start();
		thr.join();
	}

	@Test
	public void testNetw() throws Exception {

		// SS ml = new SS() {
		//
		// @Override
		// public void onCmd(NetwRunnable nr, Cmd m) {
		// System.out.println(m + "," + m.length());
		// mc++;
		// m.slice(1);
		// // m.bys();
		// m.length();
		// m.offset();
		// m.get(0);
		// NetwVable vv = (NetwVable) m;
		// if (vv.V(String.class).equals("err")) {
		// throw new RuntimeException("err");
		// }
		// }
		//
		// @Override
		// public void onErr(NetwRunnable nr, Throwable e) {
		// System.out.println(e);
		// }
		//
		// @Override
		// public void onCon(NetwRunnable nr, Netw nw) {
		// nw.setLimit(Netw.MAX_ML);
		// }
		// };
		// NetwRunner nr = new Br(po, 10240, pi, ml, ml);
		// Thread thr = new Thread(nr);
		// thr.start();
		// nr.wcon();
		// NetwVer rw = (NetwVer) nr.netw();
		// po.write(Netw.H_MOD);
		// po.flush();
		// po.write(new byte[] { 0, 2, 'a', 'b' });
		// po.flush();
		// //
		// po.write(Netw.H_MOD);
		// po.write(new byte[] { 0, 2, '1', '2' });
		// po.write(Netw.H_MOD);
		// po.write(new byte[] { 0, 4, '1', '2', '3', '4' });
		// po.flush();
		// rw.writem("abcddd".getBytes());
		// rw.writev("---->");
		// List<byte[]> tms = new ArrayList<byte[]>();
		// tms.add("sssss".getBytes());
		// rw.writem(tms);
		// // po.flush();
		// while (mc < 6) {
		// System.out.println("--->" + mc);
		// Thread.sleep(1000);
		// }
		// nr.stop();
		// rw.writem("abcddd".getBytes());
		// // rw.stop();
		// // po.close();
		// System.out.println("--->");
		// thr.join();
		//
		// //
		// //
		// System.out.println("---->01");
		// po = new PipedOutputStream();
		// pi = new PipedInputStream(po);
		// nr = new Br(po, 102400, pi, ml, ml);
		// thr = new Thread(nr);
		// thr.start();
		// nr.wcon();
		// rw = (NetwVer) nr.netw();
		// rw.writem("err".getBytes());
		// thr.join();
		// //
		// System.out.println("---->02");
		// po = new PipedOutputStream();
		// pi = new PipedInputStream(po);
		// nr = new Br(po, 102400, pi, ml, ml);
		// thr = new Thread(nr);
		// thr.start();
		// nr.wcon();
		// rw = (NetwVer) nr.netw();
		// Thread.sleep(100);
		// po.write(new byte[] { 1, 2, 3 });
		// po.write(new byte[] { 0, 2, '1', '2', '3', '4', '5' });
		// po.flush();
		// nr.stop();
		// rw.writem("ssss".getBytes());
		// rw.writem("ssss".getBytes());
		// rw.writem("ssss".getBytes());
		// thr.join();
		// //
		// System.out.println("---->03");
		// po = new PipedOutputStream();
		// pi = new PipedInputStream(po);
		// nr = new Br(po, 102400, pi, ml, ml);
		// thr = new Thread(nr);
		// thr.start();
		//
		// Thread.sleep(100);
		// po.close();
		// pi.close();
		// thr.join();
		// //
		// try {
		// rw.setLimit(10);
		// rw.writem(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
		// } catch (Exception e) {
		// }
		// try {
		// rw.setLimit(10);
		// rw.writem(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, 0, 13);
		// } catch (Exception e) {
		// }
		// try {
		// rw.setLimit(9);
		// List<byte[]> ms = new ArrayList<byte[]>();
		// ms.add(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
		// rw.writem(ms);
		// } catch (Exception e) {
		// }
		// try {
		// rw.setLimit(9);
		// List<byte[]> ms = new ArrayList<byte[]>();
		// ms.add(new byte[] { 1, 2, 3, 4, 5 });
		// ms.add(new byte[] { 1, 2, 3, 4, 5 });
		// rw.writem(ms);
		// } catch (Exception e) {
		// }
		// try {
		// rw.setLimit(9);
		// List<byte[]> ms = new ArrayList<byte[]>();
		// rw.writem(ms);
		// } catch (Exception e) {
		// }
		// try {
		// rw.setLimit(9);
		// List<byte[]> ms = null;
		// rw.writem(ms);
		// } catch (Exception e) {
		// }
		// NetwM.bstr(new byte[] {});
	}

	public void testRR() {
		// SS ml = new SS() {
		//
		// @Override
		// public void onMsg(NetwRunnable nr, Msg m) {
		// System.out.println(m + "," + m.length());
		// mc++;
		// m.slice(1);
		// // m.bys();
		// m.length();
		// m.offset();
		// m.get(0);
		// NetwVable vv = (NetwVable) m;
		// if (vv.V(String.class).equals("err")) {
		// throw new RuntimeException("err");
		// }
		// }
		//
		// @Override
		// public void onErr(NetwRunnable nr, Throwable e) {
		// System.out.println(e);
		// }
		//
		// @Override
		// public void onCon(NetwRunnable nr, Netw nw) {
		// nw.setLimit(Netw.MAX_ML);
		// }
		// };
	}
}
