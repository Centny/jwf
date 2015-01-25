package org.cny.jwf.netw.r;

public interface Msg {
	int length();

	int offset();

	byte[] bys();

	Msg slice(int offs);

	Msg slice(int offs, int len);

	byte get(int idx);

	Netw netw();
}
