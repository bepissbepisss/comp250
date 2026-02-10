// Matt L'Ecuyer 260454319

package assignment1;

abstract public class Fighter{

	private Tile position;
	private double health;
	private int weaponType;
	private int attackDamage;

	public Fighter(Tile t, double hp, int wp, int ad) {
		position = t;
		health = hp;
		weaponType = wp;
	}


	// i have to use this shit eventually, dont understand how page 9
//	private bool ensureTileContainsFighter(){
//		if (this instanceof Warrior && this.position.warrior == this) {
//			return true;
//		}
//		//make sure the last element of the troop is this monster
//		if (this instanceof Monster && MonsterTroop.flipList(this.positon.troop)[0] == this) {
//			return true;
//		}
//
//	}

	public String toString(){
		return "Health = " + health;
	}
	
	// strangely wont compile without this
	//public Fighter(){
	//}

	final public Tile getPosition() {
		return position;
	}

	final public double getHealth(){
		return health;
	}

	final public int getWeaponType(){
		return weaponType;
	}

	final public int getAttackDamage(){
		return attackDamage;
	}

	public void setPosition(Tile t) {
		position = t;
	}

	public double takeDamage(double raw, int weapon) {
		double damage;
		if (weapon > this.weaponType)	{
			damage = raw *1.5;
		} else if (weapon < this.weaponType) {
			damage = raw*0.5;
		} else {
			damage = raw;
		}

		this.health -= damage;

		if (this.health <= 0) {
			this.position.removeFighter(this);
			this.setPosition(null);
		}

		return damage;
	}


	public boolean equals(Object o) {
		if (!(o instanceof Fighter)) {
			return false;
		}
		Fighter check = (Fighter) o;
		if (check.getPosition() == this.position && (check.getHealth() <= this.health + 0.001 && check.getHealth() >= this.health - 0.001) && check.getWeaponType() == this.weaponType && check.getAttackDamage() == this.attackDamage) {
			return true;
		}
		return false;
	}
	
	abstract public int takeAction();
	void main(String[] Args) {
		System.out.println("all good");		
	}

}

