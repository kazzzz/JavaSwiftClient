package com.kazzzz.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RandomFileGenerator {

	public static File getFile(String filepath, long size) throws IOException {
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

	private static byte[] genRandomBytes(int size) {
		byte[] buf = new byte[size];
		for (int i = 0; i < buf.length; i++) {
			byte v = (byte) ((Math.random() * 1000) % 255);
			buf[i] = v;
		}
		return buf;
	}

}
