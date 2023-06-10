package Client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chat_ReceiveThread extends Thread {
	private JTextArea chattingArea;
	private JTextArea clientList;
	private JPanel startPanel;
	private JPanel quitPanel;
	private JTextField idInput;
	private JTextField textInput;
	private JLabel notice;
	
	private Socket socket;
	private DataInputStream dis;
	private ArrayList<String> connectedIds;
	private String chatId;
	
	public Chat_ReceiveThread(Socket socket, ArrayList<String> connectedIds, String chatId){
		this.connectedIds = connectedIds;
		this.socket = socket;
		this.chatId = chatId;
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGUI(JTextArea chattingArea, JTextArea clientList, JPanel startPanel, JPanel quitPanel, JTextField idInput, JLabel notice, JTextField textInput) {
		this.chattingArea = chattingArea;
		this.clientList = clientList;
		this.startPanel = startPanel;
		this.quitPanel = quitPanel;
		this.idInput = idInput;
		this.notice = notice;
		this.textInput = textInput;
	}
	
	@Override
	public void run() {
		try {
			while(dis!=null) {
				String[] receive = dis.readUTF().split(",",2);
				System.out.println(receive[0]+"|"+receive[1]);
				System.out.println(receive[1]);
				String newMsg = receive[1];
				if(receive[0].equals(ServiceCode.CHATTING_ENTER)) {
					String id = receive[1].substring(receive[1].indexOf('[')+1,receive[1].lastIndexOf(']'));
					String[] arr = receive[1].split(",",2);
					System.out.println(arr[0]);
					System.out.println(arr[1]);
					if(id.equals(chatId)) {
						System.out.println(ServiceCode.CHATTING+"서버 연결 완료");

						startPanel.setVisible(false);
						quitPanel.setVisible(true);
						idInput.setText("");
						chattingArea.setEnabled(true);
						clientList.setEnabled(true);
						notice.setText("귓속말 보내기를 체크하시면 귓속말을 보내실 수 있습니다.");
						notice.setForeground(Color.BLACK);
						connectedIds.clear();
						String[] idLists = arr[1].split(",");
						for(int i=0;i<idLists.length;i++) {
							connectedIds.add(idLists[i]);
						}
						
						newMsg = "---------------------------- 입장하셨습니다. ----------------------------";
					}else {
						connectedIds.add(id);
						newMsg = arr[0];
					}
					clientList.setText("");
					for(int i=0;i<connectedIds.size();i++) {
						if(i%5==4) {
							clientList.append(connectedIds.get(i)+"\n");
						}else {
							clientList.append(connectedIds.get(i)+"\t");
						}
					}
				}
				if(receive[0].equals(ServiceCode.CHATTING_QUIT)){
					String id = receive[1].substring(receive[1].indexOf('[')+1,receive[1].lastIndexOf(']'));
					connectedIds.remove(id);
					clientList.setText("");
					for(int i=0;i<connectedIds.size();i++) {
						if(i%5==4) {
							clientList.append(connectedIds.get(i)+"\n");
						}else {
							clientList.append(connectedIds.get(i)+"\t");
						}
					}
					if(id.equals(chatId)) {
						socket.close();
						startPanel.setVisible(true);
						quitPanel.setVisible(false);
						chattingArea.setEnabled(false);
						clientList.setEnabled(false);
						clientList.setText("");
						chattingArea.setText("");
						textInput.setText("");
						notice.setText("3~10글자 한글, 영소대문자, 숫자만 입력가능 합니다.");
						notice.setForeground(Color.BLACK);
						System.out.println(ServiceCode.CHATTING+"연결 종료");
						break;
					}
				}
				if(receive[0].equals(ServiceCode.CHATTING_MSG)) {
					String id = receive[1].substring(receive[1].indexOf('[')+1,receive[1].lastIndexOf(']'));
					if(id.equals(chatId)) {
						newMsg = receive[1].replaceFirst(id, "- 나 -");
					}
				}
				if(receive[0].equals(ServiceCode.CHATTING_EXISTID)) {
					notice.setText("이미 사용중인 채팅명입니다. 다른 채팅명을 입력해주세요.");
					notice.setForeground(Color.RED);
					continue;
				}
				chattingArea.append(newMsg+"\n");
			}
		} catch (IOException e) {
		}
	}
}
