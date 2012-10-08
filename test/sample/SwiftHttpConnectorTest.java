package sample;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.After;
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
			File file = new File("/tmp/testtext.txt");
			conn.upload("myfiles", "tekito", file);
		} catch (SwiftException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void dummytest() throws Exception {
		File file = new File("/tmp/aaa.txt");
		File ofile = new File("/tmp/teki.txt");
		System.out.println(file.length());
		byte[] bb = new byte[1024];
		System.out.println(bb.length);
		FileInputStream is = new FileInputStream(file);
		FileOutputStream os = new FileOutputStream(ofile);

		int len;
		while((len = is.read(bb)) != -1) {
			System.out.println("len:" + len);
			os.write(bb,0, len);
		}
		is.close();
		os.close();
	}
}
