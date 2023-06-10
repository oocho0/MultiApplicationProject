package Server;

public class Login_UserInfo {
	private String serialNo;
	private String name;
	private String userId;
	private String password;
	private String email;
	
	public Login_UserInfo(String serialNo, String name, String userId, String password, String email) {
		this.serialNo = serialNo;
		this.name = name;
		this.userId = userId;
		this.password = password;
		this.email = email;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public String getName() {
		return name;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return String.format("[%s] 회원명 : %s, 회원아이디 : %s, 회원비밀번호 : %s, 회원추가정보-이메일 : %s\n", serialNo, name, userId, password, email);
	}
}
