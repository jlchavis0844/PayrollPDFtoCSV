import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.text.Position;

public class Person {

	private String first;
	private String last;
	private String mi;
	private String empClass;
	private String pay;
	private String locn;
	private String fit;
	private String sit;
	private String fm;
	private String dn;
	private String retSys;
	private String retOpt;
	private String warrantEFT;
	private String p;
	private ArrayList<PayLine> payLines;
	private ArrayList<String> payCodes;


	public Person(String [] tokens){
		int currToken = 0;
		int SIZE = tokens.length;
		int classNum = 0;
		int breakNum = 0;
		payLines = new ArrayList<PayLine>();
		buildPayCodes();
		//lets find "class" by searching for "CERTIFICATED" or "CLASSIFIED" and trim data
		for(int k = 0; k < SIZE; k++){
			if(tokens[k].equals("CLASSIFIED") || tokens[k].equals("CERTIFICATED")){
				classNum = k;
			}
			tokens[k] = tokens[k].trim();
		}
		
		//all tokens before classNum are part of name
		last = "";
		for(int i = 0; i < classNum; i++){
			last = (last + " " + tokens[i]).trim();
			if(tokens[i].contains(",")){//quit after the comma (last name found, no reason to continue
				breakNum = i+1;
				break;
			}
		}
		last = last.replace(",", "");
		
		//last name was tokens[breakNum-1], mi might be tokens[classNum-1] so between is first name
		first = "";
		for(int i = breakNum; i < classNum - 1; i++){
			first = (first + " " + tokens[i]).trim(); 
		}
		
		if(tokens[classNum-1].length() == 1){
			mi = tokens[classNum-1];
		} else {
			first = (first + " " + tokens[classNum-1]).trim(); 
			mi = "";
		}
		
		empClass = tokens[classNum] + " " + tokens[classNum+1]; 
		
		currToken = classNum + 2;
//		if(currToken < SIZE && tokens[currToken].contains(",")){
//			last = tokens[currToken].replaceAll(",", "").trim();
//			currToken++;
//		} else {
//			System.err.println("no comma in last name, setting null");
//			last = "NULL";
//		}
//
//		if(currToken < SIZE && tokens[currToken].length() > 2){
//			first = tokens[currToken].trim();
//			currToken++;			
//		} else {
//			System.err.println("First name not found, setting null");
//			first = "NULL";
//		}
//
//		if(currToken < SIZE && tokens[currToken].length() == 1){
//			mi = tokens[currToken].trim();
//			currToken++;
//		} else {
//			mi = "";
//		}
//
//		int loops = 0;
//		int start = currToken;
//		empClass = "";
//		
//		while(currToken < SIZE && !payCodes.contains((tokens[currToken])) && loops < 10){
//			empClass += (" " + tokens[currToken]);
//			currToken++;
//			loops++;
//		}
		
//		
//		if(loops >= 9){//something went wrong if true, reset token
//			currToken = start;//reset
//		}
//
//		
		if(currToken < SIZE && payCodes.contains((tokens[currToken]))){
			pay = tokens[currToken];
			currToken++;
		} else {
			System.err.println("could not identify pay code, setting 000");
			pay = "000";
		}

		Pattern patt = Pattern.compile("[A-Za-z]{1}[0-9]{2}");
		int loops = 0;
		int start = currToken;
		locn = "";
		//scan until fit regex code regex passes
		while(currToken < SIZE && !patt.matcher(tokens[currToken]).matches() && loops < 10){//stack name tokens up to 10
			locn += (" " + tokens[currToken]);
			currToken++;
			loops++;
		}

		if(loops >= 9){//something went wrong if true, reset token
			currToken = start;//reset
		}

		//fit
		if(currToken < SIZE && patt.matcher(tokens[currToken]).matches()){
			fit = tokens[currToken];
			currToken++;
		} else {
			System.err.println("could not identify fit code, setting NUL");
			pay = "NUL";
		}

		//sit
		if(currToken < SIZE && patt.matcher(tokens[currToken]).matches()){
			sit = tokens[currToken];
			currToken++;
		} else {
			System.err.println("could not identify sit code, setting NUL");
			pay = "NUL";
		}

		if(currToken < SIZE && tokens[currToken].length() == 4){
			fm = tokens[currToken];
			currToken++;
		} else {
			System.err.println("could not find F/M token, using NULL");
			fm = "NULL";
		}

		if(currToken < SIZE && tokens[currToken].length() == 1){
			dn = tokens[currToken];
			currToken++;
		} else {
			System.err.println("could not find F/M token, using \"\"");
			dn = "";
		}

		patt = Pattern.compile("[0-9]{2}");
		if(currToken < SIZE && patt.matcher(tokens[currToken].trim()).matches()){
			retSys = tokens[currToken];
			currToken++;
		} else {
			System.err.println("Couldn't find RetSys, using 00");
			retSys = "00";
		}

		if(currToken < SIZE && tokens[currToken].trim().contains("/")){
			retOpt = tokens[currToken];
			currToken++;
		} else {
			System.err.println("Couldn't find Ret Opt, using \"\"");
			retOpt = "";
		}
		
		if(currToken < SIZE && tokens[currToken].trim().contains("-")){
			warrantEFT = tokens[currToken];
			currToken++;
		} else {
			System.err.println("Couldn't find warrantEFT, using \"\"");
			warrantEFT = "";
		}
		
		if(currToken < SIZE && tokens[currToken].trim().length() == 1){
			p = tokens[currToken];
			currToken++;
		} else {
			System.err.println("Couldn't find P, using \"\"");
			p = "";
		}

		//System.out.println(this);
	}
	
	public String toString(){
		String header = "first, last, mi, empClass, pay, locn, fit, sit, fm, dn, retSys, retOpt, warrantEFT, p";
		String info = last + "," +first + "," +mi + "," +empClass 
				+ "," +pay + "," +locn + "," +fit + "," +sit + "," 
				+fm + "," +dn + "," +retSys + "," +retOpt + "," +warrantEFT 
				+ "," + p;
		

		//return header + "\n" + info + "\n" + plStr;
		return info + "\n";
	}

	public ArrayList<PayLine> getPayLines(){
		return payLines;
	}
	
	public String getWarrantEFT(){
		return warrantEFT;
	}
	
	public boolean isNumeric(String s) {  
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	} 
	
	private void buildPayCodes(){
		payCodes = new ArrayList<>();
		payCodes.add("114");
		payCodes.add("108");
		payCodes.add("000");
		payCodes.add("303");
		payCodes.add("302");
		payCodes.add("106");
		payCodes.add("202");
		payCodes.add("107");
		payCodes.add("113");
		payCodes.add("205");
		payCodes.add("109");
		payCodes.add("205");
		payCodes.add("113");
		payCodes.add("201");
		payCodes.add("099");
		payCodes.add("302K");
		payCodes.add("205");
		payCodes.add("107");
		payCodes.add("110");
	}
	
	public boolean addPayLine(PayLine payLine){
		return payLines.add(payLine);
	}
	
	public boolean addPayLine(String[] payLine){
		return payLines.add(new PayLine(payLine));
	}
}
