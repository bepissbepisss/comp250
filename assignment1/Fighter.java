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
	
	// strangely wont compile without this
	//public Fighter(){
	//}

	final private Tile getPosition() {
		return position;
	}

	final private double getHealth(){
		return health;
	}

	final private int getWeaponType(){
		return weaponType;
	}

	final private int getAttackDamage(){
		return attackDamage;
	}

	private void setPosition(Tile t) {
		position = t;
	}
	

	void main(String[] Args) {
		System.out.println("all good");		
	}

}

