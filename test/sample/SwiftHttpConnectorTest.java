package sample;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SwiftHttpConnectorTest {

	private static final String PASSWORD = "hoge";
	private static final String USER = "system:master";
	private static String HOST = "172.16.116.128";
	private static String AUTH_URL = "http://" + HOST + ":8080/auth/v1.0";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAuthOK() {
		SwiftHttpConnector conn = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthNG() {
		SwiftHttpConnector conn = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, "tige");
			fail("exception has not occurered");
		} catch (SwiftException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpload() {
		SwiftHttpConnector conn = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			File file = new File("/tmp/1_3MB.file");
			conn.upload("myfiles", "tekito1_3MB", file);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testUploadLargeFile() {
		SwiftHttpConnector conn = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			File file = new File("/tmp/bigdata");
			conn.upload("myfiles", "bigdata2GB", file);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDownloadOK() {
		SwiftHttpConnector conn = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			File file = conn.download("myfiles", "tekito1_3MB", "/tmp/downloaded");
			assertEquals(file.length(), 1300000);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		
	}

	// @Test
	public void dummytest() throws Exception {
		File file = new File("/tmp/aaa.txt");
		File ofile = new File("/tmp/teki.txt");
		System.out.println(file.length());
		byte[] bb = new byte[1024];
		System.out.println(bb.length);
		FileInputStream is = new FileInputStream(file);
		FileOutputStream os = new FileOutputStream(ofile);

		int len;
		while ((len = is.read(bb)) != -1) {
			System.out.println("len:" + len);
			os.write(bb, 0, len);
		}
		is.close();
		os.close();
	}

	@Test
	public void testGenRandomFile() {
		try {
			File target1 = genRandomFile("/tmp/random1.txt", 231);
			assertEquals(231, target1.length());
			// target1.deleteOnExit();

			File target2 = genRandomFile("/tmp/random2.txt", 231);
			assertEquals(231, target1.length());
			// target1.deleteOnExit();

			long start = System.currentTimeMillis();
			File big = genRandomFile("/tmp/bigdata", 2000000000);
			long end = System.currentTimeMillis();
			System.out.println("big time:" + (end - start));
			assertEquals(2000000000, big.length());

			start = System.currentTimeMillis();
			File file512k = genRandomFile("/tmp/512KB.file", 512000);
			end = System.currentTimeMillis();
			System.out.println("512KB time:" + (end - start));
			assertEquals(512000, file512k.length());

			start = System.currentTimeMillis();
			File file1_3M = genRandomFile("/tmp/1_3MB.file", 1300000);
			end = System.currentTimeMillis();
			System.out.println("1.3MB time:" + (end - start));
			assertEquals(1300000, file1_3M.length());

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
		}
	}

//	@Test
	public void randomFile() throws Exception {
		System.out.println((byte) 257);
		System.out.println((byte) 256);
		System.out.println((byte) 255);
		System.out.println((byte) 128);
		System.out.println(Byte.MAX_VALUE);
		System.out.println(Byte.MIN_VALUE);
		while (true) {
			int seed = (int) (Math.random() * 1000) % 255;
			System.out.println("seed:" + seed);
			byte r = (byte) seed;
			System.out.println("byte:" + r);
			Thread.sleep(1000);
		}
	}

	private File genRandomFile(String filepath, long size) throws IOException {
		int bufsize = 1024;
		File file = new File(filepath);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			long loopcnt = size / bufsize;
			int remainSize = (int) (size % (long) bufsize);
			for (long i = 0; i < loopcnt; i++) {
				bos.write(genRandomBytes(bufsize));
			}
			bos.write(genRandomBytes(remainSize));
			return file;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	private byte[] genRandomBytes(int size) {
		byte[] buf = new byte[size];
		for (int i = 0; i < buf.length; i++) {
			byte v = (byte) ((Math.random() * 1000) % 255);
			buf[i] = v;
		}
		return buf;
	}
}
