package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Chat_ServerThread extends Thread {
	private HashMap<String, DataOutputStream> clients;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket socket;
	private String threadName;
	
	public Chat_ServerThread(Socket socket, HashMap<String, DataOutputStream> clients, String threadName) {
		this.clients = clients;
		this.socket = socket;
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
		String clientId = "";
		String msg = "";
		try {
			while(dis!=null&&dos!=null) {
				int receiveCode = dis.readInt();
				if(receiveCode == ServiceCode.CHATTING_REQUEST) {
					clientId = dis.readUTF();
					if(clients.containsKey(clientId)) {
						dos.writeUTF(ServiceCode.CHATTING_EXISTID+",");
						continue;
					}
					clients.put(clientId, dos);
					msg = ServiceCode.CHATTING_ENTER+",["+clientId+"] 님이 들어왔습니다."+","+currentClients();
					sendToAll(msg);
					System.out.println(threadName+"새로운 접속자가 들어왔습니다 - "+clientId);
					System.out.println(threadName+"현재 채팅 서버 접속자 수는 "+clients.size()+"입니다.");
				}
				if(receiveCode == ServiceCode.CHATTING_SEND) {
					String receiveMsg = dis.readUTF();
					msg = ServiceCode.CHATTING_MSG+",["+clientId+"]"+receiveMsg;
					sendToAll(msg);
				}
				if(receiveCode == ServiceCode.CHATTING_WHISPER) {
					String[] splitMsg = dis.readUTF().split("\\*",2);
					String[] idLists = splitMsg[0].split(",");
					ArrayList<String> whisperIds = new ArrayList<>();
					boolean clientNocontainId = false;
					for(String id: idLists) {
						if(!clients.containsKey(id)) {
							dos.writeUTF(ServiceCode.CHATTING_WRONGID+",");
							clientNocontainId = true;
							break;
						}
						if(!whisperIds.contains(id)) {
							whisperIds.add(id);
						}
					}
					if(clientNocontainId) {
						continue;
					}
					msg = ServiceCode.CHATTING_WHISPER_MSG+",["+clientId+"]"+splitMsg[1];
					System.out.println(msg);
					sendWhisper(whisperIds, msg);
				}
				if(receiveCode == ServiceCode.CHATTING_EXIT) {
					sendToAll(ServiceCode.CHATTING_QUIT+",["+clientId+"] 님이 나갔습니다.");
				}
			}
		} catch (IOException e) {
		} finally {
			clients.remove(clientId);
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
			System.out.println(threadName+"접속자가 나갔습니다 - "+clientId);
			System.out.println(threadName+"현재 서버접속자 수는 "+clients.size()+"입니다.");
		}
	}
	
	public void sendToAll(String msg) {
		for(String key : clients.keySet()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients.get(key);
				dos.writeUTF(msg);
			}catch(IOException e) {
			}
		}
	}
	
	public void sendWhisper(ArrayList<String> whisperIds, String msg) {
		for(String id : whisperIds) {
			try {
				DataOutputStream dos = (DataOutputStream) clients.get(id);
				dos.writeUTF(msg);
			} catch (IOException e) {
			}
		}
	}
	
	public String currentClients() {
		String current = "";
		for(String key : clients.keySet()) {
			current += key +",";
		}
		return current;
	}
}
