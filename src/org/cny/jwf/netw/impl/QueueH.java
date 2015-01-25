package org.cny.jwf.netw.impl;

import java.util.LinkedList;
import java.util.List;

import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.MsgListener;

public class QueueH implements MsgListener {
	private List<MsgListener> qs = new LinkedList<MsgListener>();

	@Override
	public void onMsg(NetwRunnable nr, Msg m) {
		for (MsgListener ml : this.qs) {
			ml.onMsg(nr, m);
		}
	}

	public void addh(MsgListener l) {
		this.qs.add(l);
	}

}
