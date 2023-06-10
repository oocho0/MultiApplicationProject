package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Chat_Tab extends JPanel implements ActionListener {
	private JTextArea clientList;
	private JTextArea chattingArea;
	private JTextField textInput;
	private JTextField whisperIdInput;
	private JButton textSubmit;
	private JButton startButton;
	private JButton exitButton;
	private JTextField idInput;
	private JPanel quitPanel;
	private JPanel startPanel;
	private JLabel notice;
	
	private Socket socket;
	private String chatMsg;
	private NetworkClient nc;
	private final String IP;
	private ArrayList<String> connectedIds;
	private String chatId;
	
	public Chat_Tab(String ip) {
		IP = ip;
		connectedIds = new ArrayList<>();
		
		JPanel labelPanel = new JPanel();
		JLabel clientListLabel = new JLabel("|| 채팅 접속자 목록 ||");
		clientListLabel.setHorizontalAlignment(JLabel.CENTER);
		labelPanel.add(clientListLabel);
		clientList = new JTextArea();
		clientList.setEditable(false);
		clientList.setEnabled(false);
		JScrollPane listScroll = new JScrollPane(clientList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		chattingArea = new JTextArea();
		chattingArea.setEditable(false);
		chattingArea.setEnabled(false);
		JScrollPane chatScroll = new JScrollPane(chattingArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());
		textInput = new JTextField(36);
		textSubmit = new JButton("전송");
		textSubmit.addActionListener(this);
		textPanel.add(textInput);
		textPanel.add(textSubmit);
		
		quitPanel = new JPanel();
		JPanel whisperPanel = new JPanel();
		whisperPanel.setLayout(new FlowLayout());
		JCheckBox whisperNotice = new JCheckBox("귓속말 하기", false);
		whisperIdInput = new JTextField(20);
		whisperIdInput.setEnabled(false);
		whisperNotice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				whisperIdInput.setEnabled(cb.isSelected());
				notice.setText(cb.isSelected()? "귓속말할 아이디를 입력해주세요. 예시) Hong123, gil345, ...":"귓속말 보내기를 체크하시면 귓속말을 보내실 수 있습니다.");
				notice.setForeground(Color.BLACK);
			}
		});
		exitButton = new JButton("채팅 끝내기");
		exitButton.addActionListener(this);
		whisperPanel.add(whisperNotice);
		whisperPanel.add(whisperIdInput);
		whisperPanel.add(exitButton);
		quitPanel.setLayout(null);
		textPanel.setBounds(0, -8, 487, 30);
		whisperPanel.setBounds(0, 18, 487, 30);
		quitPanel.add(textPanel);
		quitPanel.add(whisperPanel);
		
		startPanel = new JPanel();
		JLabel chattingNameLabel = new JLabel("채팅명을 입력하세요.");
		chattingNameLabel.setHorizontalAlignment(JLabel.CENTER);
		chattingNameLabel.setOpaque(true);
		chattingNameLabel.setForeground(Color.BLACK);
		JPanel inputPanel = new JPanel();
		idInput = new JTextField(36);
		startButton = new JButton("채팅 시작하기");
		startButton.addActionListener(this);
		inputPanel.setLayout(new FlowLayout());
		inputPanel.add(idInput);
		inputPanel.add(startButton);
		startPanel.setLayout(new BorderLayout());
		startPanel.add(chattingNameLabel,BorderLayout.NORTH);
		startPanel.add(inputPanel,BorderLayout.CENTER);
		
		notice = new JLabel("3~10글자 한글, 영소대문자, 숫자만 입력가능 합니다.");
		notice.setHorizontalAlignment(JLabel.CENTER);
		setLayout(null);
		labelPanel.setBounds(5, 0, 487, 20);
		listScroll.setBounds(5, 25, 487, 40);
		chatScroll.setBounds(5, 70, 487, 180);
		notice.setBounds(5,252,487,27);
		startPanel.setBounds(5, 282, 487, 75);
		quitPanel.setBounds(5, 282, 487, 75);
		add(labelPanel);
		add(listScroll);
		add(chatScroll);
		add(notice);
		add(startPanel);
		add(quitPanel);
		
		startPanel.setVisible(true);
		quitPanel.setVisible(false);
		
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startButton) {
			try {
				chatId = idInput.getText().trim();
				if(CheckLetters.isEmpty(chatId)) {
					notice.setText("채팅명을 입력하세요. 3~10글자 한글, 영소대문자, 숫자");
					idInput.setText("");
					notice.setForeground(Color.RED);
					return;
				}
				if(CheckLetters.isWrongId(chatId)) {
					notice.setText("채팅명을 확인하세요. 3~10글자 한글, 영소대문자, 숫자만 특수문자X");
					idInput.setText("");
					notice.setForeground(Color.RED);
					return;
				}
				socket = new Socket(IP,ServiceCode.CHATTING_PORT);
				nc = new NetworkClient(socket, ServiceCode.CHATTING);
				nc.sendMsg(ServiceCode.CHATTING_REQUEST,chatId);
				Chat_ReceiveThread crt = new Chat_ReceiveThread(socket, connectedIds, chatId);
				crt.setGUI(chattingArea, clientList, startPanel, quitPanel, idInput, notice, textInput);
				crt.start();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==textSubmit) {
			chatMsg = textInput.getText();
			if(whisperIdInput.isEnabled()) {
				String sendMsg = "";
				String[] ids = whisperIdInput.getText().split(",");
				String idList = "";
				for(String id : ids) {
					id = id.trim();
					if(!connectedIds.contains(id)) {
						notice.setText("귓속말할 아이디가 올바르지 않습니다.");
						notice.setForeground(Color.RED);
					}else {
						idList += id+",";
					}
				}
				idList += ServiceCode.CHATTING_ENDID;
				sendMsg = idList+chatMsg;
				nc.sendMsg(ServiceCode.CHATTING_WHISPER, sendMsg);
			}else {
				nc.sendMsg(ServiceCode.CHATTING_SEND, chatMsg);
			}
		}
		if(e.getSource()==exitButton) {
			nc.sendCode(ServiceCode.CHATTING_EXIT);
		}
	}
}
