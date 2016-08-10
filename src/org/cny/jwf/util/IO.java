package org.cny.jwf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class IO {

	public static List<String> readLines(String fpath) throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(fpath);
			BufferedReader br = new BufferedReader(fr);
			List<String> lines = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch (IOException e) {
			throw e;
		} finally {
			fr.close();
		}
	}

	public static void appendLine(String fpath, String val) throws Exception {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(fpath), true);
			fw.write(val + "\n");
		} catch (Exception e) {
			throw e;
		} finally {
			fw.close();
		}
	}

	public static void touch(String fpath) throws IOException {
		new FileWriter(new File(fpath), true).close();
	}

	public static void copy(OutputStream dest, InputStream src)
			throws IOException {
		int len;
		byte[] buffer = new byte[4096];
		while ((len = src.read(buffer)) != -1) {
			dest.write(buffer, 0, len);
		}
	}
}
