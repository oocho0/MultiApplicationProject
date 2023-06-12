package Client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ContectsTab extends JPanel implements ActionListener {
	private final String[] LABELS = {"그룹","이름","전화번호","주소"};
	private JLabel startLabel;
	private JRadioButton seoul;
	private JRadioButton busan;
	private JButton startButton;
	private JPanel searchPanel;
	private JPanel enterPanel;
	private JPanel inputOne;
	private JPanel inputTwo;
	private JScrollPane scrollTable;
	private JRadioButton byGroup;
	private JRadioButton byName;
	private JRadioButton byTel;
	private JRadioButton byAddr;
	private JTextField searchKeyword;
	private JButton search;
	private JButton total;
	private JButton modify;
	private JButton delete;
	private JLabel resultNotice;
	private JTable contectsTable;
	private JTextField inputGroup;
	private JTextField inputName;
	private JTextField inputTel;
	private JTextField inputAddr;
	private JButton save;
	private JButton reset;
	private JLabel inputNotice;
	private JButton quit;
	
	private final String IP;
	private Socket socket;
	private NetworkClient nc;
	private int receiveCode;
	private String sendInfo;
	private Map<String, Contects_Info> contects;
	
	public ContectsTab(String ip) {
		IP = ip;
		
		setLayout(null);
		startLabel = new JLabel("지부를 선택하세요");
		startLabel.setHorizontalAlignment(JLabel.CENTER);
		ButtonGroup dbType = new ButtonGroup();
		seoul = new JRadioButton("서울 지부 - 파일로 관리");
		seoul.setSelected(true);
		busan = new JRadioButton("부산 지부 - DB로 관리");
		dbType.add(seoul);
		dbType.add(busan);
		startButton = new JButton("선택");
		startButton.addActionListener(this);
		startLabel.setBounds(100, 90, 300, 30);
		seoul.setBounds(180, 130, 300, 30);
		busan.setBounds(180, 165, 300, 30);
		startButton.setBounds(100, 210, 300, 30);
		add(startLabel);
		add(seoul);
		add(busan);
		add(startButton);
		
		ButtonGroup searchBy = new ButtonGroup();
		searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		byGroup = new JRadioButton(LABELS[0]);
		byName = new JRadioButton(LABELS[1]);
		byName.setSelected(true);
		byTel = new JRadioButton(LABELS[2]);
		byAddr = new JRadioButton(LABELS[3]);
		searchBy.add(byGroup);
		searchBy.add(byName);
		searchBy.add(byTel);
		searchBy.add(byAddr);
		modify = new JButton("선택 수정");
		modify.addActionListener(this);
		delete = new JButton("선택 삭제");
		delete.addActionListener(this);
		searchPanel.add(byGroup);
		searchPanel.add(byName);
		searchPanel.add(byTel);
		searchPanel.add(byAddr);
		searchPanel.add(modify);
		searchPanel.add(delete);
		enterPanel = new JPanel();
		enterPanel.setLayout(new FlowLayout());
		total = new JButton("전체 검색");
		total.addActionListener(this);
		JLabel searchLabel = new JLabel("검색");
		searchKeyword = new JTextField(27);
		search = new JButton("찾기");
		search.addActionListener(this);
		enterPanel.add(total);
		enterPanel.add(searchLabel);
		enterPanel.add(searchKeyword);
		enterPanel.add(search);
		resultNotice = new JLabel("   ");
		resultNotice.setHorizontalAlignment(JLabel.CENTER);
		resultNotice.setForeground(Color.BLACK);
		
		DefaultTableModel tableModel = new DefaultTableModel(LABELS, 1);
		contectsTable = new JTable(tableModel);
		scrollTable = new JScrollPane(contectsTable);
		
		inputOne = new JPanel();
		inputOne.setLayout(new FlowLayout());
		JLabel inputGroupLabel = new JLabel(LABELS[0]);
		inputGroup = new JTextField(16);
		JLabel inputNameLabel = new JLabel(LABELS[1]);
		inputName = new JTextField(16);
		save = new JButton("저장");
		save.addActionListener(this);
		inputOne.add(inputGroupLabel);
		inputOne.add(inputGroup);
		inputOne.add(inputNameLabel);
		inputOne.add(inputName);
		inputOne.add(save);
		inputTwo = new JPanel();
		inputTwo.setLayout(new FlowLayout());
		JLabel inputTelLabel = new JLabel(LABELS[2]);
		inputTel = new JTextField(14);
		JLabel inputAddrLabel = new JLabel(LABELS[3]);
		inputAddr = new JTextField(16);
		reset = new JButton("초기화");
		reset.addActionListener(this);
		inputTwo.add(inputTelLabel);
		inputTwo.add(inputTel);
		inputTwo.add(inputAddrLabel);
		inputTwo.add(inputAddr);
		inputTwo.add(reset);
		
		inputNotice = new JLabel("연락처 추가");
		inputNotice.setHorizontalAlignment(JLabel.CENTER);
		inputNotice.setForeground(Color.BLACK);
		
		quit = new JButton("그만두기");
		quit.addActionListener(this);
		searchPanel.setBounds(5, 5, 487, 30);
		enterPanel.setBounds(5, 35, 487, 30);
		resultNotice.setBounds(5, 65, 487, 30);
		scrollTable.setBounds(5, 95, 487, 132);
		inputNotice.setBounds(5, 231, 487, 15);
		inputOne.setBounds(5, 242, 487, 30);
		inputTwo.setBounds(5, 272, 487, 30);
		quit.setBounds(5, 310, 487, 20);
		add(searchPanel);
		add(enterPanel);
		add(resultNotice);
		add(inputNotice);
		add(scrollTable);
		add(inputOne);
		add(inputTwo);
		add(quit);
		
		switchPanel(false);		
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startButton) {
			try {
				socket = new Socket(IP, ServiceCode.CONTECTS_PORT);
				nc = new NetworkClient(socket, ServiceCode.CONTECTS);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(seoul.isSelected()) {
				receiveCode = nc.sendCodeReceiveCode(ServiceCode.CONTECTS_REQUEST_FILE);
			}
			if(busan.isSelected()) {
				receiveCode = nc.sendCodeReceiveCode(ServiceCode.CONTECTS_REQUEST_DB);
			}
			if(receiveCode == ServiceCode.CONTECTS_ACCEPT) {
				System.out.println(ServiceCode.CONTECTS+"서버 연결 완료");
				switchPanel(true);
			}
		}
		if(e.getSource()==search) {
			String keyword = searchKeyword.getText().trim();
			if(keyword.equals("")) {
				resultNotice.setText("입력된 값이 없습니다.");
				resultNotice.setForeground(Color.RED);
				return;
			}
			if(byGroup.isSelected()) {
				
			}
			if(byName.isSelected()) {
				
			}
			if(byTel.isSelected()){
				
			}
			if(byAddr.isSelected()) {
				
			}
		}
		if(e.getSource()==total) {
			resultNotice.setText("전체 검색 : 총 검색 결과 ");
			resultNotice.setForeground(Color.BLACK);
		}
		if(e.getSource()==modify) {
			int[] infos =  contectsTable.getSelectedRows();
			if(infos.length>1) {
				resultNotice.setText("수정은 하나의 연락처만 선택 가능합니다.");
				resultNotice.setForeground(Color.RED);
				return;
			}
		}
		if(e.getSource()==delete) {
			contectsTable.getSelectedRows();
		}
		if(e.getSource()==save) {
			
		}
		if(e.getSource()==reset) {
			inputGroup.setText("");
			inputName.setText("");
			inputTel.setText("");
			inputAddr.setText("");
		}
		if(e.getSource()==quit) {
			resultNotice.setText("   ");
			resultNotice.setForeground(Color.BLACK);
			inputGroup.setText("");
			inputName.setText("");
			inputTel.setText("");
			inputAddr.setText("");
			switchPanel(false);
			nc = null;
			contects.clear();;
			try {
				socket.close();
			} catch (IOException e1) {
			}
			System.out.println(ServiceCode.CONTECTS+"연결 종료");
		}
	}
	public String infoString(int anInfo) {
		String str = "";
		for(int i=0;i<3;i++) {
			str += contectsTable.getValueAt(anInfo, i).toString()+",";
		}
		str += contectsTable.getValueAt(anInfo, 3).toString();
		return str;
	}
	
	public void switchPanel(boolean start) {
		startLabel.setVisible(!start);
		seoul.setVisible(!start);
		busan.setVisible(!start);
		startButton.setVisible(!start);
		searchPanel.setVisible(start);
		enterPanel.setVisible(start);
		resultNotice.setVisible(start);
		inputNotice.setVisible(start);
		scrollTable.setVisible(start);
		inputOne.setVisible(start);
		inputTwo.setVisible(start);
		quit.setVisible(start);
	}
}
