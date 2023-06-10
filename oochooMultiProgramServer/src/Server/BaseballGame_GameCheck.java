package Server;

public class BaseballGame_GameCheck {
	private int strike;
	private int ball;
	public BaseballGame_GameCheck(int[] com, int[] user) {
		for(int i=0;i<user.length;i++) {
			for(int j=0;j<com.length;j++) {
				if(user[i] == com[j]) {
					if(i==j) {
						strike++;
					}else {
						ball++;
					}
				}
			}
		}
	}
	public int getStrike() {
		return strike;
	}
	public int getBall() {
		return ball;
	}
	public boolean isOut() {
		if(strike == 0 && ball == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		if(isOut()) {
			return "out!";
		}
		return String.format("[strike] %d [ball] %d", strike, ball);
	}
	
	public String getValue() {
		if(isOut()) {
			return strike+","+ball+","+1;
		}
		return strike+","+ball+","+0;
	}
}
