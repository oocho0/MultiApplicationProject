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
	private Contects contectsType;
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
					contectsType = new Contects_MemoryFile(CONTECTS_FILE);
					dos.writeInt(ServiceCode.CONTECTS_ACCEPT);
					System.out.println(threadName+"서울지부-파일연결 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_REQUEST_DB) {
					contectsType = new Contects_MemoryDB();
					dos.writeInt(ServiceCode.CONTECTS_ACCEPT);
					System.out.println(threadName+"부산지부-DB연결 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_ADD) {
					String receiveInfo = dis.readUTF();
					String[] info = receiveInfo.split(",");
					contectsType.addInfo(new Contects_Info(info[0], info[1], info[2], info[3]));
					dos.writeInt(ServiceCode.CONTECTS_SUCCESS);
					System.out.println(threadName+"연락처 추가 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_MODIFY) {
					String receiveInfo = dis.readUTF();
					String[] info = receiveInfo.split(",");
					contectsType.modifyInfo(new Contects_Info(info[0], info[1], info[2], info[3], info[4]));
					dos.writeInt(ServiceCode.CONTECTS_SUCCESS);
					System.out.println(threadName+"연락처 수정 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_DELETE) {
					String receiveInfo = dis.readUTF();
					String[] infos = receiveInfo.split(",");
					int i = 0;
					for(String a:infos) {
						contectsType.removeInfo(a);
						i++;
					}
					dos.writeInt(ServiceCode.CONTECTS_SUCCESS);
					System.out.println(threadName+"총 "+i+"건 삭제 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_ALL) {
					resultInfos = contectsType.getAll();
					searchAndSend();
					System.out.println(threadName+"연락처 전체 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_SEARCH_BYGROUP) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contectsType.getInfoByGroup(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 부서명으로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_SEARCH_BYNAME) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contectsType.getInfoByName(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 이름으로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_SEARCH_BYTEL) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contectsType.getInfoByTel(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 전화번호로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_SEARCH_BYADDR) {
					String receiveKeyword = dis.readUTF();
					resultInfos = contectsType.getInfoByAddr(receiveKeyword);
					searchAndSend();
					System.out.println(threadName+"연락처 주소로 키워드 검색 완료");
				}
				if(receiveCode == ServiceCode.CONTECTS_EXIT) {
					contectsType.endService();
					if(resultInfos!=null) {
						resultInfos.clear();
					}
					dos.writeInt(ServiceCode.CONTECTS_QUIT);
					System.out.println(threadName+" 연결 해제 완료");
				}
			}
		} catch (IOException e) {
		} finally {
			contectsType = null;
			System.out.println(threadName + "연락처 종료");
			System.out.printf("%s[%s:%s] 접속 해제\n",threadName,socket.getInetAddress(), socket.getPort());
		}
	}
	
	public void searchAndSend() {
		String sendInfos = "";
		String sendMsg = "";
		if(resultInfos.isEmpty()) {
			sendMsg = ServiceCode.CONTECTS_NODATA;
			System.out.println(threadName+"검색 결과 없음");
		}
		for(String a : resultInfos.keySet()) {
			sendInfos += resultInfos.get(a)+"*";
		}
		if(!sendMsg.equals(ServiceCode.CONTECTS_NODATA)) {
			sendMsg = ServiceCode.CONTECTS_RESULT+","+sendInfos;
		}
		try {
			dos.writeUTF(sendMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
