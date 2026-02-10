// Matt L'Ecuyer 260454319

package assignment1;

public class Monster extends Fighter{
	void main() {

	}

//	public String toString(){
//		return super.toString();
//	}

	public int takeAction(){
		// if there is a warrior on this tile
		Tile currentTile = this.getPosition();
		if (currentTile.getWarrior() != null) {
			// do damage to warrior
			currentTile.getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
			// now go to the back of the troop.
			currentTile.removeFighter(this);
			currentTile.addFighter(this);
		} else { //advance towards the castle
			Tile nextTile = currentTile.towardTheCastle();
			currentTile.removeFighter(this);
			nextTile.addFighter(this);

			this.setPosition(nextTile);
				
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
	}
}
