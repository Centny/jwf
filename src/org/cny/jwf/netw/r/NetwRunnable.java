package org.cny.jwf.netw.r;

public interface NetwRunnable extends Runnable {
	public static interface EvnListener {
		void onCon(NetwRunnable nr, Netw nw);

		void onErr(NetwRunnable nr, Throwable e);
	}

	public static interface CmdListener {
		void onCmd(NetwRunnable nr, Cmd m);
	}

	Netw netw();
}
