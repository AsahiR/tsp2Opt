import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

public class TTspUtil {
	/** return line matching pattern */
	public static String accessToLine(BufferedReader br, String pattern) throws IOException {
		String line = "";
		while(line != null) {
			line = br.readLine();
			if(line.matches(pattern)) {
				return line;
			}
		}
		return  line;
	}
	
	/**
	 * @param line
	 * @return String[]. element has only digit
	 */
	public static String[] parseNumber(String line) {
		String splitter = "[^0-9\\.]+";
		String[] result = line.split(splitter);
		return result;
	}
	
	/**
	 * @param line
	 * @return String[] element have only alphabet or digit
	 */
	public static String[] parseAlphabetAndNum(String line) {
		String splitter = "[^a-zA-Z0-9\\.]+";
		String[] result = line.split(splitter);
		return result;
	}
	
	/**
	 * write table into pw
	 * @param table
	 * @param pw
	 * @param splitter
	 */
	public static void writeTable(int[][] table, PrintWriter pw, String splitter){
		int rowSize = table.length;
		for(int i = 0; i < rowSize; i++) {
			StringBuilder sb = new StringBuilder();
			int[] row = table[i];
			int columnSize = row.length;
			for(int j = 0; j < columnSize; j++) {
				sb.append(row[j]);
				if(j < columnSize - 1) {
					sb.append(splitter);
				}
			}
			pw.println(sb.toString());
		}
	}
	
	/**
	 * readTable from br and return table in ArrayList<ArrayList> (rows<columns>)
	 * @param br
	 * @param splitter
	 * @param rowSize
	 * @return
	 * @throws IOException
	 */
	public static int[][] readTable(BufferedReader br, String splitter, int rowSize) throws IOException{
		int[][] table = new int[rowSize][];
        for(int i = 0; i < rowSize; i++) {
            String line = br.readLine();
            String[] cells = line.split(splitter);
            int columnSize = cells.length;
            int[] row = new int[columnSize];

            for(int j = 0; j < columnSize; j++) {
            	String cell = cells[j];
            	row[j] = Integer.parseInt(cell);
            }
            table[i] = row; 
          }
        return table;
	}
	
	/**
	 * read table with constant columns size
	 * @param src
	 * @return
	 * @throws IOException
	 */
	public static int[][] readCTable(String src) throws Exception{
		String splitter = ",";
		int[][] table;
		try(FileReader fr = new FileReader(src); BufferedReader br = new BufferedReader(fr)){
			System.out.println(src);
              int rowSize = Integer.parseInt(br.readLine());
              br.readLine();// columnSize no use
              table = readTable(br, splitter, rowSize);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return table;
	}
	
	/**
	 * read inverse nearest list from file src
	 * @param src
	 * @return
	 * @throws IOException
	 */
	public static int[][] readINL(String src) throws Exception{
		return readCTable(src);
	}
	
	/**
	 * deprecated method. Use makeNL method . Too slow for using Integer ?
	 * @param k
	 * @param country
	 * @param cityArray
	 * @return
	 */
	public static int[][] calcNL(int k, TCountry country, int[] cityArray){
		int cityNum = cityArray.length;
		int[][] result = new int[cityNum][];
		for(int i = 0; i < cityNum; i++) {
			result[i] = calcKN(k, i, country, cityArray);
		}
		return result;
	}
	
	/**
	 * write table with constant columns size
	 * @param table
	 * @param dst
	 * @param splitter
	 */
	public static void writeCTable(int[][] table, String dst, String splitter) {
		try(PrintWriter pw = new PrintWriter(dst)){
			pw.println(table.length);
			pw.println(table[0].length);
			writeTable(table, pw, splitter);
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * write inverse nearest list(variable columns size)
	 * @param table
	 * @param dst
	 * @param splitter
	 * @param k
	 */
	public static void writeINL(int[][] table, String dst, String splitter, int k) {
		try(PrintWriter pw = new PrintWriter(dst)){
			pw.println(table.length);
			pw.println(k);
			writeTable(table, pw, splitter);
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * calc k-nearest list for one city from cityArray
	 * @param k
	 * @param distanceRow
	 * @param cityArray
	 * @return
	 */
	public  static int[] calcKN(int k, int city, TCountry coutntry, int[] cityArray) {
		int columnNum = cityArray.length;
		/** for sort */
		Integer[] nearestArray = new Integer[columnNum];
		for(int i = 0; i < columnNum; i++) {
			nearestArray[i] = cityArray[i] ;
		}
		Comparator<Integer>  distanceComp = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int distance1 = coutntry.calcDistance(city, o1.intValue());
				int distance2 = coutntry.calcDistance(city, o2.intValue());
				return Integer.compare(distance1, distance2);
			}
		};
		Arrays.sort(nearestArray, distanceComp); // cannot sort type "int" by user-definition comparator???
		/** deep copy */
		int[] result = new int[k];
		for(int i = 0; i < k; i++) {
			result[i] = nearestArray[i + 1].intValue();
		} //distance[i][i] == 0 return view
		return result;
	}
	
	/**
	 * calc length of edge o1-o2
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static int calcEdgeLength(TNode o1, TNode o2) {
		int result = 0;
		double xd = o1.getX() - o2.getX();
		double yd = o1.getY() - o2.getY();
		double tmp = Math.sqrt( xd * xd + yd * yd);
		result = (int) (tmp + 0.5);//floor???
		return result;
	}
	
	/**
	 * calc distance table (row, column) = (node, node)
	 * @param nodes
	 * @return
	 */
	public static int[][] calcDistanceTable(TNode[] nodes) {
		int citySize = nodes.length;
		int[][] distanceTable = new int[citySize][];

		for(int i = 0; i < citySize; i++) {
			int[] row = new int[citySize];

			for(int j = 0; j < citySize; j++) {
				row[j] = calcEdgeLength(nodes[i], nodes[j]);
			}
			distanceTable[i]  = row;
		}
		return distanceTable;
	}
	
	/**
	 * calc inverse nearest list
	 * @param nL
	 * @param k
	 * @return
	 */
	public static int[][] calcINL(int[][] nL, int k){
		int cityNum = nL.length;
		ArrayList<ArrayList<Integer>> iNLArrayList = new ArrayList<>(cityNum);
		for(int i = 0; i < cityNum; i++) {
			iNLArrayList.add(new ArrayList<>());
		}

		for(int i = 0; i < cityNum; i++) {
			for(int j = 0; j < k; j++) {
				iNLArrayList.get(nL[i][j]).add(i);
			}
		}
		int[][] iNL = new int[iNLArrayList.size()][];
		for(int i = 0; i < iNL.length; i++) {
			ArrayList<Integer> lINLArrayList = iNLArrayList.get(i);
			int columnNum = lINLArrayList.size();
			iNL[i] = new int[columnNum] ;
			for(int j = 0; j < columnNum; j++) {
				iNL[i][j] = lINLArrayList.get(j) ;
			}
			
		}
		return iNL;
	}
	
	/**
	 * convert cities from TNode to Integer
	 * @param nodes
	 * @return
	 */
	public static int[] getIntegerCityArray(TNode[] nodes){
		int cityNum = nodes.length;
		int[] cityArray = new int[cityNum];
		for(int i = 0; i < cityNum; i++) {
			cityArray[i] = nodes[i].getIndex() - 1; // nodeIndex is 1-base, but cityArray is 0-base
		}
		return cityArray;
	}
	
	/**
	 * swap array[i1] and array[i2]
	 * @param array
	 * @param i1
	 * @param i2
	 */
	public static void swapIntArray(int[] array, int i1, int i2) {
		int tmp = array[i1];
		array[i1] = array[i2];
		array[i2] = tmp;
	}
	
	/**
	 * 
	 * @param cityArray
	 * @param indexArray
	 * @param loc1
	 * @param loc2
	 */
	public static void swapCityAndIndexArray(int[] cityArray, int[] indexArray, int loc1, int loc2) {
		int city1 = cityArray[loc1];
		int city2 = cityArray[loc2];
		swapIntArray(cityArray, loc1, loc2);
		swapIntArray(indexArray, city1, city2);
	}

	/**
	 * calc k-nearest-lists for each city of country
	 * @param nLSize
	 * @param country
	 * @return
	 */
	public static int[][] makeNL(int nLSize, TCountry country) {
		int cityNum = country.getDim();
		int[][] neighborCities = new int[cityNum][];
		int[][] distanceTable = new int[cityNum][];
		for(int i = 0; i < cityNum; i++) {
			neighborCities[i] = new int[nLSize];
			distanceTable[i] = new int[nLSize];
			neighborCities[i][0] = -1;
			distanceTable[i] [0] = Integer.MAX_VALUE;
			
			int count = 0;
			for(int j = 0; j < cityNum; j++) {
				if(i == j) {
					continue;
				}
				int distance = country.calcDistance(i, j);
				if(distance < distanceTable[i][count]) {
					int insert = (count < nLSize - 1) ?  count + 1 : count;
					neighborCities[i][insert] = j; 
					distanceTable[i][insert] = distance;
					
					for(int k = insert; k > 0; k --) {
						if(distanceTable[i][k - 1] <= distanceTable[i][k]) {
							break;
						}
						
						int temp = neighborCities[i][k - 1];
						neighborCities[i][k - 1] = neighborCities[i][k];
						neighborCities[i][k] = temp;
						
						temp = distanceTable[i][k - 1];
						distanceTable[i][k - 1] = distanceTable[i][k];
						distanceTable[i][k] = temp;
					}
					if(count < nLSize - 1) {
						count ++;
					}
				}
			}
		}
		return neighborCities;
	}
}
