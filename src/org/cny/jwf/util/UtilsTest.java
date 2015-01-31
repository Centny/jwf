package org.cny.jwf.util;

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
}
