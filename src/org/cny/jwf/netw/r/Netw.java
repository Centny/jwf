package org.cny.jwf.netw.r;

import java.io.IOException;
import java.util.List;

public interface Netw {
	public static final byte[] H_MOD = new byte[] { '^', '~', '^' };
	public static final int MAX_ML = 102400;

	void setLimit(int l);

	int readw(byte[] buf) throws IOException;

	int readw(byte[] buf, int off, int len) throws IOException;

	byte[] readm() throws IOException, ModException;

	Msg readM() throws IOException, ModException;

	void writem(byte[] m) throws IOException;

	void writem(byte[] m, int offs, int len) throws IOException;

	void writem(List<byte[]> ms) throws IOException;

	void writeM(Msg b) throws IOException;

	public static class ModException extends Exception {

		private static final long serialVersionUID = 6734359542773586037L;

		public ModException(String message) {
			super(message);
		}

	}
}
