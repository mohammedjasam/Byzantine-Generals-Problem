import java.util.HashMap;

public class Ratio {
	public HashMap<String, int[]> update;
	public HashMap<String, int[]> search;
	
	public Ratio() {
		update = new HashMap<String, int[]>();
		search = new HashMap<String, int[]>();
	}
	
	public void IncrementSearchCost(int user, int mode) {
		if(!this.search.containsKey(String.valueOf(user))) {
			this.search.put(String.valueOf(user), new int[4]);
			for(int i=0; i<4; i++) {
				this.search.get(String.valueOf(user))[i] = 0;
			}
			
			this.update.put(String.valueOf(user), new int[4]);
			for(int i=0; i<4; i++) {
				this.update.get(String.valueOf(user))[i] = 0;
			}
		}
		this.search.get(String.valueOf(user))[mode]++;
	}
	
	public void IncrementUpdateCost(int user, int mode) {
		if(!this.update.containsKey(String.valueOf(user))) {
			this.update.put(String.valueOf(user), new int[4]);
			for(int i=0; i<4; i++) {
				this.update.get(String.valueOf(user))[i] = 0;
			}
			
			this.search.put(String.valueOf(user), new int[4]);
			for(int i=0; i<4; i++) {
				this.search.get(String.valueOf(user))[i] = 0;
			}
		}
		this.update.get(String.valueOf(user))[mode]++;
	}
	
	public void PrintUpdateVsSearchRatio(int user, int mode) {
		String modeName = "";
		switch(mode) {
		case Constant.Mode_Database_Value:
			modeName = "DV";
			break;
		case Constant.Mode_Actual_Pointer:
			modeName = "AP";
			break;
		case Constant.Mode_Forwarding_Pointer:
			modeName = "FP";
			break;
		case Constant.Mode_Partition:
			modeName = "Pn";
			break;
	}
		System.out.println("Mode: " + modeName + 
				"\tUser: " + user +
				"\tUpdate Cost: " + this.update.get(String.valueOf(user))[mode] + 
				"\tSearch Cost: " + this.search.get(String.valueOf(user))[mode] +
				"\tUpdate to Search Ratio: " + (double)(this.update.get(String.valueOf(user))[mode])/(double)(this.search.get(String.valueOf(user))[mode]));
	}
}
