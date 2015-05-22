package org.cny.jwf.netw;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.Netw.ModException;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwVer;
import org.junit.Test;

public class NetwTest {

	@Test
	public void testNetw() throws Exception {
		PipedOutputStream po = new PipedOutputStream();
		PipedInputStream pi = new PipedInputStream(po);
		NetwBase nb = new NetwRWbase(po, 1024, pi, 10240);
		NetwRWv rwv = new NetwRWv(nb) {

			@Override
			public Cmd V2B(NetwVer nv, Object o) {
				return null;
			}

			@Override
			public <T> T B2V(Cmd bys, Class<T> cls) {
				return null;
			}
		};
		rwv.writem(new byte[] { 1 });
		try {
			rwv.setLimit(1);
			rwv.writem(new byte[] { 1, 2 });
		} catch (Exception e) {

		}
		List<byte[]> ms = null;
		try {
			ms = new ArrayList<byte[]>();
			rwv.writem(ms);
		} catch (Exception e) {
		}
		try {
			ms = null;
			rwv.writem(ms);
		} catch (Exception e) {
		}
		try {
			ms = new ArrayList<byte[]>();
			ms.add(new byte[] { 1, 2 });
			ms.add(new byte[] { 1, 2 });
			rwv.setLimit(1024);
			rwv.writem(ms);
			rwv.setLimit(3);
			rwv.writem(ms);
		} catch (Exception e) {
		}
		try {
			nb.writeM(null);
		} catch (Exception e) {
		}
		try {
			nb.writeM(new ArrayList<Cmd>());
		} catch (Exception e) {
		}
		//
		//
		NetwM m = new NetwM(rwv, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
		m.setLimit(1024);
		assertEquals(m.length() - 2, m.slice(2).length());
		NetwM m2 = (NetwM) m.slice(5);
		m2.reset(2, 3);
		try {
			m.slice(20);
		} catch (Exception e) {
		}
		try {
			m.slice(20, 1);
		} catch (Exception e) {
		}
		try {
			m.slice(5).reset(20, 1);
		} catch (Exception e) {
		}
		m.V(String.class);
	}

	@Test
	public void testRw() throws Exception {
		PipedOutputStream po = new PipedOutputStream();
		PipedInputStream pi = new PipedInputStream(po);
		NetwBase nb = new NetwRWbase(po, 1024, pi, 10240);
		NetwRWv rwv = new NetwRWv(nb) {

			@Override
			public Cmd V2B(NetwVer nv, Object o) {
				return null;
			}

			@Override
			public <T> T B2V(Cmd bys, Class<T> cls) {
				return null;
			}
		};
		NetwM m = new NetwM(rwv, new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
		byte[] buf = new byte[6];
		m.writem(new byte[] { 1 });
		m.readw(buf);
		System.err.println(NetwM.bstr(buf));
		m.writem(new byte[] { 1 }, 0, 1);
		m.readw(buf, 0, 6);
		System.err.println(NetwM.bstr(buf));
		m.writem(new byte[] { 1 });
		assertEquals(1, m.readm().length);
		//
		m.writeM(new NetwM(null, new byte[] { 1, 2, 3, 4 }));
		Cmd tm = m.readM();
		assertEquals(4, tm.length());
		//
		List<byte[]> bys = new ArrayList<byte[]>();
		bys.add(new byte[] { 1, 2 });
		bys.add(new byte[] { 3, 4 });
		m.writem(bys);
		tm = m.readM();
		assertEquals(4, tm.length());
		//
		List<Cmd> cs = new ArrayList<Cmd>();
		cs.add(new NetwM(null, new byte[] { 1, 2 }));
		cs.add(new NetwM(null, new byte[] { 1, 2 }));
		m.writeM(cs);
		tm = m.readM();
		assertEquals(4, tm.length());

	}

	@Test
	public void testNew() throws Exception {
		new RWRunner(null, null) {

			@Override
			protected Netw createNetw() throws ModException {
				return null;
			}
		}.netw();
		new RWRunnerv(null, null) {

			@Override
			public Cmd V2B(NetwVer nv, Object o) {
				return null;
			}

			@Override
			public <T> T B2V(Cmd bys, Class<T> cls) {
				return null;
			}

			@Override
			protected NetwBase createNetwBase() {
				return null;
			}
		}.createNetw();
	}

	@Test
	public void testErr() throws IOException {
		try {
			new NetwRWbase(new ByteArrayOutputStream(), 10240,
					new ByteArrayInputStream(new byte[] {}) {

						@Override
						public synchronized int read(byte[] b, int off, int len) {
							return -1;
						}

					}, 1024).readw(new byte[100], 0, 100);
		} catch (Exception e) {

		}
		new NetwMv(null, new byte[1]);

		NetwRW rw;
		rw = new NetwRW(null) {

			@Override
			public Cmd newM(byte[] m, int off, int len) {
				return null;
			}

			@Override
			public int readw(byte[] buf) throws IOException {
				return buf.length;
			}
		};
		try {
			rw.readm();
		} catch (Exception e) {

		}
		rw = new NetwRW(null) {

			@Override
			public Cmd newM(byte[] m, int off, int len) {
				return null;
			}

			@Override
			public int readw(byte[] buf) throws IOException {
				buf[0] = '^';
				buf[1] = '~';
				buf[2] = '^';
				buf[3] = 0;
				buf[4] = 0;
				return buf.length;
			}
		};
		try {
			rw.readm();
		} catch (Exception e) {

		}
	}

	@Test
	public void testErr2() {
		NetwM.bstr(new byte[] {}, 0, 1);
		NetwM.bstr(new byte[] { 1 }, 11, 1);
		NetwM.bstr(new byte[] { 1 }, -1, 1);
		NetwM.bstr(new byte[] { 1 }, 0, -1);
		NetwM nm = new NetwM(null, new byte[10]);
		nm.toBs();
		try {
			nm.reset(-1, 0);
		} catch (Exception e) {

		}
		try {
			nm.reset(101, 0);
		} catch (Exception e) {

		}
		try {
			nm.reset(0, -1);
		} catch (Exception e) {

		}
		try {
			nm.reset(0, 100);
		} catch (Exception e) {

		}
		try {
			nm.reset(8, 8);
		} catch (Exception e) {

		}
	}

	@Test
	public void testR() throws Exception {
//		FileInputStream fi = new FileInputStream("/tmp/tt.data");
		//[94, 126, 94, -93, 100]
		byte[] bys = new byte[] { (byte) -93, (byte) 100 };
//		fi.read(bys);
		int len = 0;
		len += (bys[0]&0xff) << 8;
		len += (bys[1]&0xff);
		System.err.println(len);
//		fi.close();
//		-23708
	}
}
