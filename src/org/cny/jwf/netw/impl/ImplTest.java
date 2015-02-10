package org.cny.jwf.netw.impl;

import java.io.IOException;
import java.util.List;

import org.cny.jwf.netw.NetwM;
import org.cny.jwf.netw.NetwRWv;
import org.cny.jwf.netw.impl.RC.CmdL;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.cny.jwf.netw.r.NetwVer;
import org.junit.Test;

public class ImplTest {

	@Test
	public void testImpl() {
		OBDH obdh = new OBDH();
		QueueH qh = new QueueH();
		qh.addh(new OBDH());
		obdh.addh((byte) 0, qh);
		obdh.onCmd(null, new NetwM(null, new byte[] { 0, 2, 3, 4, 5 }));
		try {
			new RC(new NetwRWv(null) {

				@Override
				public Cmd V2B(NetwVer nv, Object o) {
					return null;
				}

				@Override
				public <T> T B2V(Cmd bys, Class<T> cls) {
					return null;
				}
			}).exec((byte) 0, new NetwM(null, new byte[] { 1 }),
					(CmdListener) null);
		} catch (Exception e) {

		}
		new OBDC(new NetwBase() {

			@Override
			public void writeM(List<Cmd> ms) throws IOException {

			}

			@Override
			public void setLimit(int l) {

			}

			@Override
			public int readw(byte[] buf, int off, int len) throws IOException {
				return 0;
			}
		}, (byte) 0).setLimit(1);
	}

	@Test
	public void testRc() throws Exception {

		CmdL cl = new RC.CmdL() {

			@Override
			public void setM(Cmd m) {
				throw new RuntimeException();
			}

		};
		try {
			cl.onCmd(null, null);
		} catch (Exception e) {

		}
		RC rc;
		rc = new RC(null) {

			@Override
			public void exec(byte m, Cmd args, CmdListener l) throws Exception {
				throw new RuntimeException();
			}

		};
		try {
			rc.exec((byte) 0, (Cmd) null);
		} catch (Exception e) {

		}

		rc = new RC(null) {

			@Override
			public void exec(byte m, Cmd args, CmdListener l) throws Exception {
				new LThr(l).start();
			}

		};
		try {
			rc.exec((byte) 0, (Cmd) null);
		} catch (Exception e) {

		}

		rc = new RC(new Netw() {

			@Override
			public void writeM(List<Cmd> ms) throws IOException {

			}

			@Override
			public void setLimit(int l) {

			}

			@Override
			public int readw(byte[] buf, int off, int len) throws IOException {
				return 0;
			}

			@Override
			public void writem(List<byte[]> ms) throws IOException {

			}

			@Override
			public void writem(byte[] m, int offs, int len) throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void writem(byte[] m) throws IOException {

			}

			@Override
			public void writeM(Cmd b) throws IOException {

			}

			@Override
			public int readw(byte[] buf) throws IOException {
				return 0;
			}

			@Override
			public byte[] readm() throws IOException, ModException {
				return null;
			}

			@Override
			public Cmd readM() throws IOException, ModException {
				return null;
			}

			@Override
			public Cmd newM(byte[] m, int off, int len) {
				return null;
			}
		}) {

			@Override
			protected short next_ei() {
				throw new RuntimeException();
			}

		};
		try {
			rc.exec((byte) 0, (Cmd) null, new CmdL());
		} catch (Exception e) {

		}
		try {
			rc.onCmd(null, null);
		} catch (Exception e) {

		}
		try {
			rc.onCmd(null, new NetwM(null, new byte[1]));
		} catch (Exception e) {

		}
		try {
			rc.onCmd(null, new NetwM(null, new byte[2]));
		} catch (Exception e) {

		}
		rc.hs.put((short) 0, new CmdListener() {

			@Override
			public void onCmd(NetwRunnable nr, Cmd m) {
				throw new RuntimeException();
			}
		});
		try {
			rc.onCmd(null, new NetwM(null, new byte[2]));
		} catch (Exception e) {

		}

		rc.hs.put((short) 0, new CmdL() {

		});
		try {
			rc.clear(new Exception());
		} catch (Exception e) {

		}
		rc.hs.put((short) 0, new CmdL() {

			@Override
			public void setE(Exception e) {
				throw new RuntimeException();
			}

		});
		try {
			rc.clear(null);
		} catch (Exception e) {

		}
	}

	public class LThr extends Thread {
		CmdListener l;

		public LThr(CmdListener l) {
			this.l = l;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				CmdL cl = (CmdL) l;
				cl.setE(new Exception());
				cl.onCmd(null, null);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
