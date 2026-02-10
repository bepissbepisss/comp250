// Matt L'Ecuyer 260454319

package assignment1;

public class MonsterTroop {

	private Monster[] monsters;
	private int numOfMonsters;

	public MonsterTroop(){
		monsters = new Monster[0];
		numOfMonsters = 0;
	}

	public String toString() {
		String str = new String();

		for(int i =0; i<numOfMonsters;i++){
			str = str + "Monster at " + i + " has " + monsters[i].toString() + "\n";
			if (monsters[i] == null) {
				System.out.println("Null at " + i + "!\n");
			}
		}

		return str;
	}	

	public int sizeOfTroop() {
		return numOfMonsters;
	}

	public Monster[] getMonsters(){
		Monster[] arr = new Monster[numOfMonsters];
		for(int i=0; i < numOfMonsters; i++) {
			arr[i] = monsters[i];
		}
		return arr;

	}
	
	// returns the first monster
	public Monster getFirstMonster(){
		if (numOfMonsters ==0) {
			System.out.println("No monsters");
			return null;
		}
		return monsters[0];
	}

	public void addMonster(Monster m) {
		numOfMonsters++;
		Monster[] arr= new Monster[numOfMonsters];

		for(int i=0; i < numOfMonsters-1; i++){
			arr[i] = monsters[i];
		}

		arr[numOfMonsters-1] = m;

		monsters = arr;
	}


	// i believe this should remove from the front
	public boolean removeMonster(Monster m){
		// find the first occurence from the back
		int index = -1;
		for(int i=0;i < numOfMonsters; i++) {
			if (monsters[i] == m) {
				index = i;
//				System.out.println("Found match at index " + index);
				break;
			}
		}

		if (index ==-1) {
			return false;
		}

		numOfMonsters--;
		Monster[] arr = new Monster[numOfMonsters];
		for (int i = 0; i<index;i++){
			arr[i] = monsters[i];
//			System.out.println("Loaded until index, last added is " + arr[index-1]);
			
		}

		for (int i=index; i<numOfMonsters; i++) {
			arr[i] = monsters[i+1];
//			System.out.println("Just added " + arr[i]);
		}

		monsters = arr;

		return true;

		

	}

	static public Monster[] flipList(MonsterTroop t){
		Monster[] arr = new Monster[t.numOfMonsters];
		
		int j = t.numOfMonsters -1;
		for(int i = 0; i<t.numOfMonsters; i++) {
			arr[i] = t.monsters[j];
			j--;
		}

		return arr;
	}
	
	void main(String[] Args) {
		Tile tCastle = new Tile();
		Tile tCamp = new Tile(false,false,true,null,null,null,null);

		Bladesworn b = null;
		MonsterTroop mt = new MonsterTroop();

		Tile t = new Tile(false,false,true, tCastle, tCamp, b, mt);
		b = new Bladesworn(t);
		Axebringer a = new Axebringer(t);

		Monster m = new Monster(t, 1,11,10);

		mt.addMonster(m);
		System.out.println(m);
		b.takeAction();
		System.out.println(b.getPosition());
		// should kill monster then move to next tile
		b.takeAction();
		System.out.println(b.getPosition());
		// actually moved to next tile
	}



}
