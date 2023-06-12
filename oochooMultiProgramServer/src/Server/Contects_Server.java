package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Contects_Server implements Runnable {
	private final String CONTECTS_FILE;
	
	public Contects_Server() {
		CONTECTS_FILE = "seoulContectsInfo.csv";
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket;
		Socket socket;
		try {
			serverSocket = new ServerSocket(ServiceCode.CONTECTS_PORT);
			System.out.println(Thread.currentThread().getName()+"연락처 서버가 준비되었습니다.");
			while(true) {
				socket = serverSocket.accept();
				System.out.printf("%s[%s:%s] 접속\n",Thread.currentThread().getName(),socket.getInetAddress(), socket.getPort());
				new Contects_ServerThread(socket,Thread.currentThread().getName(),CONTECTS_FILE).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
