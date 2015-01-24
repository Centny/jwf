package org.cny.jwf.netw.impl;

import java.util.HashMap;
import java.util.Map;

import org.cny.jwf.netw.Bytes;
import org.cny.jwf.netw.NetwRunnable.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OBDH implements MsgListener {
	private static Logger L = LoggerFactory.getLogger(OBDH.class);
	private Map<Byte, MsgListener> hs = new HashMap<Byte, MsgListener>();

	@Override
	public void onMsg(Bytes m) {
		if (hs.containsKey(m.get(0))) {
			hs.get(m.get(0)).onMsg(m.slice(1));
		} else {
			L.warn("recieve unknow mark(%d) message(%s)", m.get(0), m.toBs());
		}
	}

	public void addh(byte m, MsgListener h) {
		this.hs.put(m, h);
	}
}
