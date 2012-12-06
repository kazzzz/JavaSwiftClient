package com.kazzzz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kazzzz.util.RandomFileGenerator;

public class SwiftHttpConnectorTest {

	private static final String CONTAINER_NAME_MYFILES = "myfiles";
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
	public void testUploadOK() {
		SwiftHttpConnector conn = null;
		File file = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile("/tmp/1_3MB.file", 1300000);
			conn.upload(CONTAINER_NAME_MYFILES, "tekito1_3MB", file);
			file.delete();
			conn.remove(CONTAINER_NAME_MYFILES, "tekito1_3MB");
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	// @Test
	public void testUploadLargeFile() {
		SwiftHttpConnector conn = null;
		File file = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile("/tmp/bigdata", 2000000000);
			conn.upload(CONTAINER_NAME_MYFILES, "bigdata2GB", file);
			file.delete();
			conn.remove(CONTAINER_NAME_MYFILES, "bigdata2GB");
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			file.delete();
		}
	}

	@Test
	public void testDownloadOK() {
		SwiftHttpConnector conn = null;
		File downloadfile = null;
		String uploadObjectName = "1_3MB.file";
		String dir = "/tmp/";
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			File uploadfile = RandomFileGenerator.getFile(dir
					+ uploadObjectName, 1300000);
			conn.upload(CONTAINER_NAME_MYFILES, uploadObjectName, uploadfile);
			downloadfile = conn.download(CONTAINER_NAME_MYFILES, uploadObjectName,
					dir + "downloaded.file");
			assertEquals(downloadfile.length(), 1300000);
			downloadfile.delete();
			uploadfile.delete();
			conn.remove(CONTAINER_NAME_MYFILES, uploadObjectName);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testDownloadEqualsOK() {
		SwiftHttpConnector conn = null;
		String filename = "equals1OK.file";
		String filename1 = "equals1.file";
		String filename2 = "equals2.file";
		String dir = "/tmp/";
		File file = null;
		File fileD1 = null;
		File fileD2 = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile(dir + filename, 512000);
			conn.upload(CONTAINER_NAME_MYFILES, filename1, file);
			conn.upload(CONTAINER_NAME_MYFILES, filename2, file);
			fileD1 = conn.download(CONTAINER_NAME_MYFILES, filename1, dir
					+ filename1);
			fileD2 = conn.download(CONTAINER_NAME_MYFILES, filename2, dir
					+ filename2);
			assertEquals(fileD1.length(), fileD2.length());
			conn.remove(CONTAINER_NAME_MYFILES, filename1);
			conn.remove(CONTAINER_NAME_MYFILES, filename2);
			file.delete();
			fileD1.delete();
			fileD2.delete();
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testRemoveOK() {
		SwiftHttpConnector conn = null;
		String filename = "removeok.file";
		String filepath = "/tmp/" + filename;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			File removeOkFile = RandomFileGenerator.getFile(filepath, 1024);
			conn.upload(CONTAINER_NAME_MYFILES, filename, removeOkFile);
			conn.remove(CONTAINER_NAME_MYFILES, filename);
			removeOkFile.delete();
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			conn.download(CONTAINER_NAME_MYFILES, filename, filepath);
			fail("not removed");
		} catch (SwiftException ex) {
			ex.printStackTrace();
		}
	}
	
	public void testListObjects() {
		SwiftHttpConnector conn = null;
		String filename = "list1.file";
		String filename1 = "list2.file";
		String filename2 = "list3.file";
		String dir = "/tmp/";
		File file = null;
		File fileD1 = null;
		File fileD2 = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			Set objs = conn.listObjects();

			for (Object obj : objs) {
					
			}
			
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
		
	}
}
