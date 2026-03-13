// Matt L'Ecuyer 260454319

package assignment1;
public class Tile{
	
	void main(String[] Args) {

	}

	private boolean isCastle;
	private boolean isCamp;
	private boolean onThePath;

	// these could be the actual castle and camp
	private Tile towardTheCastle;
	private Tile towardTheCamp;

	private Warrior warrior;
	private MonsterTroop troop;

	public Tile(){
		isCastle = false;
		isCamp = false;
		onThePath = false;
	
		warrior = null; // not sure
		troop = new MonsterTroop();

	}	

	public Tile(boolean castle, boolean camp, boolean path, Tile toCastle, Tile toCamp, Warrior w, MonsterTroop t){
		System.out.println("Initializing tiles");
		isCastle = castle;
		isCamp = camp;
		onThePath = path;
		towardTheCastle = toCastle;
		towardTheCamp = toCamp;
		warrior = w;
		troop = t;
	}

	public boolean isCastle() {
		return isCastle;
	}

	public boolean isCamp() {
		return isCamp;
	}

	public void buildCastle(){
		isCastle = true;
	}

	public void buildCamp(){
		isCamp = true;
	}

	public boolean isOnThePath(){
		return onThePath;
	}

	// not sure about these two
	public Tile towardTheCastle(){
		if (this.isOnThePath() == false || this.isCastle() == true) {
			return null;
		}
		return this.towardTheCastle;
	}

	public Tile towardTheCamp(){
		if (this.isOnThePath() == false || this.isCamp() == true) {
			return null;
		}
		return this.towardTheCamp;
	}

	public void createPath(Tile ct, Tile cp) {
		if (ct != null && cp != null) {
			onThePath = true;
			return;
		}

		if ((ct == null && isCastle == false) || (cp == null && isCamp == false) ) {
			throw new IllegalArgumentException("Bad path logic");
		}
	}

	public int getNumOfMonsters(){
		if (troop == null) return 0;
		return troop.sizeOfTroop();
	}

	public Warrior getWarrior() {
		return warrior;
	}

	public Monster getMonster() {
		return troop.getFirstMonster();
	}

	public Monster[] getMonsters() {
		return MonsterTroop.flipList(troop);
	}

	public boolean addFighter(Fighter f) {
		if (f instanceof Warrior && warrior == null && isCamp == false) {
			f.setPosition(this);
			warrior = (Warrior) f;
			return true;
		}
		if (f instanceof Monster && (onThePath || isCastle || isCamp)) {
			f.setPosition(this);
			troop.addMonster( (Monster) f);
			return true;
		}
		return false;
	}

	public boolean removeFighter(Fighter f) {
		//System.out.println("Trying to remove fighter");
		if (f instanceof Warrior && warrior != null) {
			warrior.setPosition(null);
			warrior = null;
			return true;
		}
		if (f instanceof Monster && troop.sizeOfTroop() != 0) {
			System.out.println("Removing a a monster");
			troop.getFirstMonster().setPosition(null);
//			System.out.println("set its pos to null");
			troop.removeMonster(troop.getFirstMonster());
//			System.out.println("Removed Monster");
			return true;
		}
		return false;
	}

}
