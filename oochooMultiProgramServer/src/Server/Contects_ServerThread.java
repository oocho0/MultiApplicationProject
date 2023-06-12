package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Contects_ServerThread extends Thread{
	private Socket socket;
	private String threadName;
	private DataInputStream dis;
	private DataOutputStream dos;
	private final String CONTECTS_FILE;
	private Contects contects_type;
	private Map<String, Contects_Info> resultInfos;
	
	public Contects_ServerThread(Socket socket, String threadName, String cONTECTS_FILE) {
		this.socket = socket;
		this.threadName = threadName;
		CONTECTS_FILE = cONTECTS_FILE;
		
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
				int receiveCode;
				receiveCode = dis.readInt();
				if(receiveCode == ServiceCode.CONTECTS_REQUEST_FILE) {
					contects_type = new Contects_MemoryFile(threadName, CONTECTS_FILE);
					System.out.println(threadName+"서울지부-파일연결 완료");
					dos.writeInt(ServiceCode.CONTECTS_ACCEPT);
				}
				if(receiveCode == ServiceCode.CONTECTS_REQUEST_DB) {
					contects_type = new Contects_MemoryDB(threadName);
					System.out.println(threadName+"부산지부-DB연결 완료");
					dos.writeInt(ServiceCode.CONTECTS_ACCEPT);
				}
			}
		} catch (IOException e) {
		} finally {
			contects_type = null;
			resultInfos.clear();
			System.out.println(threadName + "연락처를 종료했습니다.");
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
		}
	}
}
