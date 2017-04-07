package space.snowwolf.sgkill;

public class GameOverException extends RuntimeException {
	
	public enum VictoryStatus {
		主公忠臣胜利,
		反贼胜利,
		内奸胜利
	}
	
	private static final long serialVersionUID = -4830080343614006946L;

	private VictoryStatus type;

	public GameOverException(VictoryStatus type) {
		this.type = type;
	}

	public VictoryStatus getType() {
		return type;
	}

}
