// Matt L'Ecuyer 260454319

package assignment1;

public class Monster extends Fighter{
	void main() {

	}

//	public String toString(){
//		return super.toString();
//	}

	public int takeAction(){
		if (this.getPosition().getWarrior() != null) {
			// do damage to warrior
			this.getPosition().getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
		}
		return 0;
		
	}

	public Monster(Tile t, double hp, int wp, int ad) {
		super(t,hp,wp,ad);	
	}
}
