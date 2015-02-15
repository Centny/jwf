package org.cny.jwf.im;

import java.io.IOException;
import java.util.HashMap;

import org.cny.jwf.im.pb.Msg.ImMsg;
import org.cny.jwf.netw.RWRunnerv;
import org.cny.jwf.netw.impl.OBDC;
import org.cny.jwf.netw.impl.OBDH;
import org.cny.jwf.netw.impl.RCv;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.DoNotCmd;
import org.cny.jwf.netw.r.Netw;
import org.cny.jwf.netw.r.NetwBase;
import org.cny.jwf.netw.r.NetwRunnable;
import org.cny.jwf.netw.r.NetwRunnable.CmdListener;
import org.cny.jwf.netw.r.NetwVer;

public abstract class IMC extends RWRunnerv implements CmdListener {

	public static final byte MK_NIM = 0;
	public static final byte MK_NRC = 4;
	public static final byte MK_NRC_LI = 10;
	public static final byte MK_NRC_LO = 20;
	public static final byte MK_NRC_UR = 30;
	protected OBDH obdh;
	protected RCv rc;
	private final MsgListener l;
	protected NetwVer nv;

	public static interface MsgListener {
		public void onMsg(Msg m);
	}

	public IMC(EvnListener e, MsgListener l) {
		super(null, null);
		this.obdh = new OBDH();
		this.evnl = e;
		this.msgl = this.obdh;
		this.l = l;
	}

	@Override
	protected Netw createNetw() throws Exception {
		NetwBase nb = this.createNetwBase();
		this.rc = new RCv(new NetwRWv_i(new OBDC(nb, MK_NRC)), this);
		this.rw = this.nv = new NetwRWv_i(new OBDC(nb, MK_NIM));
		this.obdh.addh(MK_NIM, this);
		this.obdh.addh(MK_NRC, this.rc);
		return this.rw;
	}

	@Override
	public void onCmd(NetwRunnable nr, Cmd m) {
		this.l.onMsg(new Msg(m.V(ImMsg.class)));
	}

	protected abstract ImMsg create(String[] r, byte t, byte[] c);

	public void sms(String[] r, byte t, byte[] c) throws IOException {
		this.nv.writev(this.create(r, t, c));
	}

	public void sms(String[] r, int t, byte[] c) throws IOException {
		this.sms(r, (byte) t, c);
	}

	public void li(Object args, CmdListener l) throws Exception {
		this.rc.exec(MK_NRC_LI, args, l);
	}

	public Cmd li(Object args) throws Exception {
		return this.rc.exec(MK_NRC_LI, args);
	}

	public <T> T li(Object args, Class<T> cls) throws Exception,
			InterruptedException {
		return this.rc.exec(MK_NRC_LI, args, cls);
	}

	public void lo(Object args, CmdListener l) throws Exception {
		this.rc.exec(MK_NRC_LO, args, l);
	}

	public Cmd lo(Object args) throws Exception {
		return this.rc.exec(MK_NRC_LO, args);
	}

	public <T> T lo(Object args, Class<T> cls) throws Exception {
		return this.rc.exec(MK_NRC_LO, args, cls);
	}

	public void ur() throws Exception {
		this.rc.exec(MK_NRC_UR, new HashMap<String, Object>(), new DoNotCmd());
	}

	public void rcClear(Exception e) {
		if (this.rc != null) {
			this.rc.clear(e);
		}
	}
}