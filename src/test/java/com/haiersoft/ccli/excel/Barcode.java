package com.haiersoft.ccli.excel;
import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider; 
import org.krysalis.barcode4j.tools.UnitConv; 
import org.krysalis.barcode4j.impl.code128.Code128Bean ;
public class Barcode {       
	public static void main(String[] args) { 
		OutputStream out=null;
		try {       //Create the barcode bean             
			//Code39Bean bean = new Code39Bean(); 
			Code128Bean bean = new Code128Bean();
			
			final int dpi = 150;//150;      
			
			//Configure the barcode generator             
			bean.setModuleWidth(UnitConv.in2mm(2.0f / dpi)); //makes the narrow bar  
			//width exactly one pixel             
			//bean.setWideFactor(3);             
			//bean.doQuietZone(false);                          
			//Open output file             
			File outputFile = new File("d:\\1a\\out3.png");            
			out = new FileOutputStream(outputFile);
		              //Set up the canvas provider for monochrome JPEG output                  
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);                              
			//Generate the barcode                 
			bean.generateBarcode(canvas, "A123456");                               
			//Signal end of generation                 
			canvas.finish();             
					         
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}    
		}			 
			 
	}
}