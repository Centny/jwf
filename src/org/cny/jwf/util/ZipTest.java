package org.cny.jwf.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ZipTest {

	@Test
	public void testZip() throws Exception {
		IO.appendLine("build/1.txt", "T1");
		IO.appendLine("build/2.txt", "T2");
		IO.appendLine("build/3.txt", "T3");
		Zip.zip("build/t.zip", "build", "build/1.txt", "build/2.txt",
				"build/3.txt");
		Zip.zip("build/t.zip", "build/", "build/1.txt", "build/2.txt",
				"build/3.txt");
		Zip.zip("build/t.zip", "build/");
		String[] ss = null;
		Zip.zip("build/t.zip", "build/", ss);
		Zip.zip("build/t.zip", null, "build/1.txt", "build/2.txt",
				"build/3.txt");
		Zip.zip(null, "build/", "build/1.txt", "build/2.txt", "build/3.txt");

		try {
			Zip.zip("/ss/dd", "build/", "build/1.txt", "build/2.txt",
					"build/3.txt");
		} catch (Exception e) {

		}
		try {
			Zip.zip("build/t2.zip", "build/", "/fsdkfsdkf/dd", "build/2.txt",
					"build/3.txt");
		} catch (Exception e) {

		}
		Zip.zip(null, new File("build/"), new File("build/1.txt"));
		Zip.zip(new File("build/t3.zip"), null, new File("build/1.txt"));
		File[] fs = null;
		Zip.zip(new File("build/t3.zip"), new File("build/"), fs);
		Zip.zip(new File("build/t3.zip"), new File("build/"), new File[0]);
		//
		List<File> fs2 = null;
		Zip.zip(null, new File("build/"), fs2);
		Zip.zip(new File("build/t3.zip"), null, fs2);
		Zip.zip(new File("build/t3.zip"), new File("build/"), fs2);
		Zip.zip(new File("build/t3.zip"), new File("build/"),
				new ArrayList<File>());
		new Zip();
	}
}
