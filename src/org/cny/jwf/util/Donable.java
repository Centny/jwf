package org.cny.jwf.util;

public interface Donable<T> {
	void onProc(T tg, long done);
}
