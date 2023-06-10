package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BaseballGame_Server implements Runnable {

	@Override
	public void run() {
		ServerSocket serverSocket;
		Socket socket;
		try {
			serverSocket = new ServerSocket(6002);
			System.out.println(Thread.currentThread().getName()+"야구게임 서버가 준비되었습니다.");
			while(true) {
				socket = serverSocket.accept();
				System.out.printf("%s[%s:%s] 접속\n",Thread.currentThread().getName(),socket.getInetAddress(), socket.getPort());
				new BaseballGame_ServerThread(socket,Thread.currentThread().getName()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
