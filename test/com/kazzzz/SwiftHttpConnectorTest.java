package com.kazzzz;

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

import com.kazzzz.SwiftException;
import com.kazzzz.SwiftHttpConnector;
import com.kazzzz.util.RandomFileGenerator;

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
	public void testUploadOK() {
		SwiftHttpConnector conn = null;
		File file = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile("/tmp/1_3MB.file", 1300000);
			conn.upload("myfiles", "tekito1_3MB", file);
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

//	@Test
	public void testUploadLargeFile() {
		SwiftHttpConnector conn = null;
		File file = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile("/tmp/bigdata", 2000000000);
			conn.upload("myfiles", "bigdata2GB", file);
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
		File file = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = conn.download("myfiles", "tekito1_3MB", "/tmp/downloaded");
			assertEquals(file.length(), 1300000);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			file.delete();
		}
		
	}
	
	@Test
	public void testDownloadEqualsOK() {
		SwiftHttpConnector conn = null;
		File file = null;
		File fileD1 = null;
		File fileD2 = null;
		try {
			conn = new SwiftHttpConnector();
			conn.auth(AUTH_URL, USER, PASSWORD);
			file = RandomFileGenerator.getFile("/tmp/equalsOK.file", 512000);
			conn.upload("myfiles", "equals1", file);
			conn.upload("myfiles", "equals2", file);
			fileD1 = conn.download("myfiles", "equals1", "/tmp/equalsD1.file");
			fileD2 = conn.download("myfiles", "equals2", "/tmp/equalsD2.file");
			assertEquals(fileD1,fileD2);
		} catch (SwiftException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
	}
	

//	@Test
	public void testGenRandomFile() {
		try {
			File target1 = RandomFileGenerator.getFile("/tmp/random1.txt", 231);
			assertEquals(231, target1.length());
			target1.delete();

			long start = System.currentTimeMillis();
			File big = RandomFileGenerator.getFile("/tmp/bigdata", 2000000000);
			long end = System.currentTimeMillis();
			System.out.println("big time:" + (end - start));
			assertEquals(2000000000, big.length());
			big.delete();

			start = System.currentTimeMillis();
			File file512k = RandomFileGenerator.getFile("/tmp/512KB.file", 512000);
			end = System.currentTimeMillis();
			System.out.println("512KB time:" + (end - start));
			assertEquals(512000, file512k.length());
			file512k.delete();

			start = System.currentTimeMillis();
			File file1_3M = RandomFileGenerator.getFile("/tmp/1_3MB.file", 1300000);
			end = System.currentTimeMillis();
			System.out.println("1.3MB time:" + (end - start));
			assertEquals(1300000, file1_3M.length());
			file1_3M.delete();

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} finally {
		}
	}
}
