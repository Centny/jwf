package org.cny.jwf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cny.jwf.util.Orm.Name;
import org.cny.jwf.util.Orm.OrmBuilder;
import org.junit.Test;

public class OrmTest {

	public static class Val {
		private String a;
		public int b;
		private String c;
		public String d;
		private String e;
		public String f;

		public void setA(String a) {
			this.a = a;
		}

		public void setB(int b) {
			this.b = b;
		}

		@Name(name = "cc")
		public void setC(String c) {
			this.c = c;
		}

		@Name(names = { "dd", "DD" })
		public void setD(String d) {
			this.d = d;
		}

		public void setE(String e) {
			this.e = e;
		}

		public void setAbb(int s) {

		}

		@Name(name = "-")
		public void setAbb2(int s) {

		}

		public void ss(int s) {

		}

		@Override
		public String toString() {
			return "" + this.a + " " + this.b + " " + this.c + " " + this.e;
		}

	}

	@Test
	public void testOrm() throws Exception {
		Map<String, Object> vals;
		vals = new HashMap<String, Object>();
		List<Map<String, ?>> cvals = new ArrayList<Map<String, ?>>();
		vals.put("a", "A1");
		vals.put("b", 1);
		vals.put("cc", "C1");
		vals.put("DD", "D1");
		vals.put("e", "E1");
		cvals.add(vals);
		vals.put("a", "A1");
		vals.put("b", Integer.valueOf(11));
		vals.put("cc", "C1");
		vals.put("DD", "D1");
		vals.put("e", "E1");
		vals.put("abb", "E1");
		cvals.add(vals);
		vals = new HashMap<String, Object>();
		vals.put("a", "A2");
		vals.put("b", new Short((short) 1));
		vals.put("cc", "C2");
		vals.put("d", "D2");
		vals.put("e", 2.2);
		cvals.add(vals);
		//
		Val v = Orm.build(vals, Val.class);
		System.out.println(v.toString());
		System.out.println(Orm.builds(vals, Val.class));
		System.out.println(Orm.builds(cvals, Val.class));
		new Orm() {
		};
	}

	@Test
	public void testErr() {
		try {
			Orm.build(new OrmBuilder() {

				@Override
				public boolean next() {
					throw new RuntimeException();
				}

				@Override
				public <T> T get(String name, Name n, Class<T> cls) {
					return null;
				}
			}, Object.class);
		} catch (Exception e) {

		}
		Orm.build(new OrmBuilder() {

			@Override
			public boolean next() {
				return false;
			}

			@Override
			public <T> T get(String name, Name n, Class<T> cls) {
				return null;
			}
		}, Object.class);
		try {
			Orm.builds((OrmBuilder) null, Object.class);
		} catch (Exception e) {

		}
	}
}
