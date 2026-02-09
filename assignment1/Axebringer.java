// Matt L'Ecuyer 260454319

package assignment1;

public class Axebringer extends Warrior {

	public static double BASE_HEALTH;
	public static int BASE_COST;
	public static int WEAPON_TYPE = 2; //axe
	public static int BASE_ATTACK_DAMAGE;

	public Axebringer(Tile t) {
		super(t,BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE ,BASE_COST);
	}



	void main(String[] Args) {
		System.out.println("hello");
	}

}

