package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class BaseballGame_Frame extends JFrame implements ActionListener, DocumentListener{
	private BaseballGame_CountPanel baseballGamePanel;
	private JTextField input1;
	private JTextField input2;
	private JTextField input3;
	private JLabel label;
	private JLabel noticeInputLabel;
	private JButton button;
	private JButton stop;
	private JButton restart;

	private final String IP;
	private Socket socket;
	private int receiveCode;
	private String receiveStr;
	private String sendNum;
	private NetworkClient nc;
	
	public BaseballGame_Frame(String ip) throws HeadlessException {
		IP = ip;
		
		setUIFont(new FontUIResource(new Font("Apple SD Gothic Neo", Font.PLAIN, 12)));
		
		JPanel iPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		label = new JLabel("번호를 입력하세요!");
		label.setHorizontalAlignment(JLabel.CENTER);
		input1 = new JTextField(1);
		input1.getDocument().addDocumentListener(this);
		input2 = new JTextField(1);
		input2.getDocument().addDocumentListener(this);
		input3 = new JTextField(1);
		input3.getDocument().addDocumentListener(this);
		button = new JButton("입력");
		button.addActionListener(this);
		noticeInputLabel = new JLabel("1~9 중 세 숫자를 중복없이 입력하세요.");
		noticeInputLabel.setOpaque(true);
		noticeInputLabel.setForeground(Color.BLACK);
		inputPanel.add(input1);
		inputPanel.add(input2);
		inputPanel.add(input3);
		inputPanel.add(button);
		inputPanel.add(noticeInputLabel);
		iPanel.setLayout(new BorderLayout());
		iPanel.add(label,BorderLayout.NORTH);
		iPanel.add(inputPanel,BorderLayout.SOUTH);
		
		
		baseballGamePanel = new BaseballGame_CountPanel();
		JPanel buttonPanel = new JPanel();
		stop = new JButton("게임 그만두기");
		stop.addActionListener(this);
		restart = new JButton("게임 다시 시작하기");
		restart.addActionListener(this);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(stop);
		buttonPanel.add(restart);
		setLayout(new BorderLayout());
		add(iPanel,BorderLayout.NORTH);
		add(baseballGamePanel,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				try {
					socket = new Socket(IP, ServiceCode.BASEBALLGAME_PORT);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				nc = new NetworkClient(socket, ServiceCode.BASEBALLGAME);
				receiveCode = nc.sendCodeReceiveCode(ServiceCode.BASEBALLGAME_REQUEST);
				if(receiveCode == ServiceCode.BASEBALLGAME_ACCEPT) {
					baseballGamePanel.setData(0, 0, 0);
					baseballGamePanel.repaint();
					button.setVisible(true);
					System.out.println(ServiceCode.BASEBALLGAME+"서버 연결 완료");
				}
			}
		});
		
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension frameSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width)/2, (screenSize.height-frameSize.width)/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == stop) {
			label.setText("번호를 입력하세요!");
			input1.setText("");
			input2.setText("");
			input3.setText("");
			Main_FrameSwitch sf = Main_FrameSwitch.getInstance();
			sf.baseballGameFrame.setVisible(false);
			sf.menuFrame.setVisible(true);
			try {
				socket.close();
				System.out.println(ServiceCode.BASEBALLGAME+"메뉴 창으로 전환");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == restart) {
			receiveCode = nc.sendCodeReceiveCode(ServiceCode.BASEBALLGAME_RESTART);
			if(receiveCode == ServiceCode.BASEBALLGAME_RESET) {
				label.setText("번호를 입력하세요!");
				input1.setText("");
				input2.setText("");
				input3.setText("");
				baseballGamePanel.setData(0, 0, 0);
				baseballGamePanel.repaint();
				button.setVisible(true);
				System.out.println(ServiceCode.BASEBALLGAME+"새로운 게임 시작");
			}
			
		}
		if(e.getSource() == button) {
			sendNum = "";
			String[] input = new String[3];
			input[0] = input1.getText().trim();
			input[1] = input2.getText().trim();
			input[2] = input3.getText().trim();
			for(int i=0;i<input.length;i++) {
				if(input[i].equals("")) {
					noticeInputLabel.setText("번호를 입력하지 않았습니다.");
					noticeInputLabel.setForeground(Color.RED);
					return;
				}
				sendNum += input[i]+",";
			}
			if(input[0] == input[1]||input[0]==input[2]||input[1]==input[2]) {
				noticeInputLabel.setText("중복된 번호가 있습니다. 다시 입력하세요.");
				noticeInputLabel.setForeground(Color.RED);
				return;
			}
			receiveStr = nc.sendMsgReceiveMsg(ServiceCode.BASEBALLGAME_SEND,sendNum);
			String[] resultStr = receiveStr.split(",",3);
			int[] result = new int[resultStr.length];
			for(int i=0;i<resultStr.length;i++) {
				result[i] = Integer.parseInt(resultStr[i]);
			}
			baseballGamePanel.setData(result[0], result[1], result[2]);
			baseballGamePanel.repaint();
			if(result[0]==3) {
				label.setText("축하합니다. 승리하셨습니다!");
				button.setVisible(false);
				return;
			}
			label.setText("[현재 스코어] strike "+resultStr[0]+" ball "+resultStr[1]+" out "+resultStr[2]);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkTextField();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkTextField();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
	
	public void checkTextField() {
		if(CheckLetters.isWrongGameInput(input1.getText())&&input1.getText().trim().equals("")){
			noticeInputLabel.setText("한자리 숫자만 입력해주세요.");
			noticeInputLabel.setForeground(Color.RED);
			return;
		}
		if(CheckLetters.isWrongGameInput(input2.getText())&&input2.getText().trim().equals("")) {
			noticeInputLabel.setText("한자리 숫자만 입력해주세요.");
			noticeInputLabel.setForeground(Color.RED);
			return;
		}
		if(CheckLetters.isWrongGameInput(input3.getText())&&input3.getText().trim().equals("")) {
			noticeInputLabel.setText("한자리 숫자만 입력해주세요.");
			noticeInputLabel.setForeground(Color.RED);
			return;
		}
		noticeInputLabel.setText("");
		noticeInputLabel.setForeground(Color.BLACK);
	}
	
	public static void setUIFont(FontUIResource f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource) {
				FontUIResource orig = (FontUIResource) value;
				Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}

}
