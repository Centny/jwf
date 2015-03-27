package org.cny.test;

import java.io.File;

import org.cny.jwf.util.IO;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

public class FileTest {

	@Test
	public void testRename() throws Exception {
		IO.appendLine("data/t.txt", "xxx");
		Assert.assertTrue(new File("data/t.txt").renameTo(new File(
				"build/R1.txt")));
	}

	@Test
	public void testLog() throws InterruptedException {
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "t.log");
		Logger log = LoggerFactory.getLogger("XX");
		log.error("sfsfs1");
		log.error("sfsfs2");
		log.error("sfsfs3");
		System.out.println(new File("t.log").renameTo(new File("x.log")));
		Thread.sleep(3000);
		log.error("sfsfs4");
		log.error("sfsfs5");
		log.error("sfsfs6");
	}
}
