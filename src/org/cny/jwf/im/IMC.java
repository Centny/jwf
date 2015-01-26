package org.cny.jwf.im;

import java.io.IOException;

import org.cny.jwf.netw.RWRunnerv;
import org.cny.jwf.netw.impl.OBDC;
import org.cny.jwf.netw.impl.OBDH;
import org.cny.jwf.netw.impl.RCv;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.Netw.ModException;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.cny.jwf.netw.r.NetwVer;

public abstract class IMC extends RWRunnerv implements EvnListener, CmdListener {
	public static interface MsgListener {
		public void onMsg(Msg m);
	}

	public static final byte MK_NIM = 0;
	public static final byte MK_NRC = 4;
	public static final byte MK_NRC_LI = 10;
	public static final byte MK_NRC_LO = 20;
	private OBDH obdh;
	private RCv rc;
	private MsgListener l;
	private NetwVer nv;

	public IMC(MsgListener l) {
		super(null, null);
		this.obdh = new OBDH();
		this.evnl = this;
		this.msgl = this.obdh;
		this.l = l;
	}

	@Override
	protected Netw createNetw() throws ModException {
		NetwBase nb = this.createNetwBase();
		this.rc = new RCv(new NetwRWv_i(new OBDC(nb, MK_NRC)), this);
		this.rw = this.nv = new NetwRWv_i(new OBDC(nb, MK_NIM));
		this.obdh.addh(MK_NIM, this);
		this.obdh.addh(MK_NRC, this.rc);
		return this.rw;
	}

	@Override
	public void onCmd(NetwRunnable nr, Cmd m) {
		this.l.onMsg(m.V(Msg.class));
	}

	public void writem(String[] r, byte t, byte[] c) throws IOException {
		this.nv.writev(new Msg(r, t, c));
	}

	public void li(Object args, CmdListener l) throws IOException {
		this.rc.exec(MK_NRC_LI, args, l);
	}

	public void lo(Object args, CmdListener l) throws IOException {
		this.rc.exec(MK_NRC_LO, args, l);
	}

}