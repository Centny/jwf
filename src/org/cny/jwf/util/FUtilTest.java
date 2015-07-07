package org.cny.jwf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
}
