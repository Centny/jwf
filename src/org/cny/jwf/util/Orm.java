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

/**
 * the simple implementation for ORM.<br/>
 * it can convert map to object.
 * 
 * @author cny
 *
 */
public abstract class Orm {
	/**
	 * anotation for ORM name.
	 * 
	 * @author cny
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Name {
		/**
		 * target name.
		 * 
		 * @return the string name.
		 */
		public String name() default "";

		/**
		 * the list of the name.
		 * 
		 * @return list names.
		 */
		public String[] names() default {};
	}

	/**
	 * the interface for ORM builder.
	 * 
	 * @author cny
	 *
	 */
	public interface OrmBuilder {
		/**
		 * get the current row value by name.
		 * 
		 * @param name
		 *            target value name.
		 * @param n
		 *            the anotation for filed.
		 * @param cls
		 *            the target class.
		 * @return
		 */
		public <T> T get(String name, Name n, Class<T> cls);

		/**
		 * move to next row.
		 * 
		 * @return if have more.
		 */
		public boolean next();
	}

	/**
	 * build one object by map and class.
	 * 
	 * @param vals
	 *            target field value by map.
	 * @param cls
	 *            target class.
	 * @return initialed object.
	 * @throws Exception
	 */
	public static <T> T build(Map<String, ?> vals, Class<T> cls) {
		return build(new MapBuilder(vals), cls);
	}

	/**
	 * build one object by OrmBuilder and class.
	 * 
	 * @param ob
	 *            target filed value by Builder.
	 * @param cls
	 *            target class.
	 * @return initialed object.
	 */
	public static <T> T build(OrmBuilder ob, Class<T> cls) {
		try {
			if (ob.next()) {
				return build_(ob, cls);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * build one object by OrmBuilder and class.
	 * 
	 * @param ob
	 *            target filed value by Builder.
	 * @param cls
	 *            target class.
	 * @return initialed object.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T build_(OrmBuilder ob, Class<T> cls) throws Exception {
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

	/**
	 * build the list object(only one in it) by map values and class..
	 * 
	 * @param vals
	 *            target field value by map.
	 * @param cls
	 *            target class.
	 * @return initialed object.
	 * @throws Exception
	 */
	public static <T> List<T> builds(Map<String, ?> vals, Class<T> cls) {
		return builds(new MapBuilder(vals), cls);
	}

	/**
	 * build the list object by map collection and class.
	 * 
	 * @param cvals
	 *            all map.
	 * @param cls
	 *            target class.
	 * @return the list of initialed obejct.
	 * @throws Exception
	 */
	public static <T> List<T> builds(Collection<Map<String, ?>> cvals,
			Class<T> cls) {
		return builds(new CollectMapBuilder(cvals), cls);
	}

	/**
	 * build the list object by OrmBuilder and class.
	 * 
	 * @param ob
	 *            the builder.
	 * @param cls
	 *            target class.
	 * @return the list of initialed object.
	 * @throws Exception
	 */
	public static <T> List<T> builds(OrmBuilder ob, Class<T> cls) {
		try {
			return builds_(ob, cls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * build the list object by OrmBuilder and class.
	 * 
	 * @param ob
	 *            the builder.
	 * @param cls
	 *            target class.
	 * @return the list of initialed object.
	 * @throws Exception
	 */
	public static <T> List<T> builds_(OrmBuilder ob, Class<T> cls)
			throws Exception {
		List<T> objs = new ArrayList<T>();
		while (ob.next()) {
			T obj = build_(ob, cls);
			objs.add(obj);
		}
		return objs;
	}

	/**
	 * simple implement OrmBuilder by order the value of configuring in filed
	 * name,anotation name filed,anotation names. <br/>
	 * you can adding @Name(name="-") to ignore target filed.
	 * 
	 * @author cny
	 *
	 */
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

		/**
		 * get the value by key name and class.
		 * 
		 * @param name
		 *            the target name.
		 * @param cls
		 *            the class.
		 * @return value.
		 */
		public abstract <T> T get(String name, Class<T> cls);
	}

	/**
	 * only on row builder extends to OrderBuilder.
	 * 
	 * @author cny
	 *
	 */
	public static abstract class OneBuilder extends OrderBuilder {
		protected int i = 0;

		@Override
		public boolean next() {
			return 1 > i++;
		}

	}

	/**
	 * the one map builder extends OneBuilder.
	 * 
	 * @author cny
	 *
	 */
	public static class MapBuilder extends OneBuilder {
		private final Map<String, ?> vals;

		/**
		 * new one map builder by map.
		 * 
		 * @param vals
		 */
		public MapBuilder(Map<String, ?> vals) {
			this.vals = vals;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T get(String name, Class<T> cls) {
			return (T) this.vals.get(name);
		}

	}

	/**
	 * the collection map builder extends OrderBuilder.
	 * 
	 * @author cny
	 *
	 */
	public static class CollectMapBuilder extends OrderBuilder {
		private final Collection<Map<String, ?>> cvals;
		private final Iterator<Map<String, ?>> it;
		private Map<String, ?> vals;

		/**
		 * new builder by map collection.
		 * 
		 * @param cvals
		 *            map collection.
		 */
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
