import random2013.*;

public class TTour {
	/** cityId list in tour */
	private int[] fCityArray; 

	/** location  list in tour */
	private int[] fIndexArray; 

	/** length of tour */
	private int fLength;
	/** contain cities */
	private TCountry fCountry;
	
	public TTour(TCountry country) {
		fCountry = country;
		fLength = Integer.MAX_VALUE;
	}
	
	/** initialize fCityArray, fIndexArray */
	public void init() {
		int cityNum = fCountry.getDim();
		ICRandom rng = new TCJava48BitLcg();
		fCityArray = new int[cityNum];
		fIndexArray = new int[cityNum];

		for(int i = 0; i < cityNum; i++) {
			fCityArray[i] = i;
			fIndexArray[i] = i;
		}

		for(int i = cityNum - 1; i > 0; i-- ) {
			int rLoc = rng.nextInt(0, i - 1);
			swap(rLoc, i);
		}
	}
	
	public int setLength() {
		int cityNum = fCityArray.length;
		fLength = 0;
		for(int i = 0; i < cityNum; i++) {
			if( i == cityNum - 1) {
				break;
			}
			int start = fCityArray[i];
			int end = fCityArray[i + 1];
			int edgeLength = fCountry.calcDistance(start, end);
			fLength += edgeLength;
		}
		return fLength;
	}
	
	/** calculate length and set into fLength */
	public int setLength(int[][] distanceTable) {
		int cityNum = fCityArray.length;
		fLength = 0;
		for(int i = 0; i < cityNum; i++) {
			if( i == cityNum - 1) {
				break;
			}
			int start = fCityArray[i];
			int end = fCityArray[i + 1];
			int edgeLength = distanceTable[start][end];
			fLength += edgeLength;
		}
		return fLength;
	}
	
	public int getLength() {
		return fLength;
	}
	
	public TCountry getCountry() {
		return fCountry;
	}
	
	/**
	 * @param city cityIndex
	 * @return
	 */
	public int prev(int city) {
		int loc = fIndexArray[city];
		int cityNum = fIndexArray.length;
		int prevCity = cityNum - 1;
		if(0 < loc && loc < cityNum ) {
            prevCity = fCityArray[loc - 1];
		}
		return prevCity;
	}
	
	/**
	 * @param city cityIndex
	 * @return 
	 */
	public int next(int city) {
		int loc = fIndexArray[city];
		int cityNum = fIndexArray.length;
		int nextCity = 0;
		if(0 <= loc && loc < cityNum - 1) {
			nextCity = fCityArray[loc + 1];
		}
		return nextCity;
	}
	
	/**
	 * @param loc1 location in tour
	 * @param loc2 location in tour
	 * @return necessary swapNum between loc1 and loc2
	 */
	public int calcDiffInTour(int loc1, int loc2) {
		int diff = loc1 - loc2;
		int cityNum = fIndexArray.length;
		if (diff < 0) {
			diff += cityNum;
		}
		return diff;
	}
	
	/**
	 * edgeAB and edgeCD => edgeAC and edgeBD
	 * @param a cityIndex
	 * @param b cityIndex
	 * @param c cityIndex
	 * @param d cityIndex
	 */
	public void flip(int a, int b, int c, int d) {
		int aLoc = fIndexArray[a];
		int bLoc = fIndexArray[b];
		int cLoc = fIndexArray[c];
		int dLoc = fIndexArray[d];
		int iAD = calcDiffInTour(aLoc, dLoc);
		int iCB = calcDiffInTour(cLoc, bLoc);
		int swapNum;

		int headLoc;
		int tailLoc;
		if(iAD <= iCB) {
			headLoc = dLoc;
			tailLoc = aLoc;
			swapNum = iAD + 1;
		} else {
			headLoc = bLoc;
			tailLoc = cLoc;
			swapNum = iCB + 1;
		}
		
		for(int i = 0; i < swapNum / 2; i++) {
			int cityNum = fIndexArray.length;
			swap(headLoc, tailLoc);
			headLoc ++;
			if(cityNum <= headLoc) {
				headLoc = 0;
			}
			tailLoc --;
			if(tailLoc < 0) {
				tailLoc = cityNum - 1;
			}
		}
		
	}
	
	/** copy constructor */
	public TTour(TTour src) {
		fCountry = src.getCountry().clone();
		int cityNum = src.fCityArray.length;
		fCityArray = new int[cityNum];
		fIndexArray = new int[cityNum];
		System.arraycopy(src.fCityArray, 0, fCityArray, 0, cityNum);
		System.arraycopy(src.fIndexArray, 0, fIndexArray, 0, cityNum);
		fLength = src.fLength;
	}
	/** clone */
	public TTour clone() {
		return new TTour(this);
	}
	
	public String getName() {
		return fCountry.getName();
	}
	
	public int[]  getCityArray(){
		return fCityArray;
	}
	
	public void swap(int loc1, int loc2) {
		TTspUtil.swapCityAndIndexArray(fCityArray, fIndexArray, loc1, loc2);
	}
	
}
