package assignment1;
public class Lanceforged extends Warrior{
	public static double BASE_HEALTH;
	public static int BASE_COST;
	public static int WEAPON_TYPE = 3;
	public static int BASE_ATTACK_DAMAGE = 10;

	private int piercingPower;
	private int actionRange;

	public Lanceforged(Tile t, int pierce, int range){
		super(t,BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE ,BASE_COST);
		piercingPower = pierce;
		actionRange = range;
	}

	private void dealPiercingDamage(Tile t) {
		int n;
		if (piercingPower <= t.getNumOfMonsters()) {
			n = piercingPower;
		} else {
			n = t.getNumOfMonsters();
		}
		System.out.println("n is " + n);

		// damage n monsters from the back???
		Monster[] mList = t.getMonsters();
		// from the front. should hit n monsters
		boolean killedMonster = false;
		int initNumOfMonsters = t.getNumOfMonsters();
		for(int i=initNumOfMonsters-1; i>initNumOfMonsters-1 - n; i--) {
			System.out.println("Num of monst is " + t.getNumOfMonsters());
			System.out.println("Hit a monster");
			System.out.println("i is " + i);
			Monster m = mList[i];
			m.takeDamage(this.getAttackDamage(), this.getWeaponType());
		}
	}	

	public int takeAction() {
		// look for nearest tile with a real troop
		Tile currentTile = this.getPosition();
		if (currentTile.towardTheCamp() == null) {
		}
		Tile nextTile = currentTile.towardTheCamp();
		for (int i = 1; i<actionRange; i++) {
			if (nextTile == null) {
				break;
			}
			if (nextTile.getNumOfMonsters() != 0 && nextTile.isOnThePath()) {
				break;
			}
			nextTile = nextTile.towardTheCamp();
		}

		if (nextTile.isCamp() == true || nextTile.getNumOfMonsters() == 0) {
			return 0;
		}

		dealPiercingDamage(nextTile);
		return 0;


	}
	
	void main(String[] Args) {

	}

}

