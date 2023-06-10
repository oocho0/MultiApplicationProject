package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Login_Server implements Runnable {
	private Map<String, Login_UserInfo> userList;
	private Map<String, String> idList;
	private final String USER_DB;
	
	public Login_Server() {
		userList = new HashMap<>();
		idList = new HashMap<>();
		USER_DB = "oochooAppUserListDB.csv";
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(6001);
			System.out.println(Thread.currentThread().getName()+"로그인 서버가 준비되었습니다.");
			Login_CheckDBThread ct = new Login_CheckDBThread(userList, idList, USER_DB);
			ct.start();
			ct.join();
			while(true) {
				socket = serverSocket.accept();
				System.out.printf("%s[%s:%s] 접속\n",Thread.currentThread().getName(),socket.getInetAddress(), socket.getPort());
				new Login_ServerThread(socket, USER_DB, userList, idList, Thread.currentThread().getName()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
