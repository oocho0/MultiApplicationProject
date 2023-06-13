package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient {
	Socket socket;
	DataOutputStream dos;
	DataInputStream dis;
	String serviceName;
	public NetworkClient(Socket socket, String serviceName) {
		this.socket = socket;
		this.serviceName = serviceName;
		
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int sendCodeReceiveCode(int serviceCode) {
		int receiveCode = 0;
		try {
			dos.writeInt(serviceCode);
			System.out.println(serviceName + serviceCode+" SEND");
			receiveCode = dis.readInt();
			System.out.println(serviceName + receiveCode+" RECEIVE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receiveCode;
	}
	
	public String sendCodeReceiveMsg(int serviceCode) {
		String receiveMsg = "";
		try {
			dos.writeInt(serviceCode);
			System.out.println(serviceName + serviceCode+" SEND");
			receiveMsg = dis.readUTF();
			System.out.println(serviceName + receiveMsg+ " RECEIVE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receiveMsg;
	}
	
	public int sendMsgReceiveCode(int serviceCode, String sendmsg) {
		int receiveCode = 0;
		try {
			dos.writeInt(serviceCode);
			dos.writeUTF(sendmsg);
			System.out.println(serviceName + sendmsg+" SEND");
			receiveCode = dis.readInt();
			System.out.println(serviceName + receiveCode+" RECEIVE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receiveCode;
	}
	
	public String sendMsgReceiveMsg(int serviceCode, String sendmsg) {
		String receivemsg = "";
		try {
			dos.writeInt(serviceCode);
			dos.writeUTF(sendmsg);
			System.out.println(serviceName + sendmsg+" SEND");
			receivemsg = dis.readUTF();
			System.out.println(serviceName + receivemsg+" RECEIVE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receivemsg;
	}
	
	public void sendMsg(int serviceCode, String sendmsg) {
		try {
			dos.writeInt(serviceCode);
			dos.writeUTF(sendmsg);
			System.out.println(serviceName + sendmsg+" SEND");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCode(int ServiceCode) {
		try {
			dos.writeInt(ServiceCode);
			System.out.println(serviceName + ServiceCode+" SEND");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
