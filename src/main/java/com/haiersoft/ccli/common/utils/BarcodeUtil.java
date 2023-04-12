package com.haiersoft.ccli.common.utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
 
/**
 * 条形码工具类
 */
public class BarcodeUtil {
 
    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public static String generateFile(String msg, String path,boolean hum) {
        File file = new File(path);
        if(false==file.exists()){
        	file.mkdirs();	
        }
        file = new File(path+"/"+msg+".png");
        try {
            generate(msg, new FileOutputStream(file),hum);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return msg+".png";//file.getAbsolutePath();
    }
    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream out,boolean hum) {
        if ((msg==null || "".equals(msg)) || out == null) {
            return;
        }
        String format ="image/png";//"image/jpeg";
        /*
	        Code39Bean bean = new Code39Bean();
	        // 精细度
	        final int dpi = 150;
	        // module宽度
	        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);
	 
	        // 配置对象
	        bean.setModuleWidth(moduleWidth);
	        bean.setWideFactor(3);
	        bean.doQuietZone(false);
        */
        
        Code128Bean bean = new Code128Bean();         
        final int dpi = 150;           
        // barcode        
        bean.setModuleWidth(0.55);         
        bean.setHeight(20);         
        bean.doQuietZone(false);         
        bean.setQuietZone(0);// 两边空白区         
        bean.setFontSize(7); 
        if(false == hum){
        	bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);//数字位置 
        }else{
        	bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);//数字位置 
        }
        try {
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, format, dpi,BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 生成条形码
            bean.generateBarcode(canvas, msg);
            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
        	try {                 
        		if (out != null)                     
        			out.close();             
        		} catch (IOException e) {                 
        			e.printStackTrace();             
        		} 
        }
        
    }
 
    public static void main(String[] args) {
        String msg = "123456789";
        String path = "barcode_file";
        System.out.println(generateFile(msg, path,false));
    }
}
