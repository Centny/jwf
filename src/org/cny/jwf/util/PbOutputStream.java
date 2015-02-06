package org.cny.jwf.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.protobuf.GeneratedMessage;

/**
 * the protobuf output stream extends BufferedOutputStream. <br/>
 * it will write one message by [message length][message data]
 * 
 * @author cny
 *
 */
public class PbOutputStream extends BufferedOutputStream {

	/**
	 * default constructor by output stream and buffer size.
	 * 
	 * @param out
	 *            target output stream.
	 * @param size
	 *            buffer size.
	 */
	public PbOutputStream(OutputStream out, int size) {
		super(out, size);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		synchronized (this) {
			byte[] head = new byte[2];
			head[0] = (byte) (len >> 8);
			head[1] = (byte) len;
			super.write(head, 0, 2);
			super.write(b, off, len);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		this.write(b, 0, b.length);
	}

	/**
	 * write one message to output stream.
	 * 
	 * @param gm
	 *            the protobuf message.
	 * @throws IOException
	 */
	public void write(GeneratedMessage gm) throws IOException {
		this.write(gm.toByteArray());
	}
}
