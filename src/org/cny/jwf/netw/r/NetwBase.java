package org.cny.jwf.netw.r;

import java.io.IOException;
import java.util.List;

public interface NetwBase {
	public void setLimit(int l);

	public int readw(byte[] buf, int off, int len) throws IOException;

	public void writeM(List<Cmd> ms) throws IOException;
}
