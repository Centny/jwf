package org.cny.jwf.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
		return sha1(in, out, null);
	}

	public static Hash sha1(InputStream in, OutputStream out,
			Donable<InputStream> done) throws NoSuchAlgorithmException,
			IOException {
		MessageDigest md = MessageDigest.getInstance("sha1");
		byte[] buf = new byte[2048];
		int rlen = 0;
		Hash hash = new Hash();
		if (out == null) {
			while ((rlen = in.read(buf)) != -1) {
				hash.length += rlen;
				md.update(buf, 0, rlen);
				if (done != null) {
					done.onProc(in, hash.length);
				}
			}
		} else {
			while ((rlen = in.read(buf)) != -1) {
				hash.length += rlen;
				md.update(buf, 0, rlen);
				out.write(buf, 0, rlen);
				if (done != null) {
					done.onProc(in, hash.length);
				}
			}
		}
		hash.hash = md.digest();
		return hash;
	}

	public static void create(String path, long size) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(path, false);
			bos = new BufferedOutputStream(fos);
			long tsize = size;
			while (tsize > 0) {
				if (tsize > 1024) {
					bos.write(new byte[1024]);
					tsize -= 1024;
				} else {
					bos.write(new byte[(int) tsize]);
					tsize -= tsize;
				}
			}
			bos.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
}
