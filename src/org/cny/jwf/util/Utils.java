package org.cny.jwf.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * normal util class.
 * 
 * @author cny
 *
 */
public class Utils {

	/**
	 * join Object[] to string by ,
	 * 
	 * @param vals
	 *            target values.
	 * @return joined string.
	 */
	public static <T> String join(T[] vals) {
		return join(vals, ",");
	}

	/**
	 * join Object[] to string by special separate.
	 * 
	 * @param vals
	 *            target values.
	 * @param seq
	 *            special separate.
	 * @return joined string.
	 */
	public static <T> String join(T[] vals, String seq) {
		if (vals == null || vals.length < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(vals[0].toString());
		for (int i = 1; i < vals.length; i++) {
			if (vals[i] == null) {
				continue;
			}
			sb.append(seq);
			sb.append(vals[i].toString());
		}
		return sb.toString();
	}

	/**
	 * join the byte[] to string by ,
	 * 
	 * @param vals
	 *            target bytes.
	 * @return joined string.
	 */
	public static String join(byte[] vals) {
		return join(vals, ",");
	}

	/**
	 * join byte[] to string by special separate.
	 * 
	 * @param vals
	 *            target values.
	 * @param seq
	 *            special separate.
	 * @return joined string.
	 */
	public static String join(byte[] vals, String seq) {
		if (vals == null || vals.length < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(vals[0] + "");
		for (int i = 1; i < vals.length; i++) {
			sb.append(seq);
			sb.append(vals[i]);
		}
		return sb.toString();
	}

	/**
	 * join collection to string by,.
	 * 
	 * @param vals
	 *            target values.
	 * @return joined string.
	 */
	public static <T> String join(Collection<T> vals) {
		return join(vals, ",");
	}

	/**
	 * join collection to string by special separate.
	 * 
	 * @param vals
	 *            target values.
	 * @param seq
	 *            special separate.
	 * @return joined string.
	 */
	public static <T> String join(Collection<T> vals, String seq) {
		if (vals == null || vals.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator<T> it = vals.iterator();
		sb.append(it.next().toString());
		while (it.hasNext()) {
			sb.append(seq);
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	/**
	 * convert byte[] to hex.
	 * 
	 * @param b
	 *            byte[]
	 * @return hex string.
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs.append("0").append(stmp);
			else {
				hs = hs.append(stmp);
			}
		}
		return hs.toString().toUpperCase(Locale.ENGLISH);
	}

	/**
	 * convert first char to upper case.
	 * 
	 * @param s
	 *            target string.
	 * @return new string.
	 */
	public static String firstUp(String s) {
		if (s.isEmpty()) {
			return s;
		} else {
			return s.substring(0, 1).toUpperCase(Locale.ENGLISH)
					+ s.substring(1);
		}
	}

	/**
	 * convert first char to lower case.
	 * 
	 * @param s
	 *            target string.
	 * @return new string.
	 */
	public static String firstLow(String s) {
		if (s.isEmpty()) {
			return s;
		} else {
			return s.substring(0, 1).toLowerCase(Locale.ENGLISH)
					+ s.substring(1);
		}
	}

	/**
	 * delete file or directory by deleting all child file.
	 * 
	 * @param f
	 *            target file,
	 * @return
	 */
	public static boolean del(File f) {
		if (f.isFile()) {
			return f.delete();
		}
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File tf : fs) {
				del(tf);
			}
		}
		return f.delete();
	}

	/**
	 * put all stack info to string.
	 * 
	 * @param es
	 *            target stacks.
	 * @return join string.
	 */
	public static String stack(StackTraceElement[] es) {
		StringBuffer sb = new StringBuffer();
		for (StackTraceElement e : es) {
			sb.append(e.getFileName());
			sb.append(',');
			sb.append(e.getClassName());
			sb.append(',');
			sb.append(e.getMethodName());
			sb.append(',');
			sb.append(e.getLineNumber());
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * get object info by call it all get method,and to string.
	 * 
	 * @param o
	 *            target object.
	 * @return key-value by filed name and file value.
	 */
	public static Map<String, String> oinfo(Object o) {
		Map<String, String> kvs = new HashMap<String, String>();
		for (Method m : o.getClass().getMethods()) {
			if (m.getParameterTypes().length > 0) {
				continue;
			}
			if (m.getReturnType() == void.class) {
				continue;
			}
			String mn = m.getName();
			if (mn.length() < 4) {
				continue;
			}
			if (!"get".equals(mn.substring(0, 3))) {
				continue;
			}
			String tn = Utils.firstLow(mn.substring(3));
			Object val;
			try {
				val = m.invoke(o);
			} catch (Throwable e) {
				continue;
			}
			if (val == null) {
				continue;
			}
			Class<?> tcls = val.getClass();
			if (tcls == String[].class) {
				kvs.put(tn, join((String[]) val));
			} else {
				kvs.put(tn, val.toString());
			}
		}
		return kvs;
	}

	/**
	 * new map by one key-value.
	 * 
	 * @param key
	 *            target key.
	 * @param v
	 *            target value.
	 * @return
	 */
	public static Map<String, Object> Kv(String key, Object v) {
		Map<String, Object> kv = new HashMap<String, Object>();
		kv.put(key, v);
		return kv;
	}

	/**
	 * default constructor.
	 */
	private Utils() {
		// do nothing.
	}

	/**
	 * only for test coverage.
	 * 
	 * @return
	 */
	public static Utils newu() {
		return new Utils();
	}
}
