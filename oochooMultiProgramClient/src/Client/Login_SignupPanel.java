package Client;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login_SignupPanel extends JPanel implements ActionListener {
	private JDialog signupPop;
	private JLabel signupNameLabel;
	private JTextField signupName;
	private JLabel signupIDLabel;
	private JTextField signupID;
	private JLabel signupPWLabel;
	private JPasswordField signupPW;
	private JLabel signupEmailLabel;
	private JTextField signupEmail;
	private JButton signupSubmit;
	private JButton cancelButton;
	
	private String sendMsg;
	private int receiveCode;
	private NetworkClient nc;
	
	public Login_SignupPanel(JDialog signupPop, NetworkClient nc) {
		this.signupPop = signupPop;
		this.nc = nc;
		
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		for(int i= 0;i<3;i++) {
			gbc[i] = new GridBagConstraints();
			gbc[i].gridx = i;
			gbc[i].gridx = i;
		}
		
		JPanel signup = new JPanel();
		JPanel newName = new JPanel();
		newName.setLayout(new FlowLayout());
		JPanel newID = new JPanel();
		newID.setLayout(new FlowLayout());
		JPanel newPW = new JPanel();
		newPW.setLayout(new FlowLayout());
		JPanel newEmail = new JPanel();
		newEmail.setLayout(new FlowLayout());
		signupNameLabel = new JLabel("이름");
		signupName = new JTextField(25);
		newName.add(signupNameLabel);
		newName.add(signupName);
		signupIDLabel = new JLabel("아이디");
		signupID = new JTextField(25);
		newID.add(signupIDLabel);
		newID.add(signupID);
		signupPWLabel = new JLabel("비밀번호");
		signupPW = new JPasswordField(25);
		newPW.add(signupPWLabel);
		newPW.add(signupPW);
		signupEmailLabel = new JLabel("이메일");
		signupEmail = new JTextField(25);
		newEmail.add(signupEmailLabel);
		newEmail.add(signupEmail);
		signupSubmit = new JButton("회원가입 하기");
		signupSubmit.addActionListener(this);
		cancelButton = new JButton("취소");
		cancelButton.addActionListener(this);
		signup.setLayout(new BoxLayout(signup, BoxLayout.Y_AXIS));
		signup.add(newName);
		signup.add(newID);
		signup.add(newPW);
		signup.add(newEmail);
		signup.add(signupSubmit);
		add(signup);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==signupSubmit) {
			String newName = signupName.getText().trim();
			String newId = signupID.getText().trim();
			String newPW = new String(signupPW.getPassword()).trim();
			String newEmail = signupEmail.getText().trim();
			if(newName.equals("")){
				signupName.setText("이름을 입력하지 않았습니다.");
				return;
			}
			if(newId.equals("")) {
				signupID.setText("아이디를 입력하지 않았습니다.");
				return;
			}
			if(newPW.equals("")){
				signupPW.setText("비밀번호를 입력하지 않았습니다.");
				return;
			}
			if(newEmail.equals("")) {
				signupEmail.setText("이메일을 입력하지 않았습니다.");
				return;
			}
			sendMsg = newName+","+newId+","+newPW+","+newEmail;
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.SIGNUP_SEND,sendMsg);
			if(receiveCode == ServiceCode.SIGNUP_EXIST_ID) {
				signupID.setText("이미 존재하는 아이디 입니다.");
				return;
			}
			if(receiveCode == ServiceCode.SIGNUP_EXIST_EMAIL) {
				signupEmail.setText("이미 존재하는 이메일 입니다.");
			}
			if(receiveCode == ServiceCode.SIGNUP_SUCCESS) {
				signupName.setText("");
				signupID.setText("");
				signupPW.setText("");
				signupEmail.setText("");
				signupPop.setVisible(false);
				System.out.println(ServiceCode.LOGIN+"회원가입 성공");
			}		
		}
		if(e.getSource()==cancelButton) {
			signupName.setText("");
			signupID.setText("");
			signupPW.setText("");
			signupEmail.setText("");
			signupPop.setVisible(false);
			System.out.println(ServiceCode.LOGIN+"회원가입 취소");
		}
	}
}