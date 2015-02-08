package org.cny.jwf.hook;

import org.cny.jwf.hook.Hooks.Hookable;
import org.junit.Assert;
import org.junit.Test;

public class HooksTest {

	public static class HK1 implements Hookable {

		@Override
		public int onCall(Object[] args) {
			if (args == null) {
				return 0;
			} else {
				return args.length;
			}
		}

	}

	public static class HK2 implements Hookable {

		@Override
		public int onCall(Object[] args) {
			return 1;
		}

	}

	public static class HK3 implements Hookable {

		@Override
		public int onCall(Object[] args) {
			throw new RuntimeException();
		}

	}

	@Test
	public void testHook() {
		Hooks.add(this, new HK1());
		Hooks.add(this, new HK2());
		HK3 hk = new HK3();
		Hooks.add(this, hk);
		Assert.assertEquals(1, Hooks.call(this));
		Assert.assertEquals(1, Hooks.call(this, "aa"));
		Assert.assertEquals(2, Hooks.call(this, "aa", "bb"));
		Assert.assertEquals(3, Hooks.call(this, "aa", "bb", "cc"));
		Assert.assertEquals(3, Hooks.instance().callv(this, "aa", "bb", "cc"));
		Assert.assertEquals(-1, Hooks.call(""));
		Hooks.del(this, hk);
		Hooks.instance().hks.get(this).clear();
		Assert.assertEquals(-1, Hooks.call(this, "aa", "bb", "cc"));
		Hooks.del("", hk);
		Hooks.instance().hks.clear();
		Assert.assertEquals(-1, Hooks.call(""));
	}
}
