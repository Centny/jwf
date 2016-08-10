package org.cny.jwf.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
	public static void zip(String dest, String base, String... fs)
			throws IOException {
		if (dest == null || base == null || fs == null || fs.length < 1) {
			return;
		}
		File[] tfs = new File[fs.length];
		for (int i = 0; i < fs.length; i++) {
			tfs[i] = new File(fs[i]);
		}
		zip(new File(dest), new File(base), tfs);
	}

	public static void zip(File dest, File base, File... fs) throws IOException {
		if (dest == null || base == null || fs == null || fs.length < 1) {
			return;
		}
		zip(dest, base, Arrays.asList(fs));
	}

	public static void zip(File dest, File base, List<File> fs)
			throws IOException {
		if (dest == null || base == null || fs == null || fs.isEmpty()) {
			return;
		}
		ZipEntry ze;
		FileInputStream fis = null;
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(dest)));
			for (File f : fs) {
				ze = new ZipEntry(f.getAbsolutePath().replace(
						base.getAbsolutePath() + "/", ""));
				zos.putNextEntry(ze);
				fis = new FileInputStream(f);
				IO.copy(zos, fis);
				fis.close();
				fis = null;
				zos.closeEntry();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (zos != null) {
				zos.close();
			}
		}
	}

	protected Zip() {

	}
}
