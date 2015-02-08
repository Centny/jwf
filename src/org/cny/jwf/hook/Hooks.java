package org.cny.jwf.hook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hooks {

	protected static Hooks HK = null;
	protected Map<Object, List<Hookable>> hks = new HashMap<Object, List<Hookable>>();

	public static Hooks instance() {
		if (HK == null) {
			HK = new Hooks();
		}
		return HK;
	}

	public static int call(Object t, Object... args) {
		return instance().call_(t, args);
	}

	public static Hooks add(Object key, Hookable h) {
		return instance().addv(key, h);
	}

	public static Hooks del(Object key, Hookable h) {
		return instance().delv(key, h);
	}

	public interface Hookable {
		int onCall(Object[] args);
	}

	public Hooks() {

	}

	public Hooks addv(Object key, Hookable h) {
		if (!this.hks.containsKey(key)) {
			this.hks.put(key, new ArrayList<Hooks.Hookable>());
		}
		this.hks.get(key).add(h);
		return this;
	}

	public Hooks delv(Object key, Hookable h) {
		if (this.hks.containsKey(key)) {
			this.hks.get(key).remove(h);
		}
		return this;
	}

	public int callv(Object t, Object... args) {
		return this.call_(t, args);
	}

	public int call_(Object t, Object[] args) {
		if (!this.hks.containsKey(t)) {
			return -1;
		}
		List<Hookable> thks = this.hks.get(t);
		for (Hookable h : thks) {
			int v = h.onCall(args);
			if (v > 0) {
				return v;
			}
		}
		return -1;
	}
}
