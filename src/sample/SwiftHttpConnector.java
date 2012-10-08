package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SwiftHttpConnector {

	private AuthInfo authInfo = null;

	public SwiftHttpConnector() {
	}

	public void auth(String authURL, String user, String password)
			throws SwiftException {
		URL url = null;
		HttpURLConnection urlconn = null;
		try {
			url = new URL(authURL);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.setInstanceFollowRedirects(false);
			urlconn.setRequestProperty("X-Auth-User", user);
			urlconn.setRequestProperty("X-Auth-Key", password);
			urlconn.connect();

			int responseCode = urlconn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new SwiftException("authentication failed. responseCode:"
						+ responseCode);
			}
			Map<String, List<String>> headers = urlconn.getHeaderFields();
			String storageToken = headers.get("X-Storage-Url").get(0);
			String authToken = headers.get("X-Auth-Token").get(0);
			authInfo = new AuthInfo(storageToken, authToken);
		} catch (IOException e) {
			throw new SwiftException(e);
		} finally {
			if (urlconn != null) {
				urlconn.disconnect();
			}
		}

	}

	public void upload(String containerName, String objectName, File file) throws SwiftException {
		URL url = null;
		HttpURLConnection urlconn = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			url = new URL(authInfo.getStorageToken() + "/" + containerName
					+ "/" + objectName);
			urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setRequestMethod("PUT");
			urlconn.setInstanceFollowRedirects(false);
			urlconn.setRequestProperty("X-Auth-Token", authInfo.getAuthToken());
			urlconn.setDoOutput(true);
			urlconn.setRequestProperty("Content-length",
					Long.toString(file.length()));
			urlconn.connect();

			byte[] fbytes = new byte[1024];
			int len;
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(urlconn.getOutputStream());
			while ((len = bis.read(fbytes)) != -1) {
				bos.write(fbytes, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
			int responseCode = urlconn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_CREATED) {
				throw new SwiftException("upload failed HTTP_STATUS_CODE:" + responseCode);
			}
		} catch (IOException e) {
			throw new SwiftException(e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlconn != null) {
				urlconn.disconnect();
			}
		}
	}
}
