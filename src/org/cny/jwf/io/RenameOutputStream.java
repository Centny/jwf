package org.cny.jwf.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RenameOutputStream extends OutputStream {
	protected File tf;
	protected boolean append;
	protected OutputStream out;

	public RenameOutputStream(File tf, boolean append)
			throws FileNotFoundException {
		this.tf = tf;
		this.append = append;
		this.reopen();
	}

	private void reopen() throws FileNotFoundException {
		this.out = new BufferedOutputStream(new FileOutputStream(this.tf,
				this.append));
	}

	public void rename(File nf) throws IOException {
		synchronized (this) {
			if (this.out != null) {
				this.out.close();
				this.out = null;
			}
			if (this.tf.exists()) {
				this.tf.renameTo(nf);
			}
			this.reopen();
		}
	}

	@Override
	public void write(int b) throws IOException {
		synchronized (this) {
			this.out.write(b);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		synchronized (this) {
			this.out.write(b);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		synchronized (this) {
			this.out.write(b, off, len);
		}
	}

	@Override
	public void flush() throws IOException {
		synchronized (this) {
			this.out.flush();
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (this) {
			this.out.close();
			this.out = null;
		}
	}

}
