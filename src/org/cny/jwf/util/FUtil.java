package org.cny.jwf.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FUtil {
	public static class Hash {
		public byte[] hash = null;
		public long length = 0;
	}

	public static Hash sha1(InputStream in, OutputStream out)
			throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("sha1");
		byte[] buf = new byte[2048];
		int rlen = 0;
		Hash hash = new Hash();
		if (out == null) {
			while ((rlen = in.read(buf)) != -1) {
				hash.length += rlen;
				md.update(buf, 0, rlen);
			}
		} else {
			while ((rlen = in.read(buf)) != -1) {
				hash.length += rlen;
				md.update(buf, 0, rlen);
				out.write(buf, 0, rlen);
			}
		}
		hash.hash = md.digest();
		return hash;
	}
}
