
public class T2Opt {
	private TTour fTour;
	/** selection list constant length (== fCityNum) */
	private TSelectionList fSL;
	
	/** inverse nearest list */
	private int[][] fINL;
	/** nearest list */
	private int[][] fNL;
	/** K-nearest */
	private int fK;
	private int fCityNum;
	
	public T2Opt(String iNLSrc, String nLSrc, TTour tour, int k) {
		fTour = tour;
		fK = k;
        fCityNum = tour.getCountry().getDim();
        setNL(nLSrc);
        setINL(iNLSrc);
		fSL = new TSelectionList(fCityNum);
	}
	
	/**
	 * set fINL. readFrom src . when reading fails, calculate and set
	 * @param src
	 */
	public void setINL(String src) {
		try {
          fINL = TTspUtil.readINL(src);
		} catch (Exception e) {
			fINL = TTspUtil.calcINL(fNL, fK);
			String splitter = ",";
			TTspUtil.writeINL(fINL, src, splitter, fK);
		}
	}
	
	/**
	 * set fNL. readFrom src . when reading fails, calculate and set
	 * @param src
	 */
	public void setNL(String src) {
		try {
          fNL = TTspUtil.readCTable(src);
		} catch (Exception e) {
			fNL = TTspUtil.makeNL(fK, fTour.getCountry());
			String splitter = ",";
			TTspUtil.writeCTable(fNL, src, splitter);
		}
	}
	
	/**
	 * initialize fSL, fSLSize, fExists fTour
	 */
	public void init() {
		fSL.init();
		fTour.init();
	}
	
	
	/**
	 * solve tsp for fTour
	 */
	public void execute() {
		System.out.println("start: "+ fTour.setLength());
		while(! fSL.isEmpty()) {
            selectAndCheck();
		}
		fTour.setLength();
	}
	
	/**
	 * select city from fSL and check it valid or not 
	 */
	public void selectAndCheck() {
			int cityA = fSL.selectCity();
			for(int ort = 1; ort < 3; ort++) {
				int cityB = selectEdgeEnd(ort, cityA);
				int[] aNL = fNL[cityA];
				TCountry country = fTour.getCountry();
				for(int j = 0; j < fK; j++) {
					int cityC = aNL[j];
					int cityD = selectEdgeEnd(ort, cityC);
					int distanceAB = country.calcDistance(cityA, cityB);
					int distanceCD = country.calcDistance(cityC, cityD);
					int distanceAC = country.calcDistance(cityA, cityC);
					int distanceBD = country.calcDistance(cityB, cityD);
					if(distanceAB <= distanceAC) {
						break;
					}
					int gDel = distanceAB + distanceCD;
					int gAdd = distanceAC + distanceBD;
					if(gAdd < gDel) {
						if(ort == 1) {
							fTour.flip(cityA, cityB, cityC, cityD);
						}else {
							fTour.flip(cityB, cityA, cityD, cityC);
						}
						fSL.addToSelections(cityB);
						fSL.addToSelections(cityC);
						fSL.addToSelections(cityD);
						fSL.addSetToSelections(fINL[cityA]);
						fSL.addSetToSelections(fINL[cityB]);
						fSL.addSetToSelections(fINL[cityC]);
						fSL.addSetToSelections(fINL[cityD]);
						return;
					}
				}
			}
			fSL.removeFromSelections(cityA);
			return;
	}
	
	public int[][] getNL (){
		return fNL;
	}
	
	public int[][] getINL (){
		return fINL;
	}
	
	/**
	 * return the other city of edge  with param city
	 * @param ort 1 means next , 2 means prev
	 * @param city
	 * @return
	 */
	public int selectEdgeEnd(int ort, int city) {
		int end = fTour.next(city);
		if(ort == 2) {
			end = fTour.prev(city);
		}
		return end;
	}
	
	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append("2Opt_");
		sb.append("K");
		sb.append(fK);
		sb.append("_");
		sb.append(fTour.getName());
		return sb.toString();
	}
	
	public TTour getTour() {
		return fTour;
	}
}