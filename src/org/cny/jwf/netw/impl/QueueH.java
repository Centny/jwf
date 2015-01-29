package org.cny.jwf.netw.impl;

import java.util.LinkedList;
import java.util.List;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;

public class QueueH implements CmdListener {
	private final List<CmdListener> qs = new LinkedList<CmdListener>();

	@Override
	public void onCmd(NetwRunnable nr, Cmd m) {
		for (CmdListener ml : this.qs) {
			ml.onCmd(nr, m);
		}
	}

	public void addh(CmdListener l) {
		this.qs.add(l);
	}

}
