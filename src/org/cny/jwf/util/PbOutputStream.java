package org.cny.jwf.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.protobuf.GeneratedMessage;

public class PbOutputStream extends BufferedOutputStream {

	public PbOutputStream(OutputStream out, int size) {
		super(out, size);
	}

	// @Override
	// public synchronized void write(int b) throws IOException {
	// // TODO Auto-generated method stub
	// super.write(b);
	// }

	@Override
	public synchronized void write(byte[] b, int off, int len)
			throws IOException {
		byte[] head = new byte[2];
		head[0] = (byte) (len >> 8);
		head[1] = (byte) len;
		super.write(head, 0, 2);
		super.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		this.write(b, 0, b.length);
	}

	public void write(GeneratedMessage gm) throws IOException {
		this.write(gm.toByteArray());
	}
}
