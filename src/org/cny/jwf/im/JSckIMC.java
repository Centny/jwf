package org.cny.jwf.im;

import net.sf.json.JSONObject;

import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwVer;

public class JSckIMC extends SckIMC {

	public JSckIMC(EvnListener e, MsgListener l, String host, int port) {
		super(e, l, host, port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T B2V(Cmd bys, Class<T> cls) {
//		System.err.println("==>" + bys.toString());
		return (T) JSONObject
				.toBean(JSONObject.fromObject(bys.toString()), cls);
	}

	@Override
	public Cmd V2B(NetwVer nv, Object o) {
		String json = JSONObject.fromObject(o).toString();
//		System.err.println(json);
		byte[] bys = json.getBytes();
		return this.netw().newM(bys, 0, bys.length);
	}

}
