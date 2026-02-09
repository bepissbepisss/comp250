// Matt L'Ecuyer 260454319

package assignment1;

abstract class Warrior extends Fighter{
	private int requiredSkillPoints;

	public Warrior(Tile t, double hp, int wp, int ad, int sp) {
		super(t,hp,wp,ad);
		requiredSkillPoints = sp;
	}
	
	public int getTrainingCost() {
		return requiredSkillPoints;
	}
	
	void main(String[] Args) {
		
	}

}
