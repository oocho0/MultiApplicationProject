package Client;

public class Main_FrameSwitch {
	private static Main_FrameSwitch sf = null;
	BaseballGame_Frame baseballGameFrame;
	Main_MenuFrame menuFrame;
	private static String ip;
	
	private Main_FrameSwitch() {
		baseballGameFrame = new BaseballGame_Frame(ip);
		menuFrame = new Main_MenuFrame(ip);
	}
	
	public static Main_FrameSwitch getInstance() {
		if(sf == null) {
			sf = new Main_FrameSwitch();
		}
		return sf;
	}
	
	public static void setIP(String ip) {
		Main_FrameSwitch.ip = ip;
	}
}
