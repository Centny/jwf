package org.cny.jwf.netw.impl;

import java.util.HashMap;
import java.util.Map;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OBDH implements CmdListener {
	private static Logger L = LoggerFactory.getLogger(OBDH.class);
	private Map<Byte, CmdListener> hs = new HashMap<Byte, CmdListener>();

	@Override
	public void onCmd(NetwRunnable nr, Cmd m) {
		byte mark = m.get(0);
		if (hs.containsKey(mark)) {
			m.forward(1);
			hs.get(mark).onCmd(nr, m);
		} else {
			L.warn("recieve unknow mark({}) message({})", mark, m.toString());
		}
	}

	public void addh(byte m, CmdListener h) {
		this.hs.put(m, h);
	}

}
