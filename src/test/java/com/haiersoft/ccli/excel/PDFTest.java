package com.haiersoft.ccli.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFTest {
	
	public static final String DEST = "d://tab_table2.pdf";
    public static final String[][] DATA = {
        {"John Edward Jr.", "AAA"},
        {"Pascal Einstein W. Alfi", "BBB"},
        {"St. John", "CCC"}
    };
	public static void main(String[] args) throws FileNotFoundException, DocumentException { 
		/*Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("d://Hello.pdf"));
		document.open();
		document.add(new Paragraph("Hello World"));
		document.close();
		*/
		File file = new File(DEST);
        file.getParentFile().mkdirs();
        try {
			new PDFTest().createPdf(DEST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 public void createPdf2(String dest) throws IOException, DocumentException {
	        Document document = new Document();
	        PdfWriter.getInstance(document, new FileOutputStream(dest));
	        document.open();
	        PdfPTable table = new PdfPTable(2);
	        table.setWidthPercentage(50);
	        table.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.setWidths(new int[]{5, 1});
	        table.getDefaultCell().setBorder(Rectangle.BOX);
	        table.addCell("Name: " + DATA[0][0]);
	        table.addCell(DATA[0][1]);
	        table.addCell("Surname: " + DATA[1][0]);
	        table.addCell(DATA[1][1]);
	        table.addCell("School: " + DATA[2][0]);
	        table.addCell(DATA[1][1]);
	        document.add(table);
	        document.close();
	    }
	 
	 public void createPdf(String dest) throws DocumentException, MalformedURLException, IOException {
	        Document document = new Document();
	 
	        PdfWriter.getInstance(document, new FileOutputStream(dest));
	 
	        document.open();
	 
	        document.add(createParagraphWithTab("Name: ", DATA[0][0], DATA[0][1]));
	        document.add(createParagraphWithTab("Surname: ", DATA[1][0], DATA[1][1]));
	        document.add(createParagraphWithTab("School: ", DATA[2][0], DATA[2][1]));
	      //create title image
			Image jpeg = Image.getInstance("D:\\Img\\gjss_logo.jpg");
			jpeg.setAlignment(1000);
			jpeg.scaleAbsolute(30, 37);
			document.add(jpeg);
				
	        document.close();
	    }
	 
	    public Paragraph createParagraphWithTab(String key, String value1, String value2) {
	        Paragraph p = new Paragraph();
	        p.setTabSettings(new TabSettings(200f));
	        //p.Rectangle 
	        p.add(key);
	        p.add(value1);
	        p.add(Chunk.TABBING);
	        p.add(value2);
	        return p;
	    }
}
