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

	Warrior warrior;
	MonsterTroop troop;

	public Tile(){
		isCastle = false;
		isCamp = false;
		onThePath = false;
	
		warrior = null; // not sure
		troop = new MonsterTroop();

	}	

	public Tile(boolean castle, boolean camp, boolean path, Tile toCastle, Tile toCamp, Warrior w, MonsterTroop t){
		isCastle = castle;
		isCamp = camp;
		onThePath = path;
		towardTheCastle = toCastle;
		towardTheCamp = toCamp;
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

	public boolean isOnPath(){
		return onThePath;
	}

	// not sure about these two
	public Tile towardTheCastle(){
		if (towardTheCastle.isOnPath() == false || towardTheCastle.isCastle() == true) {
			return null;
		}
		return towardTheCastle;
	}

	public Tile towardTheCamp(){
		if (towardTheCamp.isOnPath() == false || towardTheCamp.isCamp() == true) {
			return null;
		}
		return towardTheCamp;
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
		if (f instanceof Warrior && warrior != null) {
			warrior.setPosition(null);
			warrior = null;
			return true;
		}
		if (f instanceof Monster && troop.sizeOfTroop() != 0) {
			troop.getFirstMonster().setPosition(null);
			troop.removeMonster(troop.getFirstMonster());
			return true;
		}
		return false;
	}

}
