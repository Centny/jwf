package org.cny.jwf.netw.r;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import org.cny.jwf.netw.r.NetwRunnable.EvnListener;
import org.junit.Test;

public class NetwRunnerTest {

	public class RR implements Netw {

		int i = -1;

		@Override
		public void writeM(List<Cmd> ms) throws IOException {

		}

		@Override
		public void setLimit(int l) {

		}

		@Override
		public int readw(byte[] buf, int off, int len) throws IOException {
			return 0;
		}

		@Override
		public void writem(List<byte[]> ms) throws IOException {

		}

		@Override
		public void writem(byte[] m, int offs, int len) throws IOException {

		}

		@Override
		public void writem(byte[] m) throws IOException {

		}

		@Override
		public void writeM(Cmd b) throws IOException {

		}

		@Override
		public int readw(byte[] buf) throws IOException {
			return 0;
		}

		@Override
		public byte[] readm() throws IOException, ModException {
			return null;
		}

		@Override
		public Cmd readM() throws IOException, ModException {
			this.i++;
			switch (this.i) {
			case 0:
				throw new ModException("ss");

			case 1:
				throw new RuntimeException();
			default:
				throw new EOFException();

			}
		}

		@Override
		public Cmd newM(byte[] m, int off, int len) {
			return null;
		}
	}

	public class NR extends NetwRunner {

		RR nw;

		public NR(CmdListener msg, EvnListener evn) {
			super(msg, evn);
		}

		@Override
		public Netw netw() {
			return this.nw;
		}

		@Override
		protected Netw createNetw() throws Exception {
			this.nw = new RR();
			return this.nw;
		}
	}

	@Test
	public void testNw() throws Exception {
		NR nr;
		nr = new NR(null, new EvnListener() {

			@Override
			public void onErr(NetwRunnable nr, Throwable e) {

			}

			@Override
			public void onCon(NetwRunnable nr, Netw nw) throws Exception {

			}

			@Override
			public void begCon(NetwRunnable nr) throws Exception {

			}
		});
		nr.run_c();
		nr.run_c();
		nr.run_c();
		nr.run_c();
		nr.running = false;
		nr.stop();

		new NetwRunner(null, null) {

			@Override
			public void run_c() throws Exception {
				throw new Exception();
			}

			@Override
			public Netw netw() {
				return null;
			}

			@Override
			protected Netw createNetw() throws Exception {
				return null;
			}

		}.run();
	}
}
