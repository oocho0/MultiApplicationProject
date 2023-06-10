package Server;

import java.util.Random;

public class BaseballGame_RandomComputer {
	private int[] target;

	public BaseballGame_RandomComputer() {
		target = new Random().ints(1,10).distinct().limit(3).toArray();
	}
	
	public int[] getTarget() {
		return this.target;
	}
	
	@Override
	public String toString() {
		return String.format("[target] {%d}{%d}{%d}", target[0],target[1],target[2]);
	}
}