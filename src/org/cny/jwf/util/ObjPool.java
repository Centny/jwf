package org.cny.jwf.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ObjPool<T> {
	protected int max = 100;
	protected Map<Object, T> objs = new HashMap<Object, T>();
	protected List<Object> idx = new LinkedList<Object>();

	public ObjPool(int max) {
		this.max = max;
	}

	public T load(Object key, Object... args) throws Exception {
		return load_(key, args);
	}

	public T load_(Object key, Object[] args) throws Exception {
		if (key == null) {
			return null;
		}
		T val;
		if (this.objs.containsKey(key)) {
			val = this.objs.get(key);
			this.idx.remove(key);
		} else {
			if (this.idx.size() >= this.max) {
				this.objs.remove(this.idx.get(0));
				this.idx.remove(0);
			}
			val = this.create(key, args);
			if (val == null) {
				return null;
			}
			this.objs.put(key, val);
		}
		this.idx.add(key);
		return val;
	}

	protected abstract T create(Object key, Object[] args) throws Exception;
}
