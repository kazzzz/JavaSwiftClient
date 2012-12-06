package com.kazzzz.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RandomFileGeneratorTest {

	@Test
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
			File file512k = RandomFileGenerator.getFile("/tmp/512KB.file",
					512000);
			end = System.currentTimeMillis();
			System.out.println("512KB time:" + (end - start));
			assertEquals(512000, file512k.length());
			file512k.delete();

			start = System.currentTimeMillis();
			File file1_3M = RandomFileGenerator.getFile("/tmp/1_3MB.file",
					1300000);
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
