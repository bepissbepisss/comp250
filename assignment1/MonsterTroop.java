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
				System.out.println("Found match at index " + index);
				break;
			}
		}

		if (index ==-1) {
			System.out.println("Nothing to remove");
			return false;
		}

		System.out.println("number of monsters went from " + numOfMonsters);
		numOfMonsters--;
		System.out.println("To " + numOfMonsters);
		Monster[] arr = new Monster[numOfMonsters];
		for (int i = 0; i<index;i++){
			arr[i] = monsters[i];
//			System.out.println("Loaded until index, last added is " + arr[index-1]);
			
		}

		for (int i=index; i<numOfMonsters; i++) {
			arr[i] = monsters[i+1];
//			System.out.println("Just added " + arr[i]);
		}
		
		System.out.println("Updated list");
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
		Tile t1 = null;
		Tile t2 = null;
		Tile t3 = null;
		Tile t4 = null;
		Tile t5 = null;
		t5 = new Tile(false,true,true,t4, null, null,new MonsterTroop()); //camp
		t4 = new Tile(false,false,true,t3, t5, null,new MonsterTroop());
		t3 = new Tile(false,false,true,t2, t4, null,new MonsterTroop());
		t2 = new Tile(false,false,true,t1, t3, null,new MonsterTroop());
		t1 = new Tile(true,false,true,null,t2,null,new MonsterTroop()); //castle
		System.out.println("Created all tiles");	

		new Monster(t3, 1,11,10);
		new Monster(t3, 1,11,10);
		new Monster(t3, 1,11,10);
		new Monster(t3, 1,11,10);

		Lanceforged l = new Lanceforged(t2,3,4);

		Monster[] t = t3.getMonsters();
		for (int i =0; i<t.length; i++) {
			System.out.println("Monster " + (t.length-i) + " is at " +t[t.length-1-i] + " with rage level " + t[t.length-1-i].rageLevel);
		}
//		l.takeAction();

		for (int i = 0; i<t.length; i++) {
			t[i].takeAction();
		}

		t = t3.getMonsters();
		for (int i =0; i<t.length; i++) {
			System.out.println("Monster " + (t.length-i) + " is at " +t[t.length-1-i]);
		}

		


	}



}
