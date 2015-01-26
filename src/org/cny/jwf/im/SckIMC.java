package org.cny.jwf.im;

import java.io.IOException;
import java.net.Socket;

import org.cny.jwf.netw.NetwRWbase;
import org.cny.jwf.netw.r.NetwBase;

public abstract class SckIMC extends IMC {
	protected String host;
	protected int port;
	protected Socket sck;

	public SckIMC(EvnListener e, MsgListener l, String host, int port) {
		super(e, l);
		this.host = host;
		this.port = port;
	}

	public void close() throws IOException {
		if (this.sck != null) {
			this.sck.close();
		}
	}

	@Override
	protected NetwBase createNetwBase() throws Exception {
		this.sck = new Socket(this.host, this.port);
		return new NetwRWbase(this.sck.getOutputStream(), 10240,
				this.sck.getInputStream(), 102400);
	}

}
