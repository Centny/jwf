package org.cny.jwf.im;

import java.security.InvalidParameterException;
import java.util.Arrays;

import org.cny.jwf.im.pb.Msg;
import org.cny.jwf.im.pb.Msg.ImMsg;
import org.cny.jwf.netw.r.Cmd;
import org.cny.jwf.netw.r.NetwVer;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class PbSckIMC extends SckIMC {

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
				return this.gs.fromJson(bys.toString(), cls);
			}
		} catch (InvalidProtocolBufferException e) {
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
			bys = this.gs.toJson(o).getBytes();
		}
		return this.netw().newM(bys, 0, bys.length);
	}

	@Override
	protected ImMsg create(String[] r, byte t, byte[] c) {
		if (r == null || r.length < 1 || c == null || c.length < 1) {
			throw new InvalidParameterException("the r/c is null or empty");
		}
		Msg.ImMsg.Builder imb = Msg.ImMsg.newBuilder();
		imb.addAllR(Arrays.asList(r));
		imb.setT(t);
		imb.setC(ByteString.copyFrom(c));
		return imb.build();
	}

}
