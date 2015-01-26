package org.cny.jwf.netw.impl;

import java.io.IOException;
import java.util.List;

import org.cny.jwf.netw.NetwM;
import org.cny.jwf.netw.NetwRWv;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwBase;
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
			}).exec((byte) 0, new NetwM(null, new byte[] { 1 }), null);
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

}
