import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TNode {
	/** x-Axis, y-Axis */
	private double fX;
	private double fY;
	/** index in xxx.tsp . no use */
	private int fIndex;
	
	public TNode() {
	}
	
	public void readFrom(BufferedReader br) throws IOException {
		String line;
		line = br.readLine();
		String[] strArray = TTspUtil.parseNumber(line);
		fIndex = Integer.parseInt(strArray[0]);
		fX = Double.parseDouble(strArray[1]);
		fY = Double.parseDouble(strArray[2]);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String splitter = " ";
		sb.append(fIndex);
		sb.append(splitter);
		sb.append(fX);
		sb.append(splitter);
		sb.append(fY);
		return sb.toString();
	}
	
	public void writeTo(PrintWriter pw) {
		pw.println(toString());
	}
	
	public double getX() {
		return fX;
	}
	
	public double getY() {
		return fY;
	}
	
	public int getIndex() {
		return fIndex;
	}
	
	/** copy constructor */
	public TNode(TNode src) {
		fX = src.fX;
		fY = src.fY;
		fIndex = src.fIndex;
	}
	
	@Override
	public TNode clone() {
		return new TNode(this);
	}
	
}
