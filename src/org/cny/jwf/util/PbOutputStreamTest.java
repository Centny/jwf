package org.cny.jwf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.cny.jwf.im.pb.Msg;
import org.junit.Test;

public class PbOutputStreamTest {

	@Test
	public void testWrite() throws IOException {
		Msg.KV.Builder b = Msg.KV.newBuilder();
		b.setKey("sss");
		b.setVal("ss");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PbOutputStream pos;
		pos = new PbOutputStream(baos, 1024);
		pos.write(b.build());
		pos.close();
		pos = new PbOutputStream(new ByteArrayOutputStream() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.io.ByteArrayOutputStream#write(byte[], int, int)
			 */
			@Override
			public synchronized void write(byte[] b, int off, int len) {
				throw new RuntimeException();
			}

		}, 2);
		try {
			pos.write(b.build());
		} catch (Exception e) {

		}
	}
}
