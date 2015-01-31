package org.cny.jwf.netw.r;

public interface Cmd extends Netw, NetwVer.NetwVable {
	int length();

	int offset();

	void reset(int off, int len);

	void forward(int len);

	byte[] bys();

	byte[] sbys();

	Cmd slice(int off);

	Cmd slice(int off, int len);

	byte get(int idx);

	short shortv(int off);

	String toBs();
}
