package Server;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;


public class Chat_Server implements Runnable {
	private HashMap<String, DataOutputStream> clients;
	
	public Chat_Server() {
		clients = new HashMap<>();
		Collections.synchronizedMap(clients);
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(ServiceCode.CHATTING_PORT);
			System.out.println(Thread.currentThread().getName()+"채팅 서버가 준비되었습니다.");
			while(true) {
				socket = serverSocket.accept();
				System.out.printf("%s[%s:%s] 접속\n",Thread.currentThread().getName(),socket.getInetAddress(), socket.getPort());
				new Chat_ServerThread(socket, clients,Thread.currentThread().getName()).start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
