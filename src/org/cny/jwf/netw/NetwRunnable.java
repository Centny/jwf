package org.cny.jwf.netw;

public interface NetwRunnable extends Netw, Runnable {
	static interface EvnListener {
		void onErr(Throwable e);
	}

	static interface MsgListener {
		void onMsg(Bytes m);
	}

	void setEvnL(EvnListener l);

	void setMsgL(MsgListener l);
}
