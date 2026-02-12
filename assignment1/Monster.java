// Matt L'Ecuyer 260454319

package assignment1;

public class Monster extends Fighter{
	void main() {

	}

//	public String toString(){
//		return super.toString();
//	}

	private int rageLevel = 0;
	public static int BERSERK_THRESHOLD;

	// override take damage
	public double takeDamage(double raw, int weapon) {
		double damage = super.takeDamage(raw, weapon);

		int rageGain = this.getWeaponType()- weapon;
		if (rageGain > 0 ) {
			rageLevel += rageGain;
			System.out.println("Rage increased to " + rageLevel);
		}

		
		return damage;
	}		

	public int takeAction(){
		// do two actions if enraged
		int i = 1;
		if (rageLevel > BERSERK_THRESHOLD) {
			i = 2;
			rageLevel = 0;
		}

		for(int j = i; j>0; j--) {

			// if there is a warrior on this tile
			Tile currentTile = this.getPosition();
			System.out.println(currentTile);
			System.out.println(currentTile.towardTheCastle());

			System.out.println("workin");
			if (currentTile.getWarrior() != null) {

				// do damage to warrior
				currentTile.getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());

				// now go to the back of the troop.
				currentTile.removeFighter(this);
				currentTile.addFighter(this);
			} else { //advance towards the castle if the next tile is not null
				if (currentTile.towardTheCastle() != null) {
					Tile nextTile = currentTile.towardTheCastle();
					currentTile.removeFighter(this);
					nextTile.addFighter(this);

					this.setPosition(nextTile);
				}

			}
		}
		return 0;
		
	}

	public boolean equals(Object o) {
		if (!(o instanceof Monster)) {
			return false;
		}
		Monster other = (Monster) o;

		if (super.equals(other)) {
			return true;
		} else {
			return false;
		}

//		if (other.getPosition() == this.getPosition() && (other.getHealth() <= this.getHealth() + 0.001 && other.getHealth() >= this.getHealth()- 0.001) &&  other.getAttackDamage() == this.getAttackDamage()) {
//			return true;
//		} else {
//			return false;
//		}
	}

	public Monster(Tile t, double hp, int wp, int ad) {
		super(t,hp,wp,ad);
		rageLevel = 0;
	}
}
