package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class Login_ServerThread extends Thread {
	private DataInputStream dis;
	private DataOutputStream dos;
	private final String USER_DB;
	private Map<String, Login_UserInfo> userList;
	private Map<String, String> idList;
	private String threadName;
	private Socket socket;
	
	public Login_ServerThread(Socket socket, String uSER_DB, Map<String, Login_UserInfo> userList, Map<String, String> idList, String threadName) {
		USER_DB = uSER_DB;
		this.socket = socket;
		this.userList = userList;
		this.idList = idList;
		this.threadName = threadName;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			int sendCode = 0;

			while(dis!=null&&dos!=null) {
				int serviceCode = dis.readInt();
				if(serviceCode==ServiceCode.LOGIN_REQUEST) {
					sendCode = ServiceCode.LOGIN_ACCEPT;
				}
				if(serviceCode==ServiceCode.LOGIN_SEND) {
					String[] check = dis.readUTF().split(",",2);
					String checkID = check[0];
					String checkPW = check[1];
					sendCode = checkLogin(checkID, checkPW);
				}
				if(serviceCode==ServiceCode.SIGNUP_SEND) {
					String[] check = dis.readUTF().split(",",4);
					String newName = check[0];
					String newID = check[1];
					String newPW = check[2];
					String newEmail = check[3];
					sendCode = checkDuplicateAndSave(newID, newEmail);
					if(sendCode == ServiceCode.SIGNUP_SUCCESS) {
						FileWriter fw = new FileWriter(USER_DB, true);
						fw.write((UUID.randomUUID().toString())+","+newName+","+newID+","+newPW+","+newEmail+"\n");
						fw.close();
						Login_CheckDBThread ct = new Login_CheckDBThread(userList, idList, USER_DB);
						ct.start();
						ct.join();
						System.out.println(threadName+"회원가입 성공");
					}
				}
				dos.writeInt(sendCode);
				
			}
		} catch (IOException | InterruptedException e) {
		}finally {
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
		}
	}
	
	public int checkLogin(String checkID, String checkPW) {
		if(!idList.containsKey(checkID)) {
			System.out.println(threadName+"없는 아이디");
			return ServiceCode.LOGIN_NO_ID;
		} else if(checkPW.equals(userList.get(idList.get(checkID)).getPassword())){
			System.out.println(threadName+"로그인 성공");
			return ServiceCode.LOGIN_SUCCESS;
		}else {
			System.out.println(threadName+"비밀번호 틀림");
			return ServiceCode.LOGIN_NO_PW;
		}
	}
	
	public int checkDuplicateAndSave(String newID, String newEmail) {
		if(idList.containsKey(newID)) {
			System.out.println(threadName+"존재하는 아이디");
			return ServiceCode.SIGNUP_EXIST_ID;
		}
		for(Login_UserInfo user : userList.values()) {
			if(newEmail.equals(user.getEmail())) {
				System.out.println(threadName+"존재하는 이메일주소");
				return ServiceCode.SIGNUP_EXIST_EMAIL;
			}
		}
		System.out.println(threadName+"중복 확인");
		return ServiceCode.SIGNUP_SUCCESS;
	}
}
