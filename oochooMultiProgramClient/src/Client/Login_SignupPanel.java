package Client;

import java.awt.Color;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class Login_SignupPanel extends JPanel implements ActionListener {
	private JDialog signupPop;
	private JTextField signupName;
	private JLabel nameNotice;
	private JTextField signupID;
	private JLabel idNotice;
	private JPasswordField signupPW;
	private JLabel pwNotice;
	private JPasswordField signupPWCheck;
	private JLabel pwCheckNotice;
	private JTextField signupEmail;
	private JLabel emailNotice;
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
		JPanel PWCheck = new JPanel();
		PWCheck.setLayout(new FlowLayout());
		JPanel newEmail = new JPanel();
		newEmail.setLayout(new FlowLayout());
		JLabel signupNameLabel = new JLabel("이름");
		signupName = new JTextField(25);
		signupName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {check();}
			@Override
			public void insertUpdate(DocumentEvent e) {check();}
			@Override
			public void changedUpdate(DocumentEvent e) {}
			public void check() {
				String letters = signupName.getText();
				if((!letters.equals(""))&&CheckLetters.isWrongName(letters)){
					nameNotice.setText("* 국어, 영소대문자, 숫자만 가능");
					nameNotice.setForeground(Color.RED);
					return;
				}
				nameNotice.setText("국어, 영소대문자, 숫자만 가능");
				nameNotice.setForeground(Color.BLACK);
			}
		});
		newName.add(signupNameLabel);
		newName.add(signupName);
		nameNotice = new JLabel("국어, 영소대문자, 숫자만 가능");
		JLabel signupIDLabel = new JLabel("아이디");
		signupID = new JTextField(25);
		signupID.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {check();}
			@Override
			public void insertUpdate(DocumentEvent e) {check();}
			@Override
			public void changedUpdate(DocumentEvent e) {}
			public void check() {
				String letters = signupID.getText();
				if((!letters.equals(""))&&CheckLetters.isWrongId(letters)){
					idNotice.setText("* 3~10글자, 영소대문자, 숫자만 가능");
					idNotice.setForeground(Color.RED);
					return;
				}
				idNotice.setText("3~10글자, 영소대문자, 숫자만 가능");
				idNotice.setForeground(Color.BLACK);
			}
		});
		newID.add(signupIDLabel);
		newID.add(signupID);
		idNotice = new JLabel("3~10글자, 영소대문자, 숫자만 가능");
		JLabel signupPWLabel = new JLabel("비밀번호");
		signupPW = new JPasswordField(25);
		signupPW.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {check();}
			@Override
			public void insertUpdate(DocumentEvent e) {check();}
			@Override
			public void changedUpdate(DocumentEvent e) {}
			public void check() {
				String letters = new String(signupPW.getPassword());
				if((!letters.equals(""))&&CheckLetters.isWrongPW(letters)){
					pwNotice.setText("* 4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
					pwNotice.setForeground(Color.RED);
					return;
				}
				pwNotice.setText("4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
				pwNotice.setForeground(Color.BLACK);
			}
		});
		newPW.add(signupPWLabel);
		newPW.add(signupPW);
		pwNotice = new JLabel("4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
		JLabel signupPWCheckLabel = new JLabel("비밀번호 확인");
		signupPWCheck = new JPasswordField(25);
		PWCheck.add(signupPWCheckLabel);
		PWCheck.add(signupPWCheck);
		pwCheckNotice = new JLabel("   ");
		pwCheckNotice.setForeground(Color.RED);
		JLabel signupEmailLabel = new JLabel("이메일");
		signupEmail = new JTextField(25);
		newEmail.add(signupEmailLabel);
		newEmail.add(signupEmail);
		emailNotice = new JLabel("   ");
		emailNotice.setForeground(Color.RED);
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		signupSubmit = new JButton("회원가입 하기");
		signupSubmit.addActionListener(this);
		cancelButton = new JButton("취소");
		cancelButton.addActionListener(this);
		buttons.add(signupSubmit);
		buttons.add(cancelButton);
		signup.setLayout(new BoxLayout(signup, BoxLayout.Y_AXIS));
		signup.add(newName);
		signup.add(nameNotice);
		signup.add(newID);
		signup.add(idNotice);
		signup.add(newPW);
		signup.add(pwNotice);
		signup.add(PWCheck);
		signup.add(pwCheckNotice);
		signup.add(newEmail);
		signup.add(emailNotice);
		signup.add(buttons);
		add(signup);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==signupSubmit) {
			nameNotice.setText("국어, 영소대문자, 숫자만 가능");
			nameNotice.setForeground(Color.BLACK);
			idNotice.setText("3~10글자, 영소대문자, 숫자만 가능");
			idNotice.setForeground(Color.BLACK);
			pwNotice.setText("4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
			pwNotice.setForeground(Color.BLACK);
			pwCheckNotice.setText("   ");
			emailNotice.setText("   ");
			String newName = signupName.getText().trim();
			String newId = signupID.getText().trim();
			String newPW = new String(signupPW.getPassword()).trim();
			String PWCheck = new String(signupPWCheck.getPassword()).trim();
			String newEmail = signupEmail.getText().trim();
			if(newName.equals("")){
				nameNotice.setText("이름을 입력하지 않았습니다.");
				nameNotice.setForeground(Color.RED);
				return;
			}
			if(CheckLetters.isWrongName(newName)) {
				nameNotice.setText("* 국어, 영소대문자, 숫자만 가능");
				nameNotice.setForeground(Color.RED);
				return;
			}
			if(newId.equals("")) {
				idNotice.setText("아이디를 입력하지 않았습니다.");
				idNotice.setForeground(Color.RED);
				return;
			}
			if(CheckLetters.isWrongId(newId)) {
				idNotice.setText("* 3~10글자, 영소대문자, 숫자만 가능");
				idNotice.setForeground(Color.RED);
				return;
			}
			if(newPW.equals("")){
				pwNotice.setText("비밀번호를 입력하지 않았습니다.");
				pwNotice.setForeground(Color.RED);
				return;
			}
			if(CheckLetters.isWrongPW(newPW)) {
				pwNotice.setText("* 4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
				pwNotice.setForeground(Color.RED);
				return;
			}
			if(PWCheck.equals("")) {
				pwCheckNotice.setText("비밀번호 확인을 입력하지 않았습니다.");
				return;
			}
			if(!newPW.equals(PWCheck)) {
				pwCheckNotice.setText("비밀번호가 일치하지 않습니다.");
				return;
			}
			if(newEmail.equals("")) {
				emailNotice.setText("이메일을 입력하지 않았습니다.");
				return;
			}
			if(CheckLetters.isWrongEmail(newEmail)) {
				emailNotice.setText("올바르지 않은 이메일입니다.");
				return;
			}
			sendMsg = newName+","+newId+","+newPW+","+newEmail;
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.SIGNUP_SEND,sendMsg);
			if(receiveCode == ServiceCode.SIGNUP_EXIST_ID) {
				idNotice.setText("이미 존재하는 아이디 입니다.");
				idNotice.setForeground(Color.RED);
				return;
			}
			if(receiveCode == ServiceCode.SIGNUP_EXIST_EMAIL) {
				emailNotice.setText("이미 존재하는 이메일 입니다.");
				return;
			}
			if(receiveCode == ServiceCode.SIGNUP_SUCCESS) {
				signupName.setText("");
				signupID.setText("");
				signupPW.setText("");
				signupPWCheck.setText("");
				signupEmail.setText("");
				signupPop.setVisible(false);
				System.out.println(ServiceCode.LOGIN+"회원가입 성공");
			}		
		}
		if(e.getSource()==cancelButton) {
			signupName.setText("");
			signupID.setText("");
			signupPW.setText("");
			signupPWCheck.setText("");
			signupEmail.setText("");
			signupPop.setVisible(false);
			nameNotice.setText("국어, 영소대문자, 숫자만 가능");
			nameNotice.setForeground(Color.BLACK);
			idNotice.setText("3~10글자, 영소대문자, 숫자만 가능");
			idNotice.setForeground(Color.BLACK);
			pwNotice.setText("4~12글자, 영소대문자, 숫자, 특수문자 하나 이상씩 필수");
			pwNotice.setForeground(Color.BLACK);
			pwCheckNotice.setText("   ");
			emailNotice.setText("   ");
			System.out.println(ServiceCode.LOGIN+"회원가입 취소");
		}
	}
}