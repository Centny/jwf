package org.cny.jwf.netw.impl;

import java.util.LinkedList;
import java.util.List;

import org.cny.jwf.netw.Bytes;
import org.cny.jwf.netw.NetwRunnable.MsgListener;

public class QueueH implements MsgListener {
	private List<MsgListener> qs = new LinkedList<MsgListener>();

	@Override
	public void onMsg(Bytes m) {
		for (MsgListener ml : this.qs) {
			ml.onMsg(m);
		}
	}

	public void addh(MsgListener l) {
		this.qs.add(l);
	}
}
