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


	// i believe this should remove from the back
	public boolean removeMonster(Monster m){
		// find the first occurence from the back
		int index = -1;
		for(int i=numOfMonsters-1;i !=0 ; i--) {
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
	
	void main(String[] Args) {
		MonsterTroop mt = new MonsterTroop();
		Tile t = new Tile();
		Monster m = new Monster(t, 10,10,10);
		Monster n = new Monster(t, 11,1,1);
		Monster s = new Monster(t, 12,1,1);
		Monster a = new Monster(t, 11,10,10);

		mt.addMonster(m);
		mt.addMonster(n);
		mt.addMonster(s);
		mt.addMonster(n);
		mt.addMonster(s);

		System.out.println("Size is " + mt.sizeOfTroop());
		System.out.println(mt);

		boolean change;
		change = mt.removeMonster(n);
	
		System.out.println("Size is " + mt.sizeOfTroop());
		System.out.println(mt);
		System.out.println("Change? " +change);
	

	}



}
