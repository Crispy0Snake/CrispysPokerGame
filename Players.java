import java.util.ArrayList;

public class Players {
	
	private String name;
	private ArrayList<Integer> PlayernumCheckAL = new ArrayList<>(); //array of card numbers for checks P1
	private ArrayList<String> PlayersuitCheckAL = new ArrayList<>(); //array of card suits for checks P1
	private ArrayList<ArrayList<Integer>> PlayernumcheckListContainer = new ArrayList<>();
	private ArrayList<ArrayList<String>> PlayersuitcheckListContainer = new ArrayList<>();
	private ArrayList<ArrayList<String>> PlayerHandRankContainer = new ArrayList<>();
	private int besthandindex = 0;
	private boolean bust = false;
	private boolean folded = false;
	private boolean allin = false;
	
	Players(int playernum) {
		this.setName(playernum);
	}
	//GETTERS
	public String getName() {
		return name;
	}
	
	public ArrayList<Integer> getNumCheckAL() {
		return PlayernumCheckAL;
	}
	
	public ArrayList<String> getSuitCheckAL() {
		return PlayersuitCheckAL;
	}
	
	public ArrayList<ArrayList<Integer>> getNumContainAL() {
		return PlayernumcheckListContainer;
	}
	
	public ArrayList<ArrayList<String>> getSuitContainAL() {
		return PlayersuitcheckListContainer;
	}
	
	public ArrayList<ArrayList<String>> getHRContainerAL() {
		return PlayerHandRankContainer;
	}
	
	public int getBestHandIndex() {
		return besthandindex;
	}
	
	public boolean getBustStatus() {
		return bust;
	} //True if they are bust
	
	public boolean getFoldStatus() {
		return folded;
	} //True if they have folded
	
	public boolean getAllInStatus() {
		return allin;
	}
	//SETTERS
	public void setName(int playernum) {
		this.name = ("Player " + playernum);
	}
	
	public void setBestHandIndex(int index) {
		this.besthandindex = index;
	}
	
	public void setBustStatus(boolean bustq) {
		this.bust = bustq;
	}
	
	public void setFoldStatus(boolean foldq) {
		this.folded = foldq;
	}
	
	public void setAllInStatus(boolean allinq) {
		this.allin = allinq;
	}
}
