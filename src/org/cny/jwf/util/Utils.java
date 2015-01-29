package org.cny.jwf.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

public final class Utils {
	private Utils() {
		// do nothing.
	}

	public static <T> String join(T[] vals) {
		return join(vals, ",");
	}

	public static <T> String join(T[] vals, String seq) {
		if (vals == null || vals.length < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(vals[0].toString());
		for (int i = 1; i < vals.length; i++) {
			sb.append(seq);
			sb.append(vals[i].toString());
		}
		return sb.toString();
	}

	public static <T> String join(Collection<T> vals) {
		return join(vals, ",");
	}

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
}
