import java.util.ArrayList;

public class PayLine {
	private String st;
	private String percent;
	private double units;
	private double rate;
	private String rty;
	private double salary;
	private String posnDesc;
	private String earn;
	private String txty;
	private String pty;
	private String sched;
	private String range;
	private String step;
	private String cd;
	private String fq;
	private String tx;
	private String hw;
	private String rb;
	private String rc;
	private String rt;
	private String from;
	private String to;
	private int SIZE;
	private ArrayList<String> positions;
	private String warrantEFT;
	/**
	 * hard coding the constructor for now to simplify the parsing. Doesn't
	 * seem to be much variation on the lines.
	 * @param tokens
	 */
	public PayLine(String[] tokens){
		SIZE = tokens.length;
		int currToken = 0;
		buildPositions();
		
		for(int k = 0; k < SIZE; k++){
			tokens[k] = tokens[k].replaceAll(",","").trim();
		}
		
		st = tokens[0];
		units = Double.valueOf(tokens[1]);
		rate = Double.valueOf(tokens[2]);
		rty = tokens[3];
		salary = Double.valueOf(tokens[4]);
		
		if((tokens[5] + tokens[6]).equals("LTSUB")){
			posnDesc = (tokens[5] + " " + tokens[6]);
			currToken = 6;
		} else {
			posnDesc = "";
			currToken = 5;
		}
		
		for(int i = currToken; !positions.contains(tokens[i]); i++){
			if((tokens[i] + tokens[i+1]).equals("LTSUB")){
				posnDesc = (posnDesc + " " + (tokens[i] + " " + tokens[i+1])).trim();
				i++;
			} else {
			posnDesc = (posnDesc + " " + tokens[i]).trim();
			currToken = i;
			}
		}
		
		if(posnDesc.equals("")){
			posnDesc = tokens[5];
		}
		
		currToken++;
		earn = tokens[currToken++];
		txty = tokens[currToken++];
		pty = tokens[currToken++];
		fq = tokens[currToken++];
		tx = tokens[currToken++];
		hw = tokens[currToken++];
		
		if (currToken < SIZE && isNumeric(tokens[currToken].replaceAll(",",""))){
			rb = tokens[currToken++].replaceAll(",","");
		}
		
		if (currToken < SIZE){
			rc = tokens[currToken++];
		}
		
		if (currToken < SIZE && tokens[currToken].length() == 3){
			rt= tokens[currToken++];
		}
		
		if (currToken < SIZE && tokens[currToken].length() == 8){
			from = tokens[currToken];
		}
		
		if (currToken < SIZE && tokens[currToken].length() == 8){
			to = tokens[currToken];
		}
		
		
		//System.out.println(this);
	}
	
	private void buildPositions(){
		positions = new ArrayList<String>();
		positions.add("ACC9");positions.add("ADD2");positions.add("BEXN");
		positions.add("CAR1");positions.add("CASH");positions.add("COA9");
		positions.add("DEPT");positions.add("DPAY");positions.add("LNG1");
		positions.add("NML");positions.add("NMLE");positions.add("OFF9");
		positions.add("OT-2");positions.add("OUTC");positions.add("PSA1");
		positions.add("RETS");positions.add("SHF9");positions.add("STP1");
		positions.add("STRM");positions.add("SUB");positions.add("SUM9");
		positions.add("ACCT");positions.add("ADD3");positions.add("BILN");
		positions.add("CAR2");positions.add("COA1");positions.add("CONF");
		positions.add("DNP");positions.add("HDLY");positions.add("LONG");
		positions.add("NML9");positions.add("NMLR");positions.add("OFFS");
		positions.add("OT-3");positions.add("PMEM");positions.add("RETC");
		positions.add("RETT");positions.add("SHFT");positions.add("STP3");
		positions.add("STU1");positions.add("SUB9");positions.add("SUMR");
		positions.add("ADD1");positions.add("ADD5");positions.add("BON2");
		positions.add("CAR9");positions.add("COA2");positions.add("CTOT");
		positions.add("DOCT");positions.add("JURY");positions.add("MAST");
		positions.add("NMLB");positions.add("NMLT");positions.add("OT-1");
		positions.add("OUT9");positions.add("PROF");positions.add("RETN");
		positions.add("RTSA");positions.add("SPEC");positions.add("STP9");
		positions.add("STU2");positions.add("SUBA");positions.add("SVDP");
		positions.add("VACP");positions.add("XALL");positions.add("VACR");
		positions.add("XTRA");positions.add("WC01");positions.add("YDDY");

	}
	
	public boolean isNumeric(String s) {  
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	} 
	
	public String toString(){
		return st + ", " + units + ", " + rate + ", " + rty + ", " + salary  + ", " 
				+ posnDesc + ", " + earn + ", " + txty + ", " + pty
				 + ", " + fq + ", " + tx + ", " + hw + ", " + rb + ", " + rc 
				 + ", " + rt + ", " + from + ", " + to + ", " + warrantEFT + "\n"; 
	}
	
	
	public String getWarrantEFT(){
		return warrantEFT;
	}
	
	public void setWarrantEFT(String warrantEFT){
		this.warrantEFT = warrantEFT;
	}
}
