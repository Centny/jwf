package org.cny.jwf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.cny.jwf.util.FUtil.Hash;
import org.junit.Assert;
import org.junit.Test;

public class FUtilTest {

	@Test
	public void testSha1() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		ByteArrayInputStream bis = new ByteArrayInputStream(new byte[1024]);
		bis.reset();
		bos.reset();
		Hash h1 = FUtil.sha1(bis, bos);
		bis.reset();
		bos.reset();
		Hash h2 = FUtil.sha1(bis, null);
		Assert.assertArrayEquals(h1.hash, h2.hash);
	}

	@Test
	public void testCreate() throws Exception {
		FUtil.create("build/t1.data", 1021);
		FUtil.create("build/t2.data", 1024);
		FUtil.create("build/t3.data", 1023);
		FUtil.create("build/t4.data", 11);
		try {
			FUtil.create("/sss", 1023);
		} catch (Exception e) {

		}
	}

	@Test
	public void testSha1x() throws Exception {
		FileInputStream fis;
		fis = new FileInputStream("/tmp/22.png");
		Hash hash = FUtil.sha1(fis, null);
		fis.close();
		System.err.println(Utils.byte2hex(hash.hash));
	}
	// @Test
	// public void testReset() throws Exception {
	// FUtil.create("build/d.dat", 1021);
	// FileInputStream fis = new FileInputStream("build/d.dat");
	// byte[] buf1 = new byte[1024];
	// byte[] buf2 = new byte[1024];
	// fis.read(buf1);
	// fis.reset();
	// fis.read(buf2);
	// fis.close();
	//
	// }
}
