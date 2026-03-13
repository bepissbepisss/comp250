package assignment2;

public class SolitaireCipher {
	public Deck key;


	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		int[] nums = new int[size];
		int n;
		for(int i =0;i<size;i++){
			n = key.generateNextKeystreamValue();
			nums[i]=n;
		}

		return nums;
	}

	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		String cleanMsg = "";
		msg = msg.toUpperCase();
		char a;
		for (int i=0;i<msg.length();i++) {
			a = msg.charAt(i);
			if (a >= 'A' && a <= 'Z') {
				cleanMsg += a;
			}
		}
		int[] nums = getKeystream(cleanMsg.length());
		System.out.println("Message is " + cleanMsg);
		System.out.println("Nums are");
		for (int i : nums) {
			System.out.print(i);
		}

		String secret = "";
		for(int i = 0; i<cleanMsg.length();i++) {
			a = cleanMsg.charAt(i);
			System.out.print("Letter" + a +" plus " + nums[i] + " gives ");
			a += nums[i] % 26;
			while (!(a >= 'A' && a <= 'Z')) {
				a -= 26;
			}
			secret += a;
			System.out.println(a);
		}


		return secret;
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {
		/**** ADD CODE HERE ****/
		return null;
	}

}
