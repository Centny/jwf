package org.cny.jwf.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjPool<T> {
	class Ref extends SoftReference<T> {
		Object key;

		public Ref(Object key, T referent, ReferenceQueue<? super T> q) {
			super(referent, q);
			this.key = key;
		}

	}

	protected Map<Object, Ref> objs = new HashMap<Object, Ref>();
	protected ReferenceQueue<T> quque = new ReferenceQueue<T>();

	public ObjPool() {
	}

	public T load(Object key, Object... args) throws Exception {
		return load_(key, args);
	}

	public T load_(Object key, Object[] args) throws Exception {
		if (key == null) {
			return null;
		}
		this.gc();
		Object rkey = this.createKey(key, args);
		T val = null;
		if (this.objs.containsKey(rkey)) {
			val = this.objs.get(rkey).get();
		}
		if (val == null) {
			val = this.create(key, args);
			this.objs.put(rkey, new Ref(rkey, val, this.quque));
		}
		return val;
	}

	public T find(Object key, Object[] args) {
		if (key == null) {
			return null;
		}
		Object rkey = this.createKey(key, args);
		if (this.objs.containsKey(rkey)) {
			return this.objs.get(rkey).get();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void gc() {
		Ref ref;
		while ((ref = (ObjPool<T>.Ref) this.quque.poll()) != null) {
			this.objs.remove(ref.key);
		}
	}

	protected Object createKey(Object key, Object[] args) {
		if (key instanceof Key) {
			return ((Key) key).findKey();
		} else {
			return key.toString();
		}
	}

	public void add(Object key, T val) {
		Object rk = null;
		if (key instanceof Key) {
			rk = ((Key) key).findKey();
		} else {
			rk = key;
		}
		this.objs.put(rk, new Ref(rk, val, this.quque));
	}

	protected abstract T create(Object key, Object[] args) throws Exception;

	public static interface Key {
		Object findKey();
	}
}
