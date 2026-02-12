// Matt L'Ecuyer 260454319

package assignment1;

public abstract class Warrior extends Fighter{
	private int requiredSkillPoints;
	public static double CASTLE_DMG_REDUCTION;

	void main(String[] Args) {
		
	}

	public Warrior(Tile t, double hp, int wp, int ad, int sp) {
		super(t,hp,wp,ad);
		requiredSkillPoints = sp;
	}


	
	public int getTrainingCost() {
		return requiredSkillPoints;
	}

	public double takeDamage(double raw, int weapon) {
		double reducedRaw = raw * CASTLE_DMG_REDUCTION;
		return super.takeDamage(reducedRaw,weapon);
	}

	

}
