package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Contacts_ServerThread extends Thread{
	private Socket socket;
	private String threadName;
	private DataInputStream dis;
	private DataOutputStream dos;
	private final String CONTACTS_FILE;
	private Contacts contactsType;
	private Map<String, Contacts_Info> resultInfos;
	
	public Contacts_ServerThread(Socket socket, String threadName, String cONTACTS_FILE) {
		this.socket = socket;
		this.threadName = threadName;
		CONTACTS_FILE = cONTACTS_FILE;
		
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
				if(receiveCode == ServiceCode.CONTACTS_REQUEST_FILE) {
					contactsType = new Contacts_MemoryFile(CONTACTS_FILE);
					dos.writeInt(ServiceCode.CONTACTS_ACCEPT);
					System.out.println(threadName+"서울지부-파일연결 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_REQUEST_DB) {
					contactsType = new Contacts_MemoryDB();
					dos.writeInt(ServiceCode.CONTACTS_ACCEPT);
					System.out.println(threadName+"부산지부-DB연결 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_ADD) {
					String receiveInfo = dis.readUTF();
					String[] info = receiveInfo.split(",");
					contactsType.addInfo(new Contacts_Info(info[0], info[1], info[2], info[3]));
					dos.writeInt(ServiceCode.CONTACTS_SUCCESS);
					System.out.println(threadName+"연락처 추가 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_MODIFY) {
					String receiveInfo = dis.readUTF();
					String[] info = receiveInfo.split(",");
					contactsType.modifyInfo(new Contacts_Info(info[0], info[1], info[2], info[3], info[4]));
					dos.writeInt(ServiceCode.CONTACTS_SUCCESS);
					System.out.println(threadName+"연락처 수정 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_DELETE) {
					String receiveInfo = dis.readUTF();
					String[] infos = receiveInfo.split(",");
					int i = 0;
					for(String a:infos) {
						contactsType.removeInfo(a);
						i++;
					}
					dos.writeInt(ServiceCode.CONTACTS_SUCCESS);
					System.out.println(threadName+"총 "+i+"건 삭제 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_ALL) {
					resultInfos = contactsType.getAll();
					searchAndSend();
					System.out.println(threadName+"연락처 전체 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_SEARCH_BYGROUP) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contactsType.getInfoByGroup(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 부서명으로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_SEARCH_BYNAME) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contactsType.getInfoByName(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 이름으로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_SEARCH_BYTEL) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contactsType.getInfoByTel(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 전화번호로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_SEARCH_BYADDR) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contactsType.getInfoByAddr(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 주소로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTACTS_EXIT) {
					contactsType.endService();
					if(resultInfos!=null) {
						resultInfos.clear();
					}
					dos.writeInt(ServiceCode.CONTACTS_QUIT);
					System.out.println(threadName+" 연결 해제 완료");
				}
			}
		} catch (IOException e) {
		} finally {
			contactsType = null;
			System.out.println(threadName + "연락처 종료");
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
		}
	}
	
	public void searchAndSend() {
		String sendInfos = "";
		String sendMsg = "";
		if(resultInfos.isEmpty()) {
			sendMsg = ServiceCode.CONTACTS_NODATA;
			System.out.println(threadName+"검색 결과 없음");
		}
		for(String a : resultInfos.keySet()) {
			sendInfos += resultInfos.get(a)+"*";
		}
		if(!sendMsg.equals(ServiceCode.CONTACTS_NODATA)) {
			sendMsg = ServiceCode.CONTACTS_RESULT+","+sendInfos;
		}
		try {
			dos.writeUTF(sendMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
