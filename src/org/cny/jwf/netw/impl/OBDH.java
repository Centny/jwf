package org.cny.jwf.netw.impl;

import java.util.HashMap;
import java.util.Map;

import org.cny.jwf.netw.r.Msg;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OBDH implements MsgListener {
	private static Logger L = LoggerFactory.getLogger(OBDH.class);
	private Map<Byte, MsgListener> hs = new HashMap<Byte, MsgListener>();

	@Override
	public void onMsg(NetwRunnable nr, Msg m) {
		if (hs.containsKey(m.get(0))) {
			hs.get(m.get(0)).onMsg(nr, m.slice(1));
		} else {
			L.warn("recieve unknow mark(%d) message(%s)", m.get(0),
					m.toString());
		}
	}

	public void addh(byte m, MsgListener h) {
		this.hs.put(m, h);
	}

}
