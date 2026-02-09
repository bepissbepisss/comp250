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


}
