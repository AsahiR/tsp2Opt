import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import mockit.Deencapsulation;

class TTourTest {
	TTour fTour;
	int fCityNum;

	public TTourTest() {
		/** case for 7dim country */
		String tspSrc = "dataSrc/" + "test7.tsp";
		TCountry country = new TCountry();
		try(BufferedReader br = new BufferedReader(new FileReader(tspSrc))){
			country.readFrom(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fTour = new TTour(country);
		fCityNum = country.getDim();
	}

	/** test 6cases in textbook */
	@Test
	void flipTest() {
		initTourForTest();
		HashMap<int[], int[]> inputResultMap = new HashMap<>();
		inputResultMap.put(new int[]{0, 1, 3, 4}, new int[]{0, 3, 2, 1, 4, 5, 6});
		inputResultMap.put(new int[]{3, 4, 0, 1}, new int[]{0, 3, 2, 1, 4, 5, 6});
		inputResultMap.put(new int[]{6, 0, 2, 3}, new int[]{2, 1, 0, 3, 4, 5, 6});
		inputResultMap.put(new int[]{2, 3, 6, 0}, new int[]{2, 1, 0, 3, 4, 5, 6});
		inputResultMap.put(new int[]{5, 6, 1, 2}, new int[]{0, 6, 2, 3, 4, 5, 1});
		inputResultMap.put(new int[]{1, 2, 5, 6}, new int[]{0, 6, 2, 3, 4, 5, 1});
		int mapIndex = 0;
		for(int[] key : inputResultMap.keySet()) {
			initTourForTest();
			int[] result  = inputResultMap.get(key);
			int a = key[0];
			int b = key[1];
			int c = key[2];
			int d = key[3];
			fTour.flip(a, b, c, d);
			int[] cityArrayInTour = Deencapsulation.getField(fTour, "fCityArray");
			for(int i = 0; i < fCityNum; i++) {
				assertTrue(result[i] == cityArrayInTour[i], i + "-th in tour " + mapIndex + "-th case") ;
			}
			mapIndex ++;
		}
	}
	
	/** initialize not using fTour.init() */
	void initTourForTest() {
		fTour.init();
		int[] cityArray = new int[fCityNum];
		int[] indexArray = new int[fCityNum];

		for(int i = 0; i < fCityNum; i++) {
			cityArray[i] = i;
			indexArray[i] = i;
		}
		Deencapsulation.setField(fTour, "fCityArray", cityArray);
		Deencapsulation.setField(fTour, "fIndexArray", indexArray);
	}
	
	/** test for distance calculation */
	@Test
	void testSetLength(){
		initTourForTest();
		int correctLength = 6;
		int length = fTour.setLength();
		assertTrue(length == correctLength, "wrong length " + length);
	}
}
