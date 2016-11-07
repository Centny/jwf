package org.cny.jwf.im;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import org.cny.jwf.im.pb.Msg;
import org.cny.jwf.im.pb.Msg.ImMsg;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwVer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;

public class PbSckIMC extends SckIMC {

	private static Logger L = LoggerFactory.getLogger(PbSckIMC.class);
	protected final Gson gs = new Gson();

	public PbSckIMC(EvnListener e, MsgListener l, String host, int port) {
		super(e, l, host, port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T B2V(Cmd bys, Class<T> cls) {
		try {
			if (cls == Msg.ImMsg.class) {
				return (T) Msg.ImMsg.parseFrom(bys.sbys());
			} else {
				// Gson gs = new Gson();
				return this.gs.fromJson(bys.toString().trim(), cls);
			}
		} catch (Exception e) {
			L.warn("B2V Data({}) to V err({})", bys.toString(), e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public Cmd V2B(NetwVer nv, Object o) {
		byte[] bys;
		if (o instanceof Msg.ImMsg) {
			Msg.ImMsg msg = (ImMsg) o;
			bys = msg.toByteArray();
		} else {
			// Gson gs = new Gson();
			bys = this.gs.toJson(o).getBytes();
		}
		return this.netw().newM(bys, 0, bys.length);
	}

	@Override
	protected ImMsg create(String i, String[] r, byte t, byte[] c) {
		if (r == null || r.length < 1 || c == null || c.length < 1) {
			throw new InvalidParameterException("the r/c is null or empty");
		}
		Msg.ImMsg.Builder imb = Msg.ImMsg.newBuilder();
		List<String> rr = Arrays.asList(r);
		imb.addAllR(rr);
		imb.setT(t);
		imb.setC(ByteString.copyFrom(c));
		if (i != null) {
			imb.setI(i);
		}
		return imb.build();
	}

}
