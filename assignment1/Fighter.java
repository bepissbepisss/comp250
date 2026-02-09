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
	

	void main(String[] Args) {
		System.out.println("all good");		
	}

}

