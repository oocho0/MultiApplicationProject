package Client;

public class Main_ClientStarter {

	public static void main(String[] args) {
		Main_FrameSwitch.getInstance().menuFrame.setVisible(true);
		Main_FrameSwitch.setIP("192.168.0.33");
		System.out.println("[프로그램]>>> 시작");
	}

}
