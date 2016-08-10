package org.cny.jwf.util;

import org.junit.Assert;
import org.junit.Test;

public class ObjPoolTest {

	int tc = 0;

	public class Ary {

	}

	@Test
	public void testPool() throws Exception {
		tc = 0;
		ObjPool<String> op = new ObjPool<String>() {

			@Override
			protected String create(Object key, Object[] args) {
				if (args.length < 1) {
					return null;
				}
				tc++;
				return args[0].toString();
			}

		};
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				op.load("abc", "val");
			}
			for (int j = 9; j > -1; j--) {
				op.load("abc", "val");
			}
		}
		Assert.assertEquals(1, tc);
		tc = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				op.load("abc" + j, "val");
			}
			for (int j = 9; j > -1; j--) {
				op.load("abc" + j, "val");
			}
		}
		Assert.assertEquals(10, tc);
		op.load("sss");
		op.load_(null, null);
	}

	@Test
	public void testPool2() throws Exception {
		ObjPool<byte[]> op2 = new ObjPool<byte[]>() {

			@Override
			protected byte[] create(Object key, Object[] args) {
				return new byte[102400];
			}

		};
		for (int i = 0; i < 100000; i++) {
			op2.load("o" + i);
		}
	}
}
