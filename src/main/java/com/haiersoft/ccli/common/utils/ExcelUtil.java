package com.haiersoft.ccli.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import javax.servlet.http.HttpServletRequest;
/**
 * 共分为六部完成根据模板导出excel操作：<br/>
 * 第一步、设置excel模板路径（setSrcPath）<br/>
 * 第二步、设置要生成excel文件路径（setDesPath）<br/>
 * 第三步、设置模板中哪个Sheet列（setSheetName）<br/>
 * 第四步、获取所读取excel模板的对象（getSheet）<br/>
 * 第五步、设置数据（分为6种类型数据：setCellStrValue、setCellDateValue、setCellDoubleValue、setCellBoolValue、setCellCalendarValue、setCellRichTextStrValue）<br/>
 * 第六步、完成导出 （exportToNewFile）<br/>
 *
 * @author Administrator
 */
public class ExcelUtil {
    private String srcXlsPath = "";// // excel模板路径
    private String desXlsPath = "";
    @SuppressWarnings("unused")
	private String sheetName = "";
    POIFSFileSystem fs = null;
    HSSFWorkbook wb = null;
    HSSFSheet sheet = null;
    HSSFCellStyle style = null;

    public HSSFWorkbook getWbObj() {
        return wb;
    }

    public HSSFSheet getSheetObj() {
        return sheet;
    }

    /**
     * 第一步、设置excel模板路径
     *
     * @param srcXlsPath
     */
    public void setSrcPath(String srcXlsPath) {
        this.srcXlsPath = srcXlsPath;
    }

    /**
     * 第二步、设置要生成excel文件路径
     *
     * @param desXlsPath
     */
    public void setDesPath(String desXlsPath) {
        this.desXlsPath = desXlsPath;
    }

    /**
     * 第三步、设置模板中哪个Sheet列
     *
     * @param sheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 第四步、获取所读取excel模板的对象
     */
    public void getSheet() {
        try {
            File file = new File(srcXlsPath);
            if (!file.exists()) {
                System.out.println("模板文件:" + srcXlsPath + "不存在!");
                return;
            }
            fs = new POIFSFileSystem(new FileInputStream(file));
            wb = new HSSFWorkbook(fs);
            //wb= new HSSFWorkbook(new FileInputStream(new File(srcXlsPath)));
            sheet = wb.getSheetAt(0);
            if (null != sheet) {
                sheet.getPrintSetup().setLandscape(true);
                //System.out.println(sheet.getSheetName());
            } else {
                System.out.println("sheet is null");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第四步、获取所读取excel模板的对象(隐藏某列)
     */
    public void getSheet(int hs) {
        try {
            File file = new File(srcXlsPath);
            if (!file.exists()) {
                System.out.println("模板文件:" + srcXlsPath + "不存在!");
                return;
            }
            fs = new POIFSFileSystem(new FileInputStream(file));
            wb = new HSSFWorkbook(fs);
            //wb= new HSSFWorkbook(new FileInputStream(new File(srcXlsPath)));
            sheet = wb.getSheetAt(0);
            if (null != sheet) {
                sheet.getPrintSetup().setLandscape(true);
                sheet.setForceFormulaRecalculation(true);
                sheet.setColumnHidden(hs, true);//隐藏列
                //System.out.println(sheet.getSheetName());
            } else {
                System.out.println("sheet is null");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第五步、设置字符串类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--字符串类型的数据
     */
    public void setTitleCellStrValue(int rowIndex, int cellnum, String value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        HSSFCell qcell = row.getCell(cellnum-1);
        cell.setCellValue(value);
        cell.setCellStyle(qcell.getCellStyle());
    }
    
    public void createCellStrValue(int rowIndex, int cellnum, String value) {
    	HSSFRow row=sheet.createRow(rowIndex);
    	HSSFCell cell=row.createCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        }
        cell.setCellStyle(style);
    }
    
    public void setCellStrValue(int rowIndex, int cellnum, String value) {
    	HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        }
        cell.setCellStyle(style);
    }
    /**
     * 删除列
     * @param rowIndex
     * @param cellnum
     * @param value
     */
    public void removeCell(int rowIndex, int cellnum) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        row.removeCell(cell);
    }

    /**
     * sum函数(用于带小计的表求总计时使用)
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param objList--存放单元格位置的List
     * @param type--判断是中文还是英文
     */
    public void setCellSumValue(int rowIndex, int cellnum, List<String> objList, String type, String type2) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        StringBuffer sb = new StringBuffer();
        if(type2 !=null && "合计".equals(type2)){
            if ("1".equals(type) || "2".equals(type)) {
                sb.append("ROUND(SUMPRODUCT( ( (B4:B" + String.valueOf(rowIndex - 1) + "=\"在库\") + (B4:B" + String.valueOf(rowIndex - 1) + "=\"入库\")+(B4:B" + String.valueOf(rowIndex - 1) + "=\"出\"))*" + this.getCellName(4, cellnum) + ":" + this.getCellName(rowIndex - 1, cellnum) + "),2)");
            } else {
                sb.append("ROUND(SUMPRODUCT( ( (B4:B" + String.valueOf(rowIndex - 1) + "=\"pre_in\") + (B4:B" + String.valueOf(rowIndex - 1) + "=\"in\")+(B4:B" + String.valueOf(rowIndex - 1) + "=\"out\"))*" + this.getCellName(4, cellnum) + ":" + this.getCellName(rowIndex - 1, cellnum) + "),2)");
            }
            cell.setCellFormula(sb.toString());
        }else{
            if ("1".equals(type) || "2".equals(type)) {
                sb.append("SUMPRODUCT( ( (B4:B" + String.valueOf(rowIndex - 1) + "=\"在库\") + (B4:B" + String.valueOf(rowIndex - 1) + "=\"入库\")+(B4:B" + String.valueOf(rowIndex - 1) + "=\"出\"))*" + this.getCellName(4, cellnum) + ":" + this.getCellName(rowIndex - 1, cellnum) + ")");
            } else {
                sb.append("SUMPRODUCT( ( (B4:B" + String.valueOf(rowIndex - 1) + "=\"pre_in\") + (B4:B" + String.valueOf(rowIndex - 1) + "=\"in\")+(B4:B" + String.valueOf(rowIndex - 1) + "=\"out\"))*" + this.getCellName(4, cellnum) + ":" + this.getCellName(rowIndex - 1, cellnum) + ")");
            }
            cell.setCellFormula(sb.toString());
        }
    }


    /**
     * sum函数(用于带小计的表求总计时使用,有在库筛选条件)
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param objList--存放单元格位置的List
     * @param type--判断是中文还是英文
     */
    public void setCellSumValue2(int rowIndex, int cellnum, List<String> objList, int hs, String type) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        //cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        StringBuffer sb = new StringBuffer();
        int size = objList.size();
//		sb.append("SUMIFS("+this.getCellName(4, cellnum)+":"+this.getCellName(Integer.valueOf(objList.get(size-1)), cellnum)+",B4:B"+objList.get(size-1)+",\"在库\""+","+this.getCellName(4, hs)+":"+this.getCellName(Integer.valueOf(objList.get(size-1)), hs)+",\"0\")");
        if ("1".equals(type) || "2".equals(type)) {
            sb.append("SUMPRODUCT(" + "(B4:B" + objList.get(size - 1) + "=\"在库\") * (" + this.getCellName(4, hs) + ":" + this.getCellName(Integer.valueOf(objList.get(size - 1)), hs) + "=0)*" + this.getCellName(4, cellnum) + ":" + this.getCellName(Integer.valueOf(objList.get(size - 1)), cellnum) + ")");
        } else {
            sb.append("SUMPRODUCT(" + "(B4:B" + objList.get(size - 1) + "=\"pre_in\") * (" + this.getCellName(4, hs) + ":" + this.getCellName(Integer.valueOf(objList.get(size - 1)), hs) + "=0)*" + this.getCellName(4, cellnum) + ":" + this.getCellName(Integer.valueOf(objList.get(size - 1)), cellnum) + ")");
        }
        cell.setCellFormula(sb.toString());
    }
    
    public void createCellSumValue(int rowIndex, int cellnum, String begin, String end) {
    	HSSFRow row = sheet.createRow(rowIndex);
    	HSSFCell cell=row.createCell(cellnum);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellFormula("SUM(" + begin + ":" + end + ")");
    }

    /**
     * sum函数(用于求小计)
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param objList--存放单元格位置的List
     */
    public void setCellSumValue(int rowIndex, int cellnum, String begin, String end,String type) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        //cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        if(type !=null && "合计".equals(type)){
            cell.setCellFormula("ROUND(SUM(" + begin + ":" + end + "),2)");
        }else{
            cell.setCellFormula("SUM(" + begin + ":" + end + ")");
        }
    }
    
    /**
     * sumIF函数(用于求小计)
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param objList--存放单元格位置的List
     */
    public void setCellSumIfValue(int rowIndex, int cellnum,String ifBegin,String ifEnd,String begin,String end,String ntype,String type) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        //cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        if(null!=type&&!"".equals(type)){
        	if ("1".equals(ntype) || "2".equals(ntype)) {
        		cell.setCellFormula("SUMIF(C"+ifBegin+":C"+ifEnd+",\"在库\","+begin+":"+end+")");
        	}else{
        		cell.setCellFormula("SUMIF(C"+ifBegin+":C"+ifEnd+",\"pre_in\","+begin+":"+end+")");
        	}
        }else{
        	if ("1".equals(ntype) || "2".equals(ntype)) {
        		cell.setCellFormula("SUMIF(B"+ifBegin+":B"+ifEnd+",\"在库\","+begin+":"+end+")");
        	}else{
        		cell.setCellFormula("SUMIF(B"+ifBegin+":B"+ifEnd+",\"pre_in\","+begin+":"+end+")");
        	}
        }
    }
    
    public void setCellSumIfTotal(int rowIndex, int cellnum,String ifBegin,String ifEnd,String begin,String end,String ntype,String type) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        //cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        if(type !=null && "合计".equals(type)){
            if ("1".equals(ntype) || "2".equals(ntype)) {
                cell.setCellFormula("ROUND(SUMIF(A"+ifBegin+":A"+ifEnd+",\"小计 ：\","+begin+":"+end+"),2)");
            }else{
                cell.setCellFormula("ROUND(SUMIF(A"+ifBegin+":A"+ifEnd+",\"SubTotal：\","+begin+":"+end+"),2)");
            }
        }else{
            if ("1".equals(ntype) || "2".equals(ntype)) {
                cell.setCellFormula("SUMIF(A"+ifBegin+":A"+ifEnd+",\"小计 ：\","+begin+":"+end+")");
            }else{
                cell.setCellFormula("SUMIF(A"+ifBegin+":A"+ifEnd+",\"SubTotal：\","+begin+":"+end+")");
            }
        }
    }
    
    
    public void createCellSumValue2(int rowIndex, int cellnum, String begin, String end, String beginB, String endB, String hsBegin, String hsEnd, String ntype,String type) {
    	HSSFRow row=sheet.createRow(rowIndex);
    	HSSFCell cell=row.createCell(cellnum);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        if(null!=type&&!"".equals(type)){
	        if ("1".equals(ntype) || "2".equals(ntype)) {
	            cell.setCellFormula("SUMPRODUCT( (C" + beginB + ":C" + endB + "=\"在库\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        } else {
	            cell.setCellFormula("SUMPRODUCT( (C" + beginB + ":C" + endB + "=\"pre_in\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        }
        }else{
        	if ("1".equals(ntype) || "2".equals(ntype)) {
	            cell.setCellFormula("SUMPRODUCT( (B" + beginB + ":B" + endB + "=\"在库\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        } else {
	            cell.setCellFormula("SUMPRODUCT( (B" + beginB + ":B" + endB + "=\"pre_in\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        }
        }
    }
    /**
     * sum函数(用于求小计，有在库筛选条件)
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param begin--初始位置
     * @param end--结束位置
     * @param begin--筛选条件初始位置
     * @param end--筛选条件结束位置
     * @param type--判断是中文还是英文
     */
    public void setCellSumValue2(int rowIndex, int cellnum, String begin, String end, String beginB, String endB, String hsBegin, String hsEnd, String ntype,String type) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        //cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        if(null!=type&&!"".equals(type)){
	        if ("1".equals(ntype) || "2".equals(ntype)) {
	            cell.setCellFormula("SUMPRODUCT( (C" + beginB + ":C" + endB + "=\"在库\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        } else {
	            cell.setCellFormula("SUMPRODUCT( (C" + beginB + ":C" + endB + "=\"pre_in\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        }
        }else{
        	if ("1".equals(ntype) || "2".equals(ntype)) {
	            cell.setCellFormula("SUMPRODUCT( (B" + beginB + ":B" + endB + "=\"在库\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        } else {
	            cell.setCellFormula("SUMPRODUCT( (B" + beginB + ":B" + endB + "=\"pre_in\") * (" + hsBegin + ":" + hsEnd + "=0) * " + begin + ":" + end + ")");
	        }
        }
    }


    /**
     * 将单元格转换为字母+数字的名称
     *
     * @param rowIndex--行值
     * @param cellnum--列值（需要转换为字母）
     */
    private String getCellName(int rowIndex, int cellnum) {
        int zero = cellnum / 26;
        if (zero != 0) {
            char c = (char) (zero + 64);
            char d = (char) ((cellnum - zero * 26) + 65);
            String name = String.valueOf(c) + String.valueOf(d) + String.valueOf(rowIndex);
            return name;
        } else {
            char c = (char) (cellnum + 65);
            String name = String.valueOf(c) + String.valueOf(rowIndex);
            return name;
        }
    }

    /**
     * 第五步、设置字符串类型的数据（无边框）
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--字符串类型的数据
     */
    public void setCellStrValue2(int rowIndex, int cellnum, String value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
    }
//	public void setCellStrValueStyp(int rowIndex, int cellnum, String value) {
//		if(style==null){
//			style= wb.createCellStyle();
//			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			style.setBottomBorderColor(HSSFColor.BLACK.index);
//			style.setTopBorderColor(HSSFColor.BLACK.index);
//			style.setLeftBorderColor(HSSFColor.BLACK.index);
//			style.setRightBorderColor(HSSFColor.BLACK.index);
//		}
//		HSSFRow row =sheet.getRow(rowIndex);
//		if(null == row){
//			row=sheet.createRow(rowIndex);
//		}
//		row.createCell(cellnum);
//		HSSFCell cell = row.getCell(cellnum);
//		cell.setCellValue(value);
//		cell.setCellStyle(style);
//	}

    /**
     * 第五步、设置日期/时间类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--日期/时间类型的数据
     */
    public void setCellDateValue(int rowIndex, int cellnum, Date value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
    }

    /**
     * 第五步、设置浮点类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--浮点类型的数据
     */
    public void setCellDoubleValue(int rowIndex, int cellnum, double value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        if (style == null) {
            //HSSFDataFormat df = wb.createDataFormat();
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        }
        cell.setCellStyle(style);
    }

    /**
     * 第五步、设置浮点类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--浮点类型的数据
     */
    public void setCellDoubleValue0215(int rowIndex, int cellnum, double value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
//		if(style==null){
//			HSSFDataFormat df = wb.createDataFormat();
//			style= wb.createCellStyle();
//			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			style.setBottomBorderColor(HSSFColor.BLACK.index);
//			style.setTopBorderColor(HSSFColor.BLACK.index);
//			style.setLeftBorderColor(HSSFColor.BLACK.index);
//			style.setRightBorderColor(HSSFColor.BLACK.index);
// 			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));  
//		}
//		cell.setCellStyle(style);
    }


    /**
     * 第五步、设置字符串类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--字符串类型的数据
     */
    public void setCellStrValue3(int rowIndex, int cellnum, String value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        }
        cell.setCellStyle(style);
    }

    /**
     * 第五步、设置Bool类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--Bool类型的数据
     */
    public void setCellBoolValue(int rowIndex, int cellnum, boolean value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
    }

    /**
     * 第五步、设置日历类型的数据
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--日历类型的数据
     */
    public void setCellCalendarValue(int rowIndex, int cellnum, Calendar value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
    }

    /**
     * 第五步、设置富文本字符串类型的数据。可以为同一个单元格内的字符串的不同部分设置不同的字体、颜色、下划线
     *
     * @param rowIndex--行值
     * @param cellnum--列值
     * @param value--富文本字符串类型的数据
     */
    public void setCellRichTextStrValue(int rowIndex, int cellnum, RichTextString value) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (null == row) {
            row = sheet.createRow(rowIndex);
        }
        row.createCell(cellnum);
        HSSFCell cell = row.getCell(cellnum);
        cell.setCellValue(value);
        if (style == null) {
            style = wb.createCellStyle();
            style.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
            style.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }
        cell.setCellStyle(style);
    }


//	//加边框
//	public void setBorder(){
//		 HSSFCell cell1 = row.createCell((short)1);
//		if(style==null){
//			style= wb.createCellStyle();
//		}
//		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM_DASHED); //下边框
//		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM_DASH_DOT_DOT);//左边框
//		style.setBorderTop(HSSFCellStyle.BORDER_DASH_DOT);//上边框
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	}
//	

    /**
     * 第六步、完成导出到设置的导出路径
     */
    public void exportToNewFile() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(desXlsPath);
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeFileName(String fileName) {
        return StringUtils.strToUTF8(fileName + ".xls");
    }

    public static String getRootPath(HttpServletRequest request) {

        String filePath = PropertiesUtil.getPropertiesByName("filepath", "application");

        if (StringUtils.isNull(filePath)) {
            filePath = request.getSession().getServletContext().getRealPath("/");
        }

        return filePath;
    }

    public static void main(String args[]) {
        String getPath = "";//ExcelUtil.class.getClassLoader().getResource("").toString();
        getPath = getPath.replaceAll("file:/", "");
        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.setSrcPath("C:\\inreport.xls");
        excelUtil.setDesPath("C:\\inreport2.xls");
        excelUtil.setSheetName("Sheet1");
        excelUtil.getSheet();
        excelUtil.setCellStrValue(9, 0, "91");
        excelUtil.setCellStrValue(9, 1, "92");
        excelUtil.setCellStrValue(9, 2, "93");
        excelUtil.setCellStrValue(9, 3, "94");
        excelUtil.exportToNewFile();
    }

}