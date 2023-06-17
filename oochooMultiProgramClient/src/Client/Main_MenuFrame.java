package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class Main_MenuFrame extends JFrame implements ChangeListener, ActionListener{
	
	private JTabbedPane tabbedPane;
	private Login_Panel loginPanel;
	private JDialog loginPop;
	private JButton startGame;
	private JButton termilogin;
	private JButton terminate;
	private final String IP;
	
	
	public Main_MenuFrame(String ip) throws HeadlessException {
		
		this.IP = ip;
		setUIFont(new FontUIResource(new Font("Apple SD Gothic Neo", Font.PLAIN, 12)));
		
		JPanel login, baseballGameTab,contactsTab;
		
		GridBagConstraints[] gbc = new GridBagConstraints[3];
		for(int i= 0;i<3;i++) {
			gbc[i] = new GridBagConstraints();
			gbc[i].gridx = i;
			gbc[i].gridx = i;
		}
		
		loginPop = new JDialog(this,"로그인",true);
		login = new JPanel();
		loginPanel = new Login_Panel(loginPop, IP);
		loginPop.setSize(400,300);
		loginPop.setLocationRelativeTo(this);
		termilogin = new JButton("프로그램 종료");
		termilogin.addActionListener(this);
		termilogin.setAlignmentX(CENTER_ALIGNMENT);
		login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
		login.add(loginPanel);
		login.add(termilogin);
		loginPop.setLayout(new GridBagLayout());
		loginPop.add(login,gbc[1]);

		tabbedPane = new JTabbedPane();
		baseballGameTab = new JPanel();
		startGame = new JButton("야구 게임 하기");
		startGame.addActionListener(this);
		baseballGameTab.setLayout(new GridBagLayout());
		baseballGameTab.add(startGame,gbc[1]);
		tabbedPane.addTab("야구게임",baseballGameTab);
		Chat_Tab chatTab = new Chat_Tab(IP);
		tabbedPane.addTab("채팅",chatTab);
		contactsTab = new Contacts_Tab(IP);
		tabbedPane.addTab("사내 연락처", contactsTab);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addChangeListener(this);

		terminate = new JButton("프로그램 종료");
		terminate.addActionListener(this);
		setLayout(null);
		tabbedPane.setBounds(40, 28, 520, 380);
		terminate.setBounds(240,410,120,30);
		add(tabbedPane);
		add(terminate);
		
		loginPop.setUndecorated(true);
		loginPop.setVisible(true);
		
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension frameSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width)/2, (screenSize.height-frameSize.width)/2);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		/* 현재 tab 번호 */
		int idx = tabbedPane.getSelectedIndex();
		/* index 위에 tab 문자열 가져오기 */
		String msg = tabbedPane.getTitleAt(idx);
		System.out.println("[메뉴]>>> "+(msg += "탭 선택 - 이동"));
		tabbedPane.setSelectedIndex(idx);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==termilogin||e.getSource()==terminate) {
			System.out.println("[프로그램]>>> 종료");
			System.exit(0);
		}
		if(e.getSource()==startGame) {
			System.out.println("[메뉴]>>> 야구 게임 창으로 전환");
			Main_FrameSwitch sf = Main_FrameSwitch.getInstance();
			sf.baseballGameFrame.setVisible(true);
			sf.menuFrame.setVisible(false);
		}
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
