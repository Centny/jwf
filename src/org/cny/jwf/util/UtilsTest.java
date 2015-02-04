package org.cny.jwf.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void testJoin() {
		Assert.assertEquals("1,2,3", Utils.join(new byte[] { 1, 2, 3 }));
		Assert.assertEquals("", Utils.join(new byte[] {}));
		Assert.assertEquals("", Utils.join((byte[]) null));
	}

	@Test
	public void testArray() {
		Assert.assertEquals("1,2,3", Arrays.toString(new byte[] { 1, 2, 3 }));
	}

	@Test
	public void testDel() throws IOException {
		File f = new File("/tmp/sss");
		f.mkdirs();
		new File(f, "sss").mkdir();
		new File(f, "sss/abc").mkdir();
		new File(f, "/sss/ddd").createNewFile();
		Assert.assertEquals(true, Utils.del(f));
		Assert.assertEquals(true, Utils.del(new File("/sfksdfjksd/dsfdds")));
	}

	@Test
	public void testOinfo() throws Exception {
		System.err.println(Utils.oinfo(new Object()));
	}
}
