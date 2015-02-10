package org.cny.jwf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void testSome() {
		Utils.newu();
		Utils.firstLow("ss");
		Utils.firstLow("");
		Utils.firstUp("aa");
		Utils.firstUp("");
		Utils.stack(Thread.currentThread().getStackTrace());
		Utils.Kv("abc", "ss");
	}

	@Test
	public void testJoin() {
		Assert.assertEquals("1,2,3", Utils.join(new String[] { "1", "2", "3" }));
		Assert.assertEquals("1,2,3",
				Utils.join(new String[] { "1", "2", "3", null }));
		Assert.assertEquals("1,2,3", Utils.join(new byte[] { 1, 2, 3 }));
		Assert.assertEquals("", Utils.join(new byte[] {}));
		Assert.assertEquals("", Utils.join((byte[]) null));
		Assert.assertEquals("", Utils.join(new String[] {}));
		Assert.assertEquals("", Utils.join((String[]) null));
		List<String> ss = new ArrayList<String>();
		Assert.assertEquals("", Utils.join(ss));
		ss.add("1");
		ss.add("2");
		ss.add("3");
		Assert.assertEquals("1,2,3", Utils.join(ss));
		Assert.assertEquals("", Utils.join((List<String>) null));
	}

	@Test
	public void testHex() {
		Utils.byte2hex(new byte[] { 1, 2, 3, 100 });
	}

	@Test
	public void testArray() {
		Assert.assertEquals("[1,2,3]", Arrays.toString(new byte[] { 1, 2, 3 }));
	}

	@Test
	public void testDel() throws IOException {
		File f = new File("/tmp/sss");
		f.mkdirs();
		new File(f, "sss").mkdir();
		new File(f, "sss/abc").mkdir();
		new File(f, "/sss/ddd").createNewFile();
		Assert.assertEquals(true, Utils.del(f));
		Assert.assertEquals(false, Utils.del(new File("/sfksdfjksd/dsfdds")));
	}

	public static class SS {
		public void getA() {

		}

		public int ga() {
			return 1;
		}

		public int getB() {
			throw new RuntimeException();
		}

		public Object getC() {
			return null;
		}

		public String[] getD() {
			return new String[] { "1", "2" };
		}
	}

	@Test
	public void testOinfo() throws Exception {
		System.err.println(Utils.oinfo(new Object()));
		System.err.println(Utils.oinfo(new SS()));
	}

	@Test
	public void testTimestamp() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		long ttt = sdf.parse("20141230").getTime();
		for (int k = 0; k < 32; k++) {
			long bbb = ttt - k * 24 * 60 * 60 * 1000;
			for (int i = 0; i < 100; i++) {
				Utils.tmsg_(bbb - i * 24 * 60 * 60 * 1000, bbb, true);
				Utils.tmsg_(bbb - i * 24 * 60 * 60 * 1000, bbb, false);
			}
		}
		long bbb = new Date().getTime();
		for (int i = 0; i < 10; i++) {
			System.err.println(Utils.tmsg_(bbb - i * 24 * 60 * 60 * 1000, bbb,
					true));
			System.err.println(Utils.tmsg_(bbb - i * 24 * 60 * 60 * 1000, bbb,
					false));
		}
		// System.err.println(Utils.tmsg(tt - 2 * 24 * 60 * 60 * 1000));
		System.err.println("--->");
		Calendar cc = Calendar.getInstance();
		cc.set(Calendar.MONTH, cc.get(Calendar.MARCH) - 1);
		System.err.println(Utils.tmsg(cc.getTimeInMillis(), true));
		System.err.println(Utils.tmsg(cc.getTimeInMillis(), false));
		cc.setTime(new Date());
		cc.set(Calendar.YEAR, cc.get(Calendar.YEAR) - 1);
		System.err.println(Utils.tmsg(cc.getTimeInMillis(), true));
		System.err.println(Utils.tmsg(cc.getTimeInMillis(), false));
	}

	@Test
	public void testCopy() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		ByteArrayInputStream bis = new ByteArrayInputStream(new byte[1024]);
		Utils.copy(bos, bis);
	}
}
