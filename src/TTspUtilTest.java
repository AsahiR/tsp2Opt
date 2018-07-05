import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import random2013.*;

class TTspUtilTest {
	
	private ICRandom fRng;
	private int fCityNum;
	private int[] fCityArray;
	private int[] fIndexArray; 

	TTspUtilTest(){
		fRng = new TCJava48BitLcg();
		fCityNum = 10;
		fCityArray = new int[fCityNum];

		for(int i = 0; i < fCityNum; i++) {
			fCityArray[i] = i;
		}

		/** randomly initialize fCityNum and fIndexArray not using swap method */
		for(int i = fCityNum - 1; i > 0 ; i --) {
			int rLoc = fRng.nextInt(0, i - 1);
			int tmp = fCityArray[i];
			fCityArray[i] = fCityArray[rLoc];
			fCityArray[rLoc] = tmp; 
		}

		fIndexArray = new int[fCityNum];
		for(int i = 0; i < fCityNum; i++) {
			int city = fCityArray[i];
			fIndexArray[city] = i;
		}
	}
	
	/**
	 * check consistency between indexArray and cityArray
	 * @return
	 */
	Boolean cityAndIndexCheck(int[] cityArray, int[] indexArray) {
		for(int i = 0; i < fCityNum; i++) {
			int city = i;
			int loc = indexArray[city];
			int cityFromIndexArray = cityArray[loc];
			if(cityFromIndexArray != city) {
				return false;
			}
		}
		return true;
	}

	/**
	 * compare swapped array and non-swapped array
	 * @param before array before swapping
	 * @param after array after swapping
	 * @param loc1 swapped location
	 * @param loc2 swapped location
	 */
	Boolean swapped(int[] before, int[] after, int index1, int index2) {
		Boolean result1 = before[index1] == after[index2];
		Boolean result2 = before[index2] == after[index1];
		return result1 && result2;
	}
	
	/**
	 * test swapping and consistency between index and city arrays after swapping
	 */
	@Test
	void swapCityAndIndexArrayTest() {
		int[] afterCityArray = new int[fCityNum];
		int[] afterIndexArray = new int[fCityNum];

		System.arraycopy(fCityArray, 0, afterCityArray, 0, fCityNum);
		System.arraycopy(fIndexArray, 0, afterIndexArray, 0, fCityNum);

        int loc1 = fRng.nextInt(0, fCityNum - 1);
        int loc2 = fRng.nextInt(0, fCityNum - 1);
        assertTrue(cityAndIndexCheck(afterCityArray, afterIndexArray), "consistensyCheckAfterCityAndIndexSwap");

        TTspUtil.swapCityAndIndexArray(afterCityArray, afterIndexArray, loc1, loc2);
        assertTrue(swapped(fCityArray, afterCityArray, loc1, loc2), "cityArraySwap");
        assertTrue(cityAndIndexCheck(afterCityArray, afterIndexArray), "consistensyCheckAfterCityAndIndexSwap");
	}
	
	/** test for TTspUtil.swap method */
	@Test
	void swapIntArrayTest(){
		int swapLoc1 = 0;
		int swapLoc2 = 0;
		int[] array = new int[fCityNum];
		System.arraycopy(fCityArray, 0, array, 0, fCityNum);
		while(swapLoc1 == swapLoc2) {
            swapLoc1 = fRng.nextInt(0, fCityNum - 1);
            swapLoc2 = fRng.nextInt(0, fCityNum - 1);
		}
		TTspUtil.swapIntArray(array, swapLoc1, swapLoc2);
		swapped(fCityArray, array, swapLoc1, swapLoc2);
	}
	
}
