package org.cny.jwf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
	 * join collection to string by special separate.
	 * 
	 * @param vals
	 *            target values.
	 * @param seq
	 *            special separate.
	 * @return joined string.
	 */
	public static String joinSQL(Collection<String> vals) {
		if (vals == null || vals.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = vals.iterator();
		sb.append('\'');
		sb.append(it.next().toString());
		sb.append('\'');
		while (it.hasNext()) {
			sb.append(',');
			sb.append('\'');
			sb.append(it.next().toString());
			sb.append('\'');
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

	/**
	 * time message.
	 * 
	 * @param t
	 *            target timestamp.
	 * @return message.
	 */
	public static String tmsg(long t, boolean show) {
		return tmsg_(t, 0, show);
	}

	public static String tmsg_(long dt, long nt, boolean show) {
		Calendar tn = Calendar.getInstance();
		tn.setTime(new Date(dt));
		Calendar now = Calendar.getInstance();
		if (nt < 1) {
			now.setTime(new Date());
		} else {
			now.setTime(new Date(nt));
		}
		if (tn.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
			return new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(tn
					.getTime());
		}
		int td = tn.get(Calendar.DAY_OF_YEAR);
		int nd = now.get(Calendar.DAY_OF_YEAR);
		int dif = nd - td;
		switch (dif) {
		case 0:
			return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(tn
					.getTime());
		case 1:
			if (show) {
				return "昨天 "
						+ new SimpleDateFormat("HH:mm", Locale.ENGLISH)
								.format(tn.getTime());
			} else {
				return "昨天";
			}
		case 2:
			if (show) {
				return "前天 "
						+ new SimpleDateFormat("HH:mm", Locale.ENGLISH)
								.format(tn.getTime());
			} else {
				return "前天";
			}
		case 3:
		case 4:
		case 5:
		case 6:
			String hm = "";
			if (show) {
				hm = " "
						+ new SimpleDateFormat("HH:mm", Locale.ENGLISH)
								.format(tn.getTime());
			}
			switch (tn.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				return "星期天" + hm;
			case 2:
				return "星期一" + hm;
			case 3:
				return "星期二" + hm;
			case 4:
				return "星期三" + hm;
			case 5:
				return "星期四" + hm;
			case 6:
				return "星期五" + hm;
			default:
				return "星期六" + hm;
			}
		default:
			if (show) {
				return new SimpleDateFormat("MM/dd HH:mm", Locale.ENGLISH)
						.format(tn.getTime());
			} else {
				return new SimpleDateFormat("MM/dd", Locale.ENGLISH).format(tn
						.getTime());
			}
		}

	}

	// public void copy(File dst, File src) throws IOException {
	// FileInputStream fi = null;
	// FileOutputStream fo = null;
	// FileChannel in = null;
	// FileChannel out = null;
	// try {
	// fi = new FileInputStream(src);
	// fo = new FileOutputStream(dst);
	// in = fi.getChannel();
	// out = fo.getChannel();
	// in.transferTo(0, in.size(), out);
	// } catch (IOException e) {
	// throw e;
	// } finally {
	// try {
	// fi.close();
	// } catch (Exception e) {
	// }
	// try {
	// in.close();
	// } catch (Exception e) {
	// }
	// try {
	// fo.close();
	// } catch (Exception e) {
	// }
	// try {
	// out.close();
	// } catch (Exception e) {
	// }
	// }
	// }

	public static void copy(OutputStream dst, InputStream src)
			throws IOException {
		BufferedOutputStream tdst = new BufferedOutputStream(dst);
		BufferedInputStream tsrc = new BufferedInputStream(src);
		byte[] buf = new byte[2048];
		int rlen = 0;
		while ((rlen = tsrc.read(buf)) != -1) {
			tdst.write(buf, 0, rlen);
		}
		tdst.flush();
	}
}
