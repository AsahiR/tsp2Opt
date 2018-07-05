import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import csv2013.*;
import java.util.ArrayList;

import java.util.HashMap;

public class Main {
	/**  algorithm */
	private T2Opt f2Opt;

	/** index for now trial */
	private int fTrialNum;
	
	/** max trialNum */
	private int fMaxTrialNum;
	
	/**  write fLogTable into this */
	private String fCsvDst;

	/** use for fileName etc*/
	private String fName;

	/** (row, column) = (trial, result) */
	private  TCCsvData fLogTable;
	
	/** main key */
	private String fTrialKey = "trialName";

	/** keys for Average and SampleVariane */
	private String fTimeKey = "ExecutionTime";
	private String fLengthKey = "Length";
	
	/** constructor */
	public Main(int maxTrialNum, T2Opt arg2Opt) {
		fMaxTrialNum = maxTrialNum;
		ArrayList<String> keys = new ArrayList<>();
		keys.add(fTrialKey);
		keys.add(fTimeKey);
		keys.add(fLengthKey);
		fLogTable = new TCCsvData(keys);
		f2Opt = arg2Opt;
		setName();
		fCsvDst = "logDst/" + fName + ".csv";
	}
	
	public void setName() {
		StringBuilder sb = new StringBuilder();
		sb.append(f2Opt.getName());
		fName = sb.toString();
	}
	
	/**
	 *  exexute f2Opt by fMaxTrialNum , and then log results
	 */
	public void execute() {
		fTrialNum = 0;
		while(fTrialNum < fMaxTrialNum) {
			f2Opt.init();
			executeOne();
		}
		logStat(fTimeKey, fLengthKey);
		writeCsv();
		System.out.println("succeed");
	}
	
	/**
	 * execute f2Opt once , then put result into fLogTable and set fTrialNum
	 */
	public void executeOne() {
		long start = System.currentTimeMillis();
		f2Opt.execute();
		long end = System.currentTimeMillis();
		int length = f2Opt.getTour().getLength();
		log(start, end, length);
		System.out.println(fTrialNum + ": " + length);
		fTrialNum ++;
	}
	
	/**
	 * put  average row and sample variance row into fLogTable
	 */
	public void logStat(String... keys) {
		HashMap<String, String> avgRow = new HashMap<>();
		avgRow.put(fTrialKey, "Averege");
		HashMap<String, String> sVRow = new HashMap<>();
		sVRow.put(fTrialKey, "SampleVariance");

		for(String key : keys) {
			double sV = fLogTable.calculateSampleStdDev(key);
			sVRow.put(key, String.valueOf(sV));
			double avg = fLogTable.calculateAverage(key);
			avgRow.put(key, String.valueOf(avg));
		}
		fLogTable.addRow(avgRow);
		fLogTable.addRow(sVRow);
	}
	
	
	/**
	 * put execution time and tour-length into fLogTable
	 * @param start start time
	 * @param end end time
	 * @param length tour-length
	 */
	public void log(long start, long end, int length) {
		HashMap<String, String> row = new HashMap<>();
		row.put(fTimeKey, String.valueOf(end - start));
		row.put(fLengthKey, String.valueOf(length));
		row.put(fTrialKey, String.valueOf(fTrialNum));
		fLogTable.addRow(row);
	}
	
	/**
	 * write fLogTable into fCsvDst in csv-format 
	 */
	public void writeCsv() {
		try {
            fLogTable.writeTo(fCsvDst);
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
		
	public static void main(String[] argv) throws Exception{
		TCountry  country = new TCountry();
		String srcDirName = "dataSrc/";
		//String countrySrc = srcDirName + "test7.tsp";
		//String countrySrc = srcDirName + "ca4663.tsp";
		//String countrySrc = srcDirName + "ja9847.tsp";
		//String countrySrc = srcDirName + "bm33708.tsp";
		//String countrySrc = srcDirName + "ch71009.tsp";
		String countrySrc = srcDirName + "mona-lisa100K.tsp";

		try(BufferedReader br = new BufferedReader(new FileReader(countrySrc))){
			country.readFrom(br);
		}
		String countryName = country.getName();

		/** readFrom xxxSrc */
		String iNlSrc = srcDirName + "inL_" + countryName;
		String nLSrc = srcDirName + "nL_" + countryName;
		TTour tour =  new TTour(country);

		/** nearest-size */
		int k = 50;

		T2Opt arg2Opt = new T2Opt(iNlSrc, nLSrc, tour, k);
		int trialNum = 10;
		Main executer = new Main(trialNum, arg2Opt);
		executer.execute();
	}
}
