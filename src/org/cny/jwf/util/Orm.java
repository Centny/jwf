package org.cny.jwf.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Orm {
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Name {
		public String name() default "";

		public String[] names() default {};
	}

	public interface OrmBuilder {
		public <T> T get(String name, Name n, Class<T> cls);

		public boolean next();
	}

	public static <T> T build(Map<String, ?> vals, Class<T> cls)
			throws Exception {
		return build(new MapBuilder(vals), cls);
	}

	@SuppressWarnings("unchecked")
	public static <T> T build(OrmBuilder ob, Class<T> cls) throws Exception {
		Method[] ms = cls.getMethods();
		Object obj = cls.newInstance();
		for (Method m : ms) {
			Class<?>[] aty = m.getParameterTypes();
			if (aty.length != 1) {
				continue;
			}
			String mn = m.getName();
			if (mn.length() < 4) {
				continue;
			}
			if (!"set".equals(mn.substring(0, 3))) {
				continue;
			}
			String tn = Utils.firstLow(mn.substring(3));
			Object val = ob.get(tn, m.getAnnotation(Name.class),
					m.getParameterTypes()[0]);
			if (val == null) {
				continue;
			}
			if (aty[0] == String.class && val.getClass() != String.class) {
				val = val.toString();
			}

			try {
				m.invoke(obj, val);
			} catch (Exception e) {
				System.err.println("set method(" + mn + "," + aty[0]
						+ ") by value(" + val + "," + val.getClass()
						+ ") error:" + e.getMessage());
			}
		}
		return (T) obj;
	}

	public static <T> List<T> builds(Map<String, ?> vals, Class<T> cls)
			throws Exception {
		return builds(new MapBuilder(vals), cls);
	}

	public static <T> List<T> builds(Collection<Map<String, ?>> cvals,
			Class<T> cls) throws Exception {
		return builds(new CollectMapBuilder(cvals), cls);
	}

	public static <T> List<T> builds(OrmBuilder ob, Class<T> cls)
			throws Exception {
		List<T> objs = new ArrayList<T>();
		while (ob.next()) {
			T obj = build(ob, cls);
			objs.add(obj);
		}
		return objs;
	}

	public static abstract class OrderBuilder implements OrmBuilder {

		@Override
		public <T> T get(String name, Name n, Class<T> cls) {
			if (n == null) {
				return this.get(name, cls);
			}
			T val;
			String tn = n.name();
			if ("-".equals(tn)) {
				return null;
			}
			if ((val = this.get(tn, cls)) != null) {
				return val;
			}
			for (String nn : n.names()) {
				val = this.get(nn, cls);
				if (val != null) {
					return val;
				}
			}
			return this.get(name, cls);
		}

		public abstract <T> T get(String name, Class<T> cls);
	}

	public static abstract class OneBuilder extends OrderBuilder {
		protected int i = 0;

		@Override
		public boolean next() {
			return 1 > i++;
		}

	}

	public static class MapBuilder extends OneBuilder {
		private final Map<String, ?> vals;

		public MapBuilder(Map<String, ?> vals) {
			this.vals = vals;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T get(String name, Class<T> cls) {
			return (T) this.vals.get(name);
		}

	}

	public static class CollectMapBuilder extends OrderBuilder {
		private final Collection<Map<String, ?>> cvals;
		private final Iterator<Map<String, ?>> it;
		private Map<String, ?> vals;

		public CollectMapBuilder(Collection<Map<String, ?>> cvals) {
			this.cvals = cvals;
			this.it = this.cvals.iterator();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T get(String name, Class<T> cls) {
			return (T) this.vals.get(name);
		}

		@Override
		public boolean next() {
			if (this.it.hasNext()) {
				this.vals = this.it.next();
				return true;
			} else {
				return false;
			}
		}

	}
}
