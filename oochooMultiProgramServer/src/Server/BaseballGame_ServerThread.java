package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BaseballGame_ServerThread extends Thread {
	private BaseballGame_RandomComputer rc;
	private BaseballGame_GameCheck gc;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String threadName;
	private Socket socket;
	
	public BaseballGame_ServerThread(Socket socket, String threadName) {
		this.threadName = threadName;
		this.socket = socket;
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
			while(dis!=null&&dos!=null) {
				int receiveCode = dis.readInt();
				if(receiveCode == ServiceCode.BASEBALLGAME_RESTART) {
					System.out.println(threadName+"새로운 게임을 시작했습니다.");
					dos.writeInt(ServiceCode.BASEBALLGAME_RESET);
					rc = new BaseballGame_RandomComputer();
					System.out.println(rc);
				}
				if(receiveCode == ServiceCode.BASEBALLGAME_REQUEST) {
					System.out.println(threadName+"게임을 시작했습니다.");
					dos.writeInt(ServiceCode.BASEBALLGAME_ACCEPT);
					rc = new BaseballGame_RandomComputer();
					System.out.println(rc);
				}
				if(receiveCode == ServiceCode.BASEBALLGAME_SEND){
					String receiveSend = dis.readUTF();
					String[] numArray = receiveSend.split(",");
					int[] input = new int[numArray.length];
					for(int i = 0;i<numArray.length;i++) {
						input[i] = Integer.parseInt(numArray[i]);
					}
					gc = new BaseballGame_GameCheck(rc.getTarget(), input);
					System.out.println(threadName+gc);
					dos.writeUTF(gc.getValue());
				}
			}
		} catch (IOException e) {
		} finally {
			System.out.println(threadName+"게임을 종료했습니다.");
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
		}
	}
}