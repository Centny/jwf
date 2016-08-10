package org.cny.jwf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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
		//
		Assert.assertEquals("1,2,3",
				Utils.join(new String[] { "0", "1", "2", "3", "4" }, 1, 3, ","));
		Assert.assertEquals("0,1,2",
				Utils.join(new String[] { "0", "1", "2", "3", "4" }, 0, 3, ","));
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

	public static class ObjectDeserializer<T> implements JsonDeserializer<T> {
		protected Gson gs = new Gson();
		protected JsonElement je;

		@Override
		public T deserialize(JsonElement je, Type type,
				JsonDeserializationContext ctx) throws JsonParseException {
			this.je = je;
			if (je.isJsonObject() || je.isJsonArray()) {
				return this.gs.fromJson(je, type);
			} else {
				return null;
			}
		}
	}

	public static class Abc {
		public String key;
		public String val;
		public int type;
	}

	public class CRes<T> {
		/**
		 * the response code.
		 */
		public int code;
		/**
		 * the data object.
		 */
		public T data;
		/**
		 * the error message.
		 */
		public String msg;
		/**
		 * the debug message.
		 */
		public String dmsg;
	}

	public String tdata4 = "{\"code\":11}";
	public String tdata5 = "{\"code\":11,\"data\":\"sdfs\"}";
	public String tdata6 = "{\"code\":11,\"data\":111}";
	public String tdata7 = "{\"code\":11,\"data\":{\"key\":\"ss\"}}";

	public class Azz<T> {

		public ObjectDeserializer<T> xx() {
			return new ObjectDeserializer<T>();
		}
	}

	public class Axx extends Azz<Abc> {

		public TypeToken<CRes<Abc>> cc() {
			return new TypeToken<CRes<Abc>>() {
			};
		}
	}

	@Test
	public void testGSon() {
		GsonBuilder gb = new GsonBuilder();
		Axx axx = new Axx();
		gb.registerTypeAdapter(Abc.class, axx.xx());
		Gson gs = gb.create();

		// TypeToken<CRes<Abc>> tt = new TypeToken<CRes<Abc>>() {
		// };
		CRes<Abc> res;
		res = gs.fromJson(tdata6, axx.cc().getType());
		Assert.assertNull(res.data);
		res = gs.fromJson(tdata7, axx.cc().getType());
		Assert.assertEquals("ss", res.data.key);
	}
}
