import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		String filePath = "H:\\Documents\\test.pdf";
//		JFileChooser chooser = new JFileChooser();
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//				"PDF Files", "pdf");
//		chooser.setFileFilter(filter);
//		
//		int returnVal = chooser.showOpenDialog(null);
//		if(returnVal == JFileChooser.APPROVE_OPTION) {
//			filePath = chooser.getSelectedFile().getAbsolutePath();
//		}
		
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
					lastPerson.addPayLine(lines.get(i).split(" "));
				}
			}

			BufferedWriter csvOut = new BufferedWriter(new FileWriter("H:\\Documents\\outFile.csv"));
			String header = "first, last, mi, empClass, pay, locn, fit, sit, fm, dn, retSys, retOpt, warrantEFT, p";
			csvOut.write(header + "\n");
			
			for(Person p: people){
				csvOut.write(p.toString());
			}
			
			csvOut.close();

		} catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
