// Matt L'Ecuyer 260454319

package assignment1;

public class Bladesworn extends Warrior{
	
	public static double BASE_HEALTH;
	public static int BASE_COST;
	public static int WEAPON_TYPE = 3;
	public static int BASE_ATTACK_DAMAGE = 10;

	public Bladesworn(Tile t) {
		super(t,BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE ,BASE_COST);
	}


	void main(String[] Args) {
	}

	public int takeAction() {
		Tile currentTile = this.getPosition();
		Tile nextTile = currentTile.towardTheCamp();
		// check if the tile has a monster
		if (currentTile.getNumOfMonsters() != 0) {
			System.out.println("There is a monster");
			System.out.println(currentTile.getMonster());
			currentTile.getMonster().takeDamage(this.getAttackDamage(), this.getWeaponType());

		} else if (nextTile.getWarrior() == null && nextTile.isCamp() == false) {
			currentTile.removeFighter(this);
			nextTile.addFighter(this);
		}
		
		return 0;
	}

}
