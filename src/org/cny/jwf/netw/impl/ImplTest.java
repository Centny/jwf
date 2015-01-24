package org.cny.jwf.netw.impl;

import org.cny.jwf.netw.Bytes;
import org.junit.Test;

public class ImplTest {

	@Test
	public void testImpl() {
		OBDH obdh = new OBDH();
		QueueH qh = new QueueH();
		qh.addh(new OBDH());
		obdh.addh((byte) 0, qh);
		obdh.onMsg(new Bytes(new byte[] { 0, 2, 3, 4, 5 }));

	}
}
