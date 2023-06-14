package Client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
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
	private final String[] LABELS = {"번호","부서","이름","전화번호","주소"};
	private JLabel startLabel;
	private JRadioButton seoul;
	private JRadioButton busan;
	private JButton startButton;
	private JPanel searchPanel;
	private JPanel enterPanel;
	private JPanel inputOne;
	private JPanel inputTwo;
	private DefaultTableModel tableModel;
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
	private JLabel inputLabel;
	private JLabel inputNotice;
	private JButton quit;
	
	private final String IP;
	private Socket socket;
	private NetworkClient nc;
	private int receiveCode;
	private String receiveInfos;
	private Map<Integer, Contects_Info> contects;
	private Contects_Info modifyingInfo;
	
	public ContectsTab(String ip) {
		IP = ip;
		contects = new HashMap<>();
		
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
		byGroup = new JRadioButton(LABELS[1]);
		byName = new JRadioButton(LABELS[2]);
		byName.setSelected(true);
		byTel = new JRadioButton(LABELS[3]);
		byAddr = new JRadioButton(LABELS[4]);
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
		
		tableModel = new DefaultTableModel(LABELS, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		contectsTable = new JTable(tableModel);
		scrollTable = new JScrollPane(contectsTable);
		
		inputOne = new JPanel();
		inputOne.setLayout(new FlowLayout());
		JLabel inputGroupLabel = new JLabel(LABELS[1]);
		inputGroup = new JTextField(16);
		JLabel inputNameLabel = new JLabel(LABELS[2]);
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
		JLabel inputTelLabel = new JLabel(LABELS[3]);
		inputTel = new JTextField(14);
		JLabel inputAddrLabel = new JLabel(LABELS[4]);
		inputAddr = new JTextField(16);
		reset = new JButton("초기화");
		reset.addActionListener(this);
		inputTwo.add(inputTelLabel);
		inputTwo.add(inputTel);
		inputTwo.add(inputAddrLabel);
		inputTwo.add(inputAddr);
		inputTwo.add(reset);
		
		JPanel labels = new JPanel();
		labels.setLayout(new FlowLayout());
		inputLabel = new JLabel("|| 연락처 추가 ||");
		inputNotice = new JLabel("         ");
		inputNotice.setForeground(Color.BLACK);
		labels.add(inputLabel);
		labels.add(inputNotice);
		
		quit = new JButton("그만두기");
		quit.addActionListener(this);
		searchPanel.setBounds(5, 5, 487, 30);
		enterPanel.setBounds(5, 35, 487, 30);
		resultNotice.setBounds(5, 65, 487, 30);
		scrollTable.setBounds(5, 95, 487, 132);
		labels.setBounds(5, 229, 487, 17);
		inputOne.setBounds(5, 242, 487, 30);
		inputTwo.setBounds(5, 272, 487, 30);
		quit.setBounds(5, 310, 487, 20);
		add(searchPanel);
		add(enterPanel);
		add(resultNotice);
		add(labels);
		add(scrollTable);
		add(inputOne);
		add(inputTwo);
		add(quit);
		
		switchPanel(false);		
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		inputNotice.setText("");
		inputNotice.setForeground(Color.BLACK);
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
			tableReset();
			String keyword = searchKeyword.getText().trim();
			String by = "";
			if(keyword.equals("")) {
				resultNotice.setText("입력된 값이 없습니다.");
				resultNotice.setForeground(Color.RED);
				return;
			}
			if(byGroup.isSelected()) {
				receiveInfos = nc.sendMsgReceiveMsg(ServiceCode.CONTECTS_SEARCH_BYGROUP, keyword);
				by = "부서명으로";
			}
			if(byName.isSelected()) {
				receiveInfos = nc.sendMsgReceiveMsg(ServiceCode.CONTECTS_SEARCH_BYNAME, keyword);
				by = "이름으로";
			}
			if(byTel.isSelected()){
				receiveInfos = nc.sendMsgReceiveMsg(ServiceCode.CONTECTS_SEARCH_BYTEL, keyword);
				by = "전화번호로";
			}
			if(byAddr.isSelected()) {
				receiveInfos = nc.sendMsgReceiveMsg(ServiceCode.CONTECTS_SEARCH_BYADDR, keyword);
				by = "주소로";
			}
			contects.clear();
			tableReset();
			String[] arr = receiveInfos.split(",",2);
			if(arr[0].equals(ServiceCode.CONTECTS_RESULT)) {
				String[] infos = arr[1].split("\\*");
				int i = 1;
				for(String a: infos) {
					String[] info = a.split(",");
					contects.put(i, new Contects_Info(i++, info[0], info[1], info[2], info[3], info[4]));
				}
				contectsToTable();
				resultNotice.setText("|| "+by+" '"+keyword+"' 검색 || 총 "+contects.size()+"건");
				resultNotice.setForeground(Color.BLACK);
				searchKeyword.setText("");
			}
			if(arr[0].equals(ServiceCode.CONTECTS_NODATA)) {
				resultNotice.setText("|| "+by+" '"+keyword+"' 검색 || 결과가 없습니다.");
				resultNotice.setForeground(Color.BLACK);
				searchKeyword.setText("");
			}
		}
		if(e.getSource()==total) {
			contects.clear();
			tableReset();
			receiveInfos = nc.sendCodeReceiveMsg(ServiceCode.CONTECTS_ALL);
			String[] arr = receiveInfos.split(",",2);
			if(arr[0].equals(ServiceCode.CONTECTS_RESULT)) {
				String[] infos = arr[1].split("\\*");
				int i = 1;
				for(String a : infos) {
					String[] info = a.split(",");
					contects.put(i, new Contects_Info(i++, info[0], info[1], info[2], info[3], info[4]));
				}
				contectsToTable();
				resultNotice.setText("|| 전체 검색 || 총 "+contects.size()+"건");
				resultNotice.setForeground(Color.BLACK);
			}
			if(arr[0].equals(ServiceCode.CONTECTS_NODATA)) {
				resultNotice.setText("|| 전체 검색 || 결과가 없습니다.");
				resultNotice.setForeground(Color.BLACK);
			}
		}
		if(e.getSource()==modify) {
			colorCheck(resultNotice);
			int[] infos =  contectsTable.getSelectedRows();
			if(infos.length==0) {
				resultNotice.setText("수정할 연락처를 선택하세요.");
				resultNotice.setForeground(Color.RED);
				return;
			}
			if(infos.length>1) {
				resultNotice.setText("수정은 하나의 연락처만 선택 가능합니다.");
				resultNotice.setForeground(Color.RED);
				return;
			}
			inputLabel.setText("|| 연락처 수정 ||");
			save.setText("수정");
			reset.setText("취소");
			modifyingInfo = contects.get(infos[0]+1);
			inputGroup.setText(modifyingInfo.getGroup());
			inputName.setText(modifyingInfo.getName());
			inputTel.setText(modifyingInfo.getTel());
			inputAddr.setText(modifyingInfo.getAddress());
		}
		if(e.getSource()==delete) {
			colorCheck(resultNotice);
			int[] infos = contectsTable.getSelectedRows();
			if(infos.length==0) {
				resultNotice.setText("삭제할 연락처를 선택하세요.");
				resultNotice.setForeground(Color.RED);
				return;
			}
			String notice = "";
			String sendInfo = "";
			int origin = contects.size();
			for(int a : infos) {
				Contects_Info anInfo = contects.get(a+1);
				sendInfo += anInfo.getSerialNo()+",";
			}
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.CONTECTS_DELETE, sendInfo);
			if(receiveCode == ServiceCode.CONTECTS_SUCCESS) {
				for(int a:infos) {
					Contects_Info anInfo = contects.get(a+1);
					if(modifyingInfo == anInfo) {
						notice = "수정 중이던 연락처가 삭제되었습니다. | ";
						save.setText("저장");
						reset.setText("초기화");
						resetInputInfo();
						inputLabel.setText("|| 연락처 추가 ||");
						modifyingInfo = null;
					}
					contects.remove(a+1);
				}
				int j = 1;
				for(int i=1;i<=origin;i++) {
					if(contects.get(i)==null) {
						continue;
					}
					if(contects.get(i).getNo()==j) {
						j++;
					}else {
						contects.get(i).setNo(j);
						contects.put(j, contects.get(i));
						contects.remove(i);
						j++;
					}
				}
				tableReset();
				contectsToTable();
				resultNotice.setText(notice+"총 "+infos.length+"건 삭제");
				resultNotice.setForeground(Color.BLACK);
			}
		}
		if(e.getActionCommand().equals("저장")) {
			colorCheck(resultNotice);
			String group = inputGroup.getText().trim();
			String name = inputName.getText().trim();
			String tel = inputTel.getText().trim();
			String addr = inputAddr.getText().trim();
			if(checkIsWrongInfo(group, name, tel, addr)) {
				return;
			};
			String info = "";
			info += group + ",";
			info += name + ",";
			info += tel + ",";
			info += addr;
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.CONTECTS_ADD, info);
			if(receiveCode == ServiceCode.CONTECTS_SUCCESS) {
				inputNotice.setText("정상 등록되었습니다.");
				inputNotice.setForeground(Color.BLACK);
				resetInputInfo();
			}
		}
		if(e.getActionCommand().equals("수정")) {
			colorCheck(resultNotice);
			String group = inputGroup.getText().trim();
			String name = inputName.getText().trim();
			String tel = inputTel.getText().trim();
			String addr = inputAddr.getText().trim();
			if(checkIsWrongInfo(group, name, tel, addr)) {
				return;
			};
			String info = "";
			info += modifyingInfo.getSerialNo()+",";
			info += group + ",";
			info += name + ",";
			info += tel + ",";
			info += addr;
			receiveCode = nc.sendMsgReceiveCode(ServiceCode.CONTECTS_MODIFY, info);
			if(receiveCode == ServiceCode.CONTECTS_SUCCESS) {
				contects.put(modifyingInfo.getNo(), new Contects_Info(modifyingInfo.getNo(), modifyingInfo.getSerialNo(),group, name, tel, addr));
				tableReset();
				contectsToTable();
				inputNotice.setText("정상 수정되었습니다.");
				inputNotice.setForeground(Color.BLACK);
				save.setText("저장");
				reset.setText("초기화");
				resetInputInfo();
				inputLabel.setText("|| 연락처 추가 ||");
				modifyingInfo = null;
			}
		}
		if(e.getActionCommand().equals("초기화")) {
			inputNotice.setText("   ");
			inputNotice.setForeground(Color.BLACK);
			resetInputInfo();
		}
		if(e.getActionCommand().equals("취소")) {
			colorCheck(resultNotice);
			save.setText("저장");
			reset.setText("취소");
			resetInputInfo();
			inputLabel.setText("|| 연락처 추가 ||");
			inputNotice.setText("  ");
			inputNotice.setForeground(Color.BLACK);
		}
		if(e.getSource()==quit) {
			receiveCode = nc.sendCodeReceiveCode(ServiceCode.CONTECTS_EXIT);
			if(receiveCode == ServiceCode.CONTECTS_QUIT) {
				resultNotice.setText("   ");
				resultNotice.setForeground(Color.BLACK);
				inputLabel.setText("|| 연락처 추가 ||");
				inputNotice.setText("   ");
				inputNotice.setForeground(Color.BLACK);
				searchKeyword.setText("");
				resetInputInfo();
				tableReset();
				switchPanel(false);
				nc = null;
				contects.clear();
				try {
					socket.close();
				} catch (IOException e1) {
				}
				System.out.println(ServiceCode.CONTECTS+"연결 종료");
			}
		}
	}
	
	public void switchPanel(boolean start) {
		startLabel.setVisible(!start);
		seoul.setVisible(!start);
		busan.setVisible(!start);
		startButton.setVisible(!start);
		searchPanel.setVisible(start);
		enterPanel.setVisible(start);
		resultNotice.setVisible(start);
		inputLabel.setVisible(start);
		scrollTable.setVisible(start);
		inputOne.setVisible(start);
		inputTwo.setVisible(start);
		quit.setVisible(start);
	}
	
	public void colorCheck(JLabel label) {
		if(label.getForeground() == Color.RED) {
			label.setText("   ");
			label.setForeground(Color.BLACK);
		}
	}
	
	public boolean checkIsWrongInfo(String group, String name, String tel, String addr) {
		if(group.equals("")) {
			inputNotice.setText("부서명을 입력하지 않았습니다.");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(name.equals("")) {
			inputNotice.setText("이름을 입력하지 않았습니다.");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(tel.equals("")) {
			inputNotice.setText("전화번호를 입력하지 않았습니다.");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(addr.equals("")) {
			inputNotice.setText("주소를 입력하지 않았습니다.");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(CheckLetters.isWrongName(group)) {
			inputNotice.setText("부서명에는 국어, 영소대문자, 숫자만 가능합니다. 특수문자 불가능");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(CheckLetters.isWrongName(name)) {
			inputNotice.setText("이름에는 국어, 영소대문자, 숫자만 가능합니다. 특수문자 불가능");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(CheckLetters.isWrongTel(tel)) {
			inputNotice.setText("전화번호에는 기호를 제외한 띄지 않고 숫자만 입력하세요.");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		if(CheckLetters.isWrongAddr(addr)) {
			inputNotice.setText("주소에는 국어, 영소대문자, 숫자만 가능합니다. 특수문자 불가능");
			inputNotice.setForeground(Color.RED);
			return true;
		}
		return false;
	}
	
	public void resetInputInfo() {
		inputGroup.setText("");
		inputName.setText("");
		inputTel.setText("");
		inputAddr.setText("");
	}
	
	public void tableReset() {
		if(contectsTable.getRowCount()==0) {
			return;
		}
		tableModel.setNumRows(0);
	}
	public void contectsToTable() {
		for(int i=1;i<=contects.keySet().size();i++) {
			Contects_Info info = contects.get(i);
			String[] str = {String.valueOf(info.getNo()), info.getGroup(), info.getName(), info.getTel(), info.getAddress()};
			tableModel.addRow(str);
		}
	}
}
