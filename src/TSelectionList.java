import random2013.*;

public class TSelectionList {
	/** cityIds in selectionList */
	private int[] fCityArray;

	/** location list in selectionList */
	private int[] fIndexArray;
	
	/** city in valid position in selectionList or not */
	private Boolean[] fExists;
	
	/** constant value */
	private int fCityNum;

	/** use for selectCity */
	private ICRandom fSelectionRng;

	/** valid elements size in fCityArray */
	private int fValidSize;
	
	/** constructor */
	public TSelectionList(int cityNum){
		fCityNum = cityNum;
	}
	
	/** init fSelectionRng, fExists, fCityArray, fIndexArray, fValidSize*/
	public void init() {
		fSelectionRng = new TCJava48BitLcg();
		fCityArray = new int[fCityNum];
		fExists = new Boolean[fCityNum];
		fIndexArray = new int[fCityNum];
		fValidSize = 0;
		for(int i = 0; i < fCityNum; i++) {
			fCityArray[i] = i;
			fIndexArray[i] = i;
			fExists[i] = true;
			fValidSize ++;
		}
	}
	
	/**
	 * @return randomly selected city
	 */
	public int selectCity() {
		int index = fSelectionRng.nextInt(0, fValidSize - 1);
		int city = fCityArray[index];
		return city;
	}
	
	/**
	 * @param city put city into fCityArray[fValidSize]
	 */
	public void addToSelections(int city) {
		if(fExists[city]) {
			return;
		}
		fCityArray[fValidSize] = city;
		fIndexArray[city] = fValidSize;
		fExists[city] = true;
		fValidSize ++;
	}
	
	/**
	 * @param city remove city. swap(fIndexArray[city], fValidSize - 1)
	 */
	public void removeFromSelections(int city) {
		if(! fExists[city]) {
			return;
		}
		int removeCityLoc = fIndexArray[city];
		swap(removeCityLoc, fValidSize - 1);
		fExists[city] = false;
		fValidSize --;
	}
	
	/**
	 * swap loc1 and loc2
	 * @param loc1 location in fCityArray
	 * @param loc2 location in fCityArray
	 */
	public void swap(int loc1, int loc2) {
		TTspUtil.swapCityAndIndexArray(fCityArray, fIndexArray, loc1, loc2);
	}
	
	/** add cities into selections */
	public void addSetToSelections(int[] set) {
		for(int city : set) {
			addToSelections(city);
		}
	}
	
	/**
	 * @return selections empty or not
	 */
	public Boolean isEmpty() {
		return fValidSize == 0;
	}
	
	
	/** for test */
	public int[] getCityArray() {
		return fCityArray;
	}
	
	public int[] getIndexArray() {
		return fIndexArray;
	}
}
