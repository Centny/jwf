package org.cny.jwf.netw;

public interface NetwRunnable extends Netw, Runnable {
	static interface MsgListener {
		void onErr(Throwable e);

		void onMsg(byte[] m);
	}

	void setListener(MsgListener l);
}
