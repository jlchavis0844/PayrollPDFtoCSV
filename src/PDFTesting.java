import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		String filePath = "H:\\Documents\\test.pdf";
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"PDF Files", "pdf");
		chooser.setFileFilter(filter);
		
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			filePath = chooser.getSelectedFile().getAbsolutePath();
		}
		
		File f = new File(filePath);
		ArrayList<Person> people = new ArrayList<>();

		try {
			pdDoc = PDDocument.load(f);
			pdfStripper = new PDFTextStripper();

			String parsedText = pdfStripper.getText(pdDoc);
			//			System.out.println(parsedText);

			BufferedWriter textOut = new BufferedWriter(new FileWriter("H:\\Documents\\PDFtest.txt"));
			textOut.write(parsedText);
			textOut.close();
			
			ArrayList<String> lines = new ArrayList<String>
			(Arrays.asList(parsedText.split("\n")));

			String[] tokens = null;
			String line = "";
			PayLine temp = null;
			Person lastPerson = null;
			for (int i = 0, LEN = lines.size();i < LEN; i++){
				line = lines.get(i);
				if(line.startsWith("SSN")){//find bio header

					if(lines.get(i+2).contains("Continued")){
						i += 2; //skip continue and bio line 
						continue;//back to the top
					}

					i++;//load next line for BIO data
					line = lines.get(i).replaceAll("  ", " ");
					tokens = line.split(" ");
					lastPerson = new Person(tokens);
					people.add(lastPerson);
				} else if(line.startsWith("St %")){
					i++;
					temp = new PayLine(lines.get(i).split(" "));
					temp.setWarrantEFT(lastPerson.getWarrantEFT());
					lastPerson.addPayLine(temp);
				}
			}
			String savePath = "H:\\Documents";
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setDialogTitle("Where would you like to save to?");
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
		    returnVal = chooser.showOpenDialog(null);
		    
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				savePath = chooser.getSelectedFile().toString();
			} else {
				System.err.println("No Selection ");
				System.exit(-1);
			}
			
			if(!savePath.endsWith("\\")){
				savePath += "\\";
			}
			
			BufferedWriter csvBioOut = new BufferedWriter(new FileWriter(savePath +"outBioFile.csv"));
			BufferedWriter csvPayOut = new BufferedWriter(new FileWriter(savePath +"outPayFile.csv"));
			String bioHeader = "last, first, mi, empClass, pay, locn, fit, sit, fm, dn, retSys, retOpt, warrantEFT, p";
			String payHeader = "st, units, rate, rty, salary, posnDesc, earn, txty, pty, "
					+ "fq, tx, hw, rb, rc, rt, from, to, warrantEFT";
			csvBioOut.write(bioHeader + "\n");
			csvPayOut.write(payHeader + "\n");
			
			for(Person p: people){
				csvBioOut.write(p.toString());
				for(PayLine payLine: p.getPayLines()){
					csvPayOut.write(payLine.toString());
				}
			}
			
			csvBioOut.close();
			csvPayOut.close();
			
		} catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
