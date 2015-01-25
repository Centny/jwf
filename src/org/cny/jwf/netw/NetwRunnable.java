package org.cny.jwf.netw;

import java.io.IOException;

public interface NetwRunnable extends Netw, Runnable {
	static interface EvnListener {
		void onErr(Throwable e);
	}

	static interface MsgListener {
		void onMsg(Bytes m);
	}

	static interface Converter {
		<T> T B2V(Bytes bys, Class<T> cls);

		Bytes V2B(NetwRunnable nr, Object o);
	}

	void writem(Bytes b) throws IOException;

	void writev(Object v) throws IOException;

	void setEvnL(EvnListener l);

	void setMsgL(MsgListener l);

	void setConverter(Converter c);

	Converter getConverter();
}
