package sample;

public class AuthInfo {
	
	private String storageToken = null;
	private String authToken = null;
	
	public AuthInfo() {
	}
	
	public AuthInfo(String storageToken, String authToken) {
		this.storageToken = storageToken;
		this.authToken = authToken;
	}
	
	public String getStorageToken() {
		return storageToken;
	}
	public void setStorageToken(String storageToken) {
		this.storageToken = storageToken;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
			

}
