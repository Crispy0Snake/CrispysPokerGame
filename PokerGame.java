import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
	//TO DO LIST Hand Identifier/Value assigner, Highest value wins
public class PokerGame {
	
	public static void main(String[] args) {
//-------------------------------------------------------
	ArrayList<Integer> cardDeck = new ArrayList<>(); //Card deck 1 to 52 in position 0 to 51
	for(int i = 0; i < 52; i++) {
		int a = i+1;
		cardDeck.add(a);
	}
	Collections.shuffle(cardDeck);
	
	ArrayList<Integer> cardIds = new ArrayList<>(); //will store Id's of dealt cards
	
	int numberOfPlayers = 5; // 2 <= n <= 23
	
	int dealamount = (2*numberOfPlayers)+5;
	
	cardIdGen(dealamount, cardDeck, cardIds);
	
	ArrayList<Players> PlayerList = new ArrayList<Players>();
	
	for(int i = 0; i < numberOfPlayers; i++) {
		Players player = new Players(i+1);
		PlayerList.add(player);
	}
	
	//PlayerList.get(2).setBustStatus(true);
	
	ArrayList<ArrayList<Integer>> FinalNumContain = new ArrayList<>();
	ArrayList<ArrayList<String>> FinalSuitContain = new ArrayList<>();
	ArrayList<ArrayList<String>> FinalHRContain = new ArrayList<>();
//-------------------------------------------------------
	for(int k = 0; k < numberOfPlayers; k++) { //TODO GO TO N IN ONE LOOP	
		if(!PlayerList.get(k).getBustStatus()) {
			System.out.println(PlayerList.get(k).getName() + "'s cards: ");
			cardidtoCard(cardIds.get(2*k), PlayerList.get(k).getNumCheckAL(), PlayerList.get(k).getSuitCheckAL());
			cardidtoCard(cardIds.get(2*k+1), PlayerList.get(k).getNumCheckAL(), PlayerList.get(k).getSuitCheckAL());
			System.out.println("--------------------------------");
		}
		else {
			cardidtoCardNP(cardIds.get(2*k), PlayerList.get(k).getNumCheckAL(), PlayerList.get(k).getSuitCheckAL());
			cardidtoCardNP(cardIds.get(2*k+1), PlayerList.get(k).getNumCheckAL(), PlayerList.get(k).getSuitCheckAL());
		}
	}
	//Betting round 1
	System.out.println("Flop cards:");
	cardidtoCard(cardIds.get(2*numberOfPlayers), PlayerList.get(0).getNumCheckAL(), PlayerList.get(0).getSuitCheckAL());
	cardidtoCard(cardIds.get(2*numberOfPlayers+1), PlayerList.get(0).getNumCheckAL(), PlayerList.get(0).getSuitCheckAL());
	cardidtoCard(cardIds.get(2*numberOfPlayers+2), PlayerList.get(0).getNumCheckAL(), PlayerList.get(0).getSuitCheckAL());
	for(int l = 1; l < numberOfPlayers; l++) {
		cardidtoCardNP(cardIds.get(2*numberOfPlayers), PlayerList.get(l).getNumCheckAL(), PlayerList.get(l).getSuitCheckAL());
		cardidtoCardNP(cardIds.get(2*numberOfPlayers+1), PlayerList.get(l).getNumCheckAL(), PlayerList.get(l).getSuitCheckAL());
		cardidtoCardNP(cardIds.get(2*numberOfPlayers+2), PlayerList.get(l).getNumCheckAL(), PlayerList.get(l).getSuitCheckAL());
	}
	System.out.println("--------------------------------");
	//Betting round 2
	System.out.println("Turn card:");
	cardidtoCard(cardIds.get(2*numberOfPlayers+3), PlayerList.get(0).getNumCheckAL(), PlayerList.get(0).getSuitCheckAL());
	for(int m = 1; m < numberOfPlayers; m++) {
		cardidtoCardNP(cardIds.get(2*numberOfPlayers+3), PlayerList.get(m).getNumCheckAL(), PlayerList.get(m).getSuitCheckAL());
	}
	System.out.println("--------------------------------");
	//Betting round 3
	System.out.println("River card:");
	cardidtoCard(cardIds.get(2*numberOfPlayers+4), PlayerList.get(0).getNumCheckAL(), PlayerList.get(0).getSuitCheckAL());
	for(int n = 1; n < numberOfPlayers; n++) {
		cardidtoCardNP(cardIds.get(2*numberOfPlayers+4), PlayerList.get(n).getNumCheckAL(), PlayerList.get(n).getSuitCheckAL());
	}
	//Betting round 4
	
	for(int o = 0; o < numberOfPlayers; o++) {
		FilerOfCheckLists(PlayerList.get(o).getNumContainAL(), PlayerList.get(o).getSuitContainAL(), PlayerList.get(o).getNumCheckAL(), PlayerList.get(o).getSuitCheckAL());
		for(int i = 0; i < 21; i++) {
			HandStrengthFiler(PlayerList.get(o).getNumContainAL().get(i), PlayerList.get(o).getSuitContainAL().get(i), PlayerList.get(o).getHRContainerAL());
		}
	}
	
	for(int p = 0; p < numberOfPlayers; p++) {
		PlayerList.get(p).setBestHandIndex(Integer.parseInt(ReturnBestHI(PlayerList.get(p).getHRContainerAL()).get(0)));
	}
	
	for(int q = 0; q < numberOfPlayers; q++) {
		FinalNumContain.add(PlayerList.get(q).getNumContainAL().get(PlayerList.get(q).getBestHandIndex()));
		FinalSuitContain.add(PlayerList.get(q).getSuitContainAL().get(PlayerList.get(q).getBestHandIndex()));
	}
	
	for(int r = 0; r < numberOfPlayers; r++) {
		HandStrengthFiler(FinalNumContain.get(r), FinalSuitContain.get(r), FinalHRContain); //THIS HRC ARRAY PLAYER 1's BEST HAND AT 0
	}
	
	for(int s = 0; s < numberOfPlayers; s++) { //GO TO N IN ONE LOOP	
		if(PlayerList.get(s).getBustStatus() || PlayerList.get(s).getFoldStatus()) { //if bust or folded
			for(int i = 0; i < FinalHRContain.get(s).size(); i++) {
				FinalHRContain.get(s).set(i, "0");
			}
		}
	}
	//TODO EACH SIDE POT CREATED IN BETTING ROUNDS NEEDS IT'S OWN SHOWDOWN
	System.out.println("--------------------------------");
	if(ReturnBestHI(FinalHRContain).size() == 1) {
		System.out.println("Winner: " + PlayerList.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0))).getName());
		HRALTranslator(FinalHRContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0)))); 
		System.out.println("Cards: ");
		PrintCards(FinalNumContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0))), FinalSuitContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0)))); 
	}
	else if(ReturnBestHI(FinalHRContain).size() > 1) {
		System.out.println("Draw Between: ");
		for(int i = 0; i < ReturnBestHI(FinalHRContain).size(); i++) {
			System.out.println(PlayerList.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(i))).getName());	
		}
		System.out.println("--------------------------------");
		System.out.println("Example of Drawing Hand: ");
		HRALTranslator(FinalHRContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0)))); 
		PrintCards(FinalNumContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0))), FinalSuitContain.get(Integer.parseInt(ReturnBestHI(FinalHRContain).get(0))));
	}
	
	//TODO Add bets and All-in Boolean element, deal with one and then many side pots (First win detect if an all inned player has won, if yes, deal out their side pot (players in*their bet) then find the next winners to dish out the remaining pot to
}
//-------------------------------------------------------
public static void FilerOfCheckLists(ArrayList<ArrayList<Integer>> numcontainer, ArrayList<ArrayList<String>> suitcontainer, ArrayList<Integer> num, ArrayList<String> suit) {
		
		for(int j = 0; j < 21; j++) {
			
			ArrayList<Integer> numAlter = new ArrayList<>();
			ArrayList<String> suitAlter = new ArrayList<>();
			numAlter.clear();
			suitAlter.clear();
			
			for(int k = 0; k < 7; k++) {
			numAlter.add(num.get(k));
			suitAlter.add(suit.get(k));
			}
				
			int a;
			int b;
			
			if (j < 6) {
				a = 0;
				b = j;
			}
			else if (j < 11) {
				a = 1;
				b = j-5;
			}
			else if (j < 15) {
				a = 2;
				b = j-9;
			}
			else if (j < 18) {
				a = 3;
				b = j-12;
			}
			else if (j < 20) {
				a = 4;
				b = j-14;
			}
			else {
				a = 5;
				b = j-15;
			}
			numAlter.remove(a);
			numAlter.remove(b);
			suitAlter.remove(a);
			suitAlter.remove(b);
			
			for(int l = 0; l < 5; l++) {
				
				int min = numAlter.get(0);
				
				for(int m = 0; m < 5; m++) {
					if (numAlter.get(m) < min) {
						min = numAlter.get(m);
					}
				}
				suitAlter.add(suitAlter.get(numAlter.indexOf(min)));
				suitAlter.remove(numAlter.indexOf(min));
				
				numAlter.remove(numAlter.indexOf(min)); 
				numAlter.add(min+13); 
			}
			for(int n = 0; n < 5; n++) {
				numAlter.set(n, numAlter.get(n)-13);
			}
			
			numcontainer.add(new ArrayList<>(Arrays.asList(numAlter.get(0), numAlter.get(1), numAlter.get(2), numAlter.get(3), numAlter.get(4))));
			suitcontainer.add(new ArrayList<>(Arrays.asList(suitAlter.get(0), suitAlter.get(1), suitAlter.get(2), suitAlter.get(3), suitAlter.get(4))));
		}		
	}

	public static void cardidtoCard(int cardid, ArrayList<Integer> nums, ArrayList<String> suits) {
		
		String hearts = "Hearts";
		String diamonds = "Diamonds";
		String clubs = "Clubs";
		String spades = "Spades";
		
		String ace = "Ace";
		String jack = "Jack";
		String queen = "Queen";
		String king = "King";
		
		int cardnum;
		String suit;
		
		if(cardid <= 13) {
			suit = hearts;
			cardnum = cardid;
		}
		else if(cardid <= 26) {
			suit = diamonds;
			cardnum = cardid-13;
		}
		else if(cardid <= 39) {
			suit = clubs;
			cardnum = cardid-26;
		}
		else if(cardid <= 52) {
			suit = spades;
			cardnum = cardid-39;
		}
		else {
			suit = "errors";
			cardnum = 0;
		}
		nums.add(cardnum);
		
		suits.add(suit);
	//-------------------------------------------------------
		String prefix;
		
		if(cardnum == 1) {
			prefix = ace;
		}
		else if(cardnum == 11) {
			prefix = jack;
		}
		else if(cardnum == 12) {
			prefix = queen;
		}
		else if(cardnum == 13) {
			prefix = king;
		}
		else {
			prefix = Integer.toString(cardnum);
		}
		
		System.out.println(prefix+" of "+suit);
	}	
	public static void cardidtoCardNP(int cardid, ArrayList<Integer> nums, ArrayList<String> suits) {
		// A version of the above with no prefix or print. used to store data in the player ArrayLists only
		String hearts = "Hearts";
		String diamonds = "Diamonds";
		String clubs = "Clubs";
		String spades = "Spades";
		
		int cardnum;
		String suit;
		
		if(cardid <= 13) {
			suit = hearts;
			cardnum = cardid;
		}
		else if(cardid <= 26) {
			suit = diamonds;
			cardnum = cardid-13;
		}
		else if(cardid <= 39) {
			suit = clubs;
			cardnum = cardid-26;
		}
		else if(cardid <= 52) {
			suit = spades;
			cardnum = cardid-39;
		}
		else {
			suit = "errors";
			cardnum = 0;
		}
		nums.add(cardnum);
		
		suits.add(suit);
	}
	
	public static void cardIdGen(int dealnum, ArrayList<Integer> deck, ArrayList<Integer> dealt) {
		//if dealnum = 2, produce 2 card id's stored in an arraylist of dealt cards
		for(int j = 0; j < dealnum; j++) {
			dealt.add(deck.get(j)); //adds card to array of dealt cards 0-(dealnum-1) for dealnum cards
			deck.remove(j); 
		}
	}
	
	public static void HandStrengthFiler(ArrayList<Integer> cardnum, ArrayList<String> suits, ArrayList<ArrayList<String>> rankcontainer) {
		
		ArrayList<String> handrank = new ArrayList<>();
		
		//---------------------------------
		boolean FlushCheck;
		int Ftruecheck = 0;
		
		for(int k = 0; k < 4; k++) {
			if(suits.get(0) == suits.get(k+1)) {
				Ftruecheck++;
			}
		}
		if(Ftruecheck == 4) {
			FlushCheck = true;
		}
		else {
			FlushCheck = false;
		}
		//---------------------------------
		boolean StraightCheck = false;
		boolean ahStraightCheck = false;
		int Struecount = 0;
		
		ArrayList<Integer> acehigh = new ArrayList<>(Arrays.asList(1, 10, 11, 12, 13));
		int ahtruecount = 0;
		
		for(int l = 0; l < 4; l++) {
			if (cardnum.get(l) == cardnum.get(l+1)-1) {
				Struecount++;
			}
		}
		
		for(int m = 0; m < 5; m++) {
			if (cardnum.get(m) == acehigh.get(m)) {
				ahtruecount++;
			}
		}
		
		if (Struecount == 4) {
			StraightCheck = true;
		}
		
		if (ahtruecount == 5) {
			ahStraightCheck = true;
		}
		//----------------------------
		boolean Pair = false;
		boolean TwoPair = false;
		boolean ThreeofaK = false;
		boolean FullHouse = false;
		boolean FourofaK = false;
		int paircount = 0;
		
		Integer[] cardnumcount = new Integer[13];
		
		for(int n = 0; n < 13; n++) {
			cardnumcount[n] = 0;
		}
		
		for(int o = 0; o < 5; o++) {
			for(int p = 0; p < 13; p++) {
				if (cardnum.get(o) == p+1) {
					cardnumcount[p]++;
				}
			}
		}
		for(int q = 0; q < 13; q++) {
			if(cardnumcount[q] == 2) {
				paircount++;
				Pair = true;
			}
			if (cardnumcount[q] == 3) {
				ThreeofaK = true;
			}
			if (cardnumcount[q] == 4) {
				FourofaK = true;
			}
		}
		if(paircount == 2) {
			TwoPair = true;
			Pair = false;
		}
		if(Pair && ThreeofaK) {
			FullHouse = true;
			Pair = false;
			ThreeofaK = false; //ERROR
		}
		//----------------------------
		//vv--Royal Flush--vv (Rank 10) ACE HANDLED
		if(ahStraightCheck && FlushCheck) {
			handrank.add(Integer.toString(10));
			handrank.add(Integer.toString(14));
			for(int r = 0; r < 5; r++) {
				handrank.add(Integer.toString(0));
			}
		}
		//vv--Straight Flush--vv (Rank 9) ACE HANDLED
		else if(StraightCheck && FlushCheck) {
			handrank.add(Integer.toString(9));
			for(int s = 0; s < 5; s++) {
				handrank.add(Integer.toString(cardnum.get(4-s)));
			}
			handrank.add(suits.get(0));
		}
		//vv--Four of a Kind--vv (Rank 8) ACE HANDLED (USE CARDNUMCOUNT ARRAY)
		else if(FourofaK) {
			handrank.add(Integer.toString(8));
			if(cardnum.get(0) == cardnum.get(1)) {
				if(cardnum.get(0) == 1) {
					handrank.add(Integer.toString(14));
					handrank.add(Integer.toString(cardnum.get(4)));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(0)));
				handrank.add(Integer.toString(cardnum.get(4)));
				}
			}
			else {
				if(cardnum.get(0) == 1) {
					handrank.add(Integer.toString(cardnum.get(4)));
					handrank.add(Integer.toString(14));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(4)));
				handrank.add(Integer.toString(cardnum.get(0)));
				}
			}
			
			for(int r = 0; r < 4; r++) {
				handrank.add(Integer.toString(0));
			}
		}
		//vv--Full House--vv (Rank 7) ACE HANDLED
		else if(FullHouse) {
			handrank.add(Integer.toString(7));
			
			if(cardnum.get(2) == 1) {
				handrank.add(Integer.toString(14));
			}
			else {
			handrank.add(Integer.toString(cardnum.get(2)));
			}
			
			if(cardnum.get(2) == cardnum.get(1)) {
				if(cardnum.get(3) == 1) {
					handrank.add(Integer.toString(14));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(3))); 
				}
			}
			else {
				if(cardnum.get(1) == 1) {
					handrank.add(Integer.toString(14));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(1))); 
				}
			}
			
			for(int r = 0; r < 4; r++) {
				handrank.add(Integer.toString(0));
			}
			
		}
		//vv--Flush--vv (Rank 6) ACE HANDLED
		else if(FlushCheck) {
			handrank.add(Integer.toString(6));
			if(cardnum.get(0) == 1) {
				handrank.add(Integer.toString(14));
				for(int t = 0; t < 4; t++) {
					handrank.add(Integer.toString(cardnum.get(4-t)));
				}
			}
			else {
				for(int t = 0; t < 5; t++) {
					handrank.add(Integer.toString(cardnum.get(4-t)));
				}
			}
			handrank.add(suits.get(0));
		}
		//vv--Straight--vv (Rank 5) ACE HANDLED
		else if(StraightCheck) {
			handrank.add(Integer.toString(5));
			for(int u = 0; u < 5; u++) {
				handrank.add(Integer.toString(cardnum.get(4-u)));
			}
			handrank.add(Integer.toString(0));
		}
		else if(ahStraightCheck) {
			handrank.add(Integer.toString(5));
			handrank.add(Integer.toString(14));
			for(int v = 0; v < 4; v++) {
				handrank.add(Integer.toString(cardnum.get(4-v)));
			}
			handrank.add(Integer.toString(0));
		}
		//vv--Three Of A Kind--vv (Rank 4) ACE HANDLED
		else if(ThreeofaK) {
			handrank.add(Integer.toString(4));
			
			if(cardnum.get(2) == 1) {
				handrank.add(Integer.toString(14));
			}
			else {
			handrank.add(Integer.toString(cardnum.get(2))); 
			}
			
			if(cardnum.get(1) == cardnum.get(2)) {
				if(cardnum.get(4) == 1) {
					handrank.add(Integer.toString(14));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(4))); 
				}
			}
			else {
				if(cardnum.get(1) == 1) {
					handrank.add(Integer.toString(14));
				}
				else {
				handrank.add(Integer.toString(cardnum.get(1))); 
				}
			}
			
			if(cardnum.get(3) == cardnum.get(2)) {
				handrank.add(Integer.toString(cardnum.get(0))); 
			}
			else {
				handrank.add(Integer.toString(cardnum.get(3))); 
			}
			
			for(int r = 0; r < 3; r++) {
				handrank.add(Integer.toString(0));
			}
		}
		//vv--Two Pair--vv (Rank 3) ACE HANDLED
		else if(TwoPair) { //aabb- aa-bb -aabb element 1 will always be lower pair, element 3 will always be higher pair
			handrank.add(Integer.toString(3));
			
			if(cardnum.get(3) == 1) {
				handrank.add(Integer.toString(14));
			}
			else {
			handrank.add(Integer.toString(cardnum.get(3))); 
			}
			
			handrank.add(Integer.toString(cardnum.get(1))); 
			
			for(int r = 0; r < 5; r++) {
				if(cardnum.get(r) != cardnum.get(3) && 
						cardnum.get(r) != cardnum.get(1)) {
					if(cardnum.get(r) == 1) {
						handrank.add(Integer.toString(14));
					}
					else {
					handrank.add(Integer.toString(cardnum.get(r))); 
					}
				}
			}
			
			for(int r = 0; r < 3; r++) {
				handrank.add(Integer.toString(0));
			}
		}
		//vv--Pair--vv (Rank 2) //aa--- -aa-- --aa- ---aa find a, add numbers that aren't a to the array in reverse order
		else if(Pair) {
			handrank.add(Integer.toString(2));
			
			int pairnum = 0;
			
			for(int r = 0; r < 4; r++) {
				if(cardnum.get(r) == cardnum.get(r+1)) { //find pair in positions r and r+1
					if(cardnum.get(r) == 1) {
						pairnum = 14;
						handrank.add(Integer.toString(14));
					}
					else {
						pairnum = cardnum.get(r);
						handrank.add(Integer.toString(cardnum.get(r))); 
					}
				}
			}
			//reverse through the array adding elements != Integer.parseInt(cardnum.get(r))
			//Scenario 1: pair is not aces and an ace exists in the hand
			if(cardnum.get(0) == 1 && pairnum != 14) {
				handrank.add(Integer.toString(14));
				for(int s = 0; s < 5; s++) {
					//reverse add numbers in cardnum which are not pairnum or 1 to handrank
					if(cardnum.get(4-s) != pairnum && cardnum.get(4-s) != 1) {
						handrank.add(Integer.toString(cardnum.get(4-s))); 
					}
				}
			}
			//Scenario 2: pair is not aces and an ace does not exist
			if(cardnum.get(0) != 1) {
				for(int t = 0; t < 5; t++) {
					if(cardnum.get(4-t) != pairnum) {
						handrank.add(Integer.toString(cardnum.get(4-t)));
					}
				}
			}
			//Scenario 3: pair is aces
			if(pairnum == 14) {
				for(int u = 0; u < 5; u++) {
					if(cardnum.get(4-u) != 1) {
						handrank.add(Integer.toString(cardnum.get(4-u)));
					}
				}
			}
			handrank.add(Integer.toString(0));
			handrank.add(Integer.toString(0));
		}
		//vv--High Card--vv (Rank 1) ACE HANDLED
		else {
			handrank.add(Integer.toString(1));
			if(cardnum.get(0) == 1) {
				handrank.add(Integer.toString(14));
				for(int t = 0; t < 4; t++) {
					handrank.add(Integer.toString(cardnum.get(4-t)));
				}
			}
			else {
				for(int t = 0; t < 5; t++) {
					handrank.add(Integer.toString(cardnum.get(4-t)));
				}
			}
			handrank.add(Integer.toString(0));
		}
		rankcontainer.add(handrank);
	}

	public static ArrayList<String> ReturnBestHI(ArrayList<ArrayList<String>> HRContainer) {
		//CHANGE: RETURN AN ARRAYLIST OF BEST HAND INDEXES IN CASE OF A DRAW IN FINAL CONTAINER
		ArrayList<ArrayList<String>> HRCDummy = new ArrayList<>();
		HRCDummy.clear();
		
		for(int s = 0; s < HRContainer.size(); s++) {
			HRCDummy.add(HRContainer.get(s));
		}
		
		int RWValue = 0;
		int R1Value = 0;
		int R2Value = 0;
		int R3Value = 0;
		int R4Value = 0;
		int R5Value = 0; //DEFAULT VALUES
		
		//Round 1
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(0)) >= RWValue) {
				RWValue = Integer.parseInt(HRCDummy.get(t).get(0));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(0)) < RWValue) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		//Round 2
		for(int u = 0; u < HRCDummy.size(); u++) {
			if(Integer.parseInt(HRCDummy.get(u).get(1)) >= R1Value) {
				R1Value = Integer.parseInt(HRCDummy.get(u).get(1));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(1)) < R1Value) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		//Round 3
		for(int y = 0; y < HRCDummy.size(); y++) {
			if(Integer.parseInt(HRCDummy.get(y).get(2)) >= R2Value) {
				R2Value = Integer.parseInt(HRCDummy.get(y).get(2));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(2)) < R2Value) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		//Round 4
		for(int v = 0; v < HRCDummy.size(); v++) {
			if(Integer.parseInt(HRCDummy.get(v).get(3)) >= R3Value) {
				R3Value = Integer.parseInt(HRCDummy.get(v).get(3));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(3)) < R3Value) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		//Round 5
		for(int w = 0; w < HRCDummy.size(); w++) {
			if(Integer.parseInt(HRCDummy.get(w).get(4)) >= R4Value) {
				R4Value = Integer.parseInt(HRCDummy.get(w).get(4));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(4)) < R4Value) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		//Round 6
		for(int x = 0; x < HRCDummy.size(); x++) {
			if(Integer.parseInt(HRCDummy.get(x).get(5)) >= R5Value) {
				R5Value = Integer.parseInt(HRCDummy.get(x).get(5));
			}
		}
		for(int t = 0; t < HRCDummy.size(); t++) {
			if(Integer.parseInt(HRCDummy.get(t).get(5)) < R5Value) {
				for(int i = 0; i < 6; i++) {
					HRCDummy.get(t).set(i, Integer.toString(0));
				}
			}
		}
		ArrayList<String> bestIndexes = new ArrayList<>();
		bestIndexes.clear();
		//add index of any array not 0'd
		for(int z = 0; z < HRCDummy.size(); z++) {
			if(Integer.parseInt(HRCDummy.get(z).get(0)) != 0) {
				bestIndexes.add(Integer.toString(z));
			}
		}
		return bestIndexes;
	}
	
	public static void PrintCards(ArrayList<Integer> cardnums, ArrayList<String> suits) {
		for(int i = 0; i < cardnums.size(); i++) {
			String prefix;
			
			if(cardnums.get(i) == 1) {
				prefix = "Ace";
			}
			else if(cardnums.get(i) == 11) {
				prefix = "Jack";
			}
			else if(cardnums.get(i) == 12) {
				prefix = "Queen";
			}
			else if(cardnums.get(i) == 13) {
				prefix = "King";
			}
			else {
				prefix = Integer.toString(cardnums.get(i));
			}
			
			System.out.println(prefix + " of " + suits.get(i));
		}
	}
	
	public static String HRALSupp(int cardnum) {
		
		String prefix;
		
		if(cardnum == 14 || cardnum == 1) {
			prefix = "Aces";
		}
		else if(cardnum == 11) {
			prefix = "Jacks";
		}
		else if(cardnum == 12) {
			prefix = "Queens";
		}
		else if(cardnum == 13) {
			prefix = "Kings";
		}
		else {
			prefix = (Integer.toString(cardnum) + "s");
		}
		return prefix;
	}
	
public static String HRALSuppNP(int cardnum) {
		//Non Plural Version of the above
		String prefix;
		
		if(cardnum == 14 || cardnum == 1) {
			prefix = "Ace";
		}
		else if(cardnum == 11) {
			prefix = "Jack";
		}
		else if(cardnum == 12) {
			prefix = "Queen";
		}
		else if(cardnum == 13) {
			prefix = "King";
		}
		else {
			prefix = Integer.toString(cardnum);
		}
		return prefix;
	}
	
	public static void HRALTranslator(ArrayList<String> HRAL) {
		
		
		
		if(Integer.parseInt(HRAL.get(0)) == 10) {
			System.out.println("Hand: " + "Royal Flush in " + HRAL.get(6));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 9) {
			System.out.println(HRAL.get(1) + "high Straight Flush in " + HRAL.get(6));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 8) { //In Aces/Kings/Queens
			System.out.println("Hand: " + "Four of a Kind in " + HRALSupp(Integer.parseInt(HRAL.get(1))));
			System.out.println("Sidecard: " + HRALSuppNP(Integer.parseInt(HRAL.get(2))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 7) {
			System.out.println("Hand: " + "Full House in " + HRALSupp(Integer.parseInt(HRAL.get(1))) + " and " + HRALSupp(Integer.parseInt(HRAL.get(2))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 6) {
			System.out.println("Hand: " + HRALSuppNP(Integer.parseInt(HRAL.get(1))) + " high Flush in " + HRAL.get(6));
			System.out.println("Other cards: " + HRALSuppNP(Integer.parseInt(HRAL.get(2))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(3))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(4))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(5))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 5) {
			System.out.println("Hand: " + HRALSuppNP(Integer.parseInt(HRAL.get(1))) + " high Straight");
		}
		else if(Integer.parseInt(HRAL.get(0)) == 4) {
			System.out.println("Hand: " + "Three of Kind in " + HRALSupp(Integer.parseInt(HRAL.get(1))));
			System.out.println("Sidecards: " + HRALSuppNP(Integer.parseInt(HRAL.get(2))) + " and " + HRALSuppNP(Integer.parseInt(HRAL.get(3))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 3) {
			System.out.println("Hand: " + "Two Pair of " + HRALSupp(Integer.parseInt(HRAL.get(1))) + " and " + HRALSupp(Integer.parseInt(HRAL.get(2))));
			System.out.println("Sidecard: " + HRALSuppNP(Integer.parseInt(HRAL.get(3))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 2) {
			System.out.println("Hand: " + "Pair of " + HRALSupp(Integer.parseInt(HRAL.get(1))));
			System.out.println("With three sidecards: " + HRALSuppNP(Integer.parseInt(HRAL.get(2))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(3))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(4))));
		}
		else if(Integer.parseInt(HRAL.get(0)) == 1) {
			System.out.println("Hand: " + HRALSuppNP(Integer.parseInt(HRAL.get(1))) + " High");
			System.out.println("Other cards: " + HRALSuppNP(Integer.parseInt(HRAL.get(2))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(3))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(4))) + ", " + HRALSuppNP(Integer.parseInt(HRAL.get(5))));
		}
	}
}