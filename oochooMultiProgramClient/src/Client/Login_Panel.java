package Client;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login_Panel extends JPanel implements ActionListener {
	
	private JDialog loginPop;
	private JLabel loginIDLabel;
	private JTextField loginID;
	private JLabel loginPWLabel;
	private JPasswordField loginPW;
	private JButton loginSubmit;
	private JButton goSignup;
	private JDialog signupPop;
	private Login_SignupPanel signupPanel;
	
	private final String IP;
	private Socket socket;
	private String sendMsg;
	private int receiveCode;
	private NetworkClient nc;
	
	public Login_Panel(JDialog loginPop, String ip) {
		this.loginPop = loginPop;
		IP = ip;
		
		try {
			socket = new Socket(IP, ServiceCode.LOGIN_PORT);
			nc = new NetworkClient(socket, ServiceCode.LOGIN);
			receiveCode = nc.sendCodeReceiveCode(ServiceCode.LOGIN_REQUEST);
			if(receiveCode == ServiceCode.LOGIN_ACCEPT) {
				System.out.println(ServiceCode.LOGIN+"서버 연결 완료");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		for(int i= 0;i<3;i++) {
			gbc[i] = new GridBagConstraints();
			gbc[i].gridx = i;
			gbc[i].gridx = i;
		}
		
		JPanel panel = new JPanel();
		JPanel login = new JPanel();
		JPanel id = new JPanel();
		id.setLayout(new FlowLayout());
		JPanel pw = new JPanel();
		pw.setLayout(new FlowLayout());
		JPanel button = new JPanel();
		button.setLayout(new FlowLayout());
		loginIDLabel = new JLabel("아이디");
		loginID = new JTextField(25);
		id.add(loginIDLabel);
		id.add(loginID);
		loginPWLabel = new JLabel("비밀번호");
		loginPW = new JPasswordField(25);
		pw.add(loginPWLabel);
		pw.add(loginPW);
		loginSubmit = new JButton("로그인 하기");
		loginSubmit.addActionListener(this);
		goSignup = new JButton("회원가입 하기");
		goSignup.addActionListener(this);
		button.add(loginSubmit);
		button.add(goSignup);
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
		login.add(id);
		login.add(pw);
		login.add(button);
		panel.setLayout(new GridBagLayout());
		panel.add(login,gbc[1]);
		add(panel);
		
		signupPop = new JDialog();
		signupPop.setSize(500, 300);
		signupPop.setModal(true);
		signupPop.setTitle("회원가입");
		signupPop.setLocationRelativeTo(this);
		
		signupPanel = new Login_SignupPanel(signupPop, nc);
		signupPop.setLayout(new GridBagLayout());
		signupPop.add(signupPanel, gbc[1]);
		signupPop.setVisible(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginSubmit) {
			String id = loginID.getText().trim();
			String pw = new String(loginPW.getPassword()).trim();
			if(id.equals("")){
				loginID.setText("아이디를 입력하지 않았습니다.");
				return;
			}
			if(pw.equals("")) {
				loginPW.setText("비밀번호를 입력하지 않았습니다.");
				return;
			}
			sendMsg = id+","+pw;
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.LOGIN_SEND,sendMsg);
			if(receiveCode == ServiceCode.LOGIN_NO_ID) {
				loginID.setText("존재하지 않는 아이디 입니다.");
				return;
			}
			if(receiveCode == ServiceCode.LOGIN_NO_PW) {
				loginPW.setText("비밀번호가 맞지 않습니다.");
				return;
			}
			if(receiveCode == ServiceCode.LOGIN_SUCCESS) {
				loginID.setText("");
				loginPW.setText("");
				System.out.println(ServiceCode.LOGIN+"로그인 성공");
				try {
					socket.close();
				} catch (IOException e1) {}
				System.out.println(ServiceCode.LOGIN+"연결 종료");
				loginPop.setVisible(false);
			}
		}
		if(e.getSource() == goSignup) {
			signupPop.setVisible(true);
		}
		
	}
}
