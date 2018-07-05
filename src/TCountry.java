
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.io.FileReader;

public class TCountry {
	/** cities */
	private TNode[] fNodes;
	private String fName;
	/** num of cities */
	private int fDim;
	
	public void readFrom(BufferedReader br) throws Exception {
		String nameSplitter = "NAME.*";
		String line = TTspUtil.accessToLine(br, nameSplitter);
        fName = TTspUtil.parseAlphabetAndNum(line)[1];
		
		String dimSplitter = "DIMENSION.*";
		line = TTspUtil.accessToLine(br,  dimSplitter);
		fDim = Integer.parseInt(TTspUtil.parseNumber(line)[1]);

		String nodeSplitter = "NODE_COORD_SECTION";
		line = TTspUtil.accessToLine(br, nodeSplitter);

		fNodes = new TNode[fDim];
		for(int i = 0; i < fDim; i++) {
			fNodes[i] = new TNode();
			fNodes[i].readFrom(br);
		}

	}
	
	public TNode getNode(int index) {
		return fNodes[index];
	}
	
	public String getName() {
		return fName;
	}
	
	public int getDim() {
		return fDim;
	}
	
	public TNode[] getNodes() {
		return fNodes;
	}
	
	public static void main(String[] argv) throws FileNotFoundException{
		String src = "wi29.tsp";
		TCountry country = new TCountry();
		try(BufferedReader br = new BufferedReader(new FileReader(src))){
			country.readFrom(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(country);
	}
	
	/** copy constructor */
	public TCountry(TCountry src) {
		fName = src.fName;
		fDim = src.fDim;
		fNodes = new TNode[fDim];
		TNode[] srcNodes = src.getNodes();
		for(int i = 0; i < fDim ; i++) {
			fNodes[i] = srcNodes[i].clone();
		}
	}
	
	@Override
	public TCountry clone() {
		return new TCountry(this);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(fName);
		sb.append("\n");
		sb.append(fDim);
		sb.append("\n");
		for(int i = 0; i < fDim; i++) {
			sb.append(fNodes[i]);
			if(i < fDim - 1) {
                sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public int calcDistance(int nodeId1, int nodeId2) {
		TNode o1 = fNodes[nodeId1];
		TNode o2 = fNodes[nodeId2];
		double xd = o1.getX() - o2.getX();
		double yd = o1.getY() - o2.getY();
		double tmp = Math.sqrt( xd * xd + yd * yd);
		int result = (int) (tmp + 0.5);//floor???
		return result;
	}
	
	public TCountry() {
	}
}
