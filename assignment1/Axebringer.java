// Matt L'Ecuyer 260454319

package assignment1;

public class Axebringer extends Warrior {

	public static double BASE_HEALTH;
	public static int BASE_COST;
	public static int WEAPON_TYPE = 2; //axe
	public static int BASE_ATTACK_DAMAGE;
	private boolean cooldown = false;

	public Axebringer(Tile t) {
		super(t,BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE ,BASE_COST);
		cooldown = false;
	}



	void main(String[] Args) {
		System.out.println("hello");
	}

	public int takeAction(){
		if (cooldown == false) {
			Tile currentTile = this.getPosition();
			Tile nextTile = currentTile.towardTheCamp();
			int skillPoints = 0;
			int ad = this.getAttackDamage();
			if (currentTile.getNumOfMonsters() != 0) {
				double damageDealt = currentTile.getMonster().takeDamage(this.getAttackDamage(), this.getWeaponType());
				skillPoints = (int) (((double) ad) / damageDealt +1);
			} else if (nextTile.getNumOfMonsters() != 0 && nextTile.isCamp() == false) {
				cooldown = true;
				double damageDealt = nextTile.getMonster().takeDamage(this.getAttackDamage(), this.getWeaponType());
				skillPoints = (int) (((double) ad) / damageDealt +1);
			}


			return skillPoints;
		}
		cooldown = false;
		return 0;

	}

}

