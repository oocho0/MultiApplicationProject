package Server;

public class Main_ServerStarter {
	public static void main(String[] args) {
		Thread ls = new Thread(new Login_Server(),ServiceCode.LOGIN);
		ls.start();
		Thread bs = new Thread(new BaseballGame_Server(), ServiceCode.BASEBALLGAME);
		bs.start();
		Thread cs = new Thread(new Chat_Server(),ServiceCode.CHATTING);
		cs.start();
		Thread co = new Thread(new Contects_Server(), ServiceCode.CONTECTS);
		co.start();
	}
}
