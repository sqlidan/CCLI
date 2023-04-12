package com.haiersoft.ccli.common.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 *
 */
public class ExcelUtil2 {
	private String srcXlsPath = "";// // excel模板路径
	private String desXlsPath = "";
	@SuppressWarnings("unused")
	private String sheetName = "";
	POIFSFileSystem fs = null;
	XSSFWorkbook wb = null;
	XSSFSheet sheet = null;
	XSSFCellStyle style=null; 
	
	public XSSFWorkbook getWbObj() {
		return wb;
	}
	
	public XSSFSheet getSheetObj() {
		return sheet;
	}
	/**
	 * 第一步、设置excel模板路径
	 * @param srcXlsPath
	 */
	public void setSrcPath(String srcXlsPath) {
		this.srcXlsPath = srcXlsPath;
	}

	/**
	 * 第二步、设置要生成excel文件路径
	 * @param desXlsPath
	 */
	public void setDesPath(String desXlsPath) {
		this.desXlsPath = desXlsPath;
	}

	/**
	 * 第三步、设置模板中哪个Sheet列
	 * @param sheetName
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * 第四步、获取所读取excel模板的对象
	 */
	@SuppressWarnings("resource")
	public void getSheet() {
		try {
			FileInputStream file = new FileInputStream(new File(srcXlsPath));
			if(!new File(srcXlsPath).exists()){
				System.out.println("模板文件:"+srcXlsPath+"不存在!");
				return;
			}
//			fs = new POIFSFileSystem(new FileInputStream(file));
			wb = new XSSFWorkbook(file);
			//wb= new HSSFWorkbook(new FileInputStream(new File(srcXlsPath)));
			sheet = wb.getSheetAt(0);
			if(null!=sheet){
				//System.out.println(sheet.getSheetName());
			}else{
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
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--字符串类型的数据
	 */
	public void setCellStrValue(int rowIndex, int cellnum, String value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}
	
	/**
	 * sum函数(用于带小计的表求总计时使用)
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param objList--存放单元格位置的List
	 */
	public void setCellSumValue(int rowIndex, int cellnum,List<String> objList) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		//cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		StringBuffer sb= new StringBuffer();
		sb.append("SUM(");
		int size= objList.size();
		for(int i=0;i<size;i+=2){
			if(i==0){
				sb.append(""+this.getCellName(Integer.valueOf(objList.get(i)), cellnum)+":"+this.getCellName(Integer.valueOf(objList.get(i+1)), cellnum));
			}else{
				sb.append(","+this.getCellName(Integer.valueOf(objList.get(i)), cellnum)+":"+this.getCellName(Integer.valueOf(objList.get(i+1)), cellnum));
			}
		}
		sb.append(")");
	    cell.setCellFormula(sb.toString());
	}
	
	
	/**
	 * sum函数(用于带小计的表求总计时使用,有在库筛选条件)
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param objList--存放单元格位置的List
	 */
	public void setCellSumValue2(int rowIndex, int cellnum,List<String> objList) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		//cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		StringBuffer sb= new StringBuffer();
		int size= objList.size();
		sb.append("SUMIF(B4:B"+objList.get(size-1)+",\"在库\","+this.getCellName(4, cellnum)+":"+this.getCellName(Integer.valueOf(objList.get(size-1)), cellnum)+")");
	    cell.setCellFormula(sb.toString());
	}
	
	/**
	 * sum函数(用于求小计)
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param objList--存放单元格位置的List
	 */
	public void setCellSumValue(int rowIndex, int cellnum,String begin,String end) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		//cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
	    cell.setCellFormula("SUM("+begin+":"+end+")");
	}
	
	/**
	 * sum函数(用于求小计，有在库筛选条件)
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param begin--初始位置
	 * @param end--结束位置
	 * @param begin--筛选条件初始位置
	 * @param end--筛选条件结束位置
	 */
	public void setCellSumValue2(int rowIndex, int cellnum,String begin,String end,String beginB,String endB) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		//cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
	    cell.setCellFormula("SUMIF(B"+beginB+":B"+endB+",\"在库\","+begin+":"+end+")");
	}
	
	
	/**
	 * 将单元格转换为字母+数字的名称
	 * @param rowIndex--行值
	 * @param cellnum--列值（需要转换为字母）
	 */
	private String getCellName(int rowIndex, int cellnum){
		char c = (char)(cellnum+65);
		String name = String.valueOf(c)+String.valueOf(rowIndex);
		return name;
	}
	
	/**
	 * 第五步、设置字符串类型的数据（无边框）
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--字符串类型的数据
	 */
	public void setCellStrValue2(int rowIndex, int cellnum, String value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
	}
	/**
	 * 第五步、设置日期/时间类型的数据
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--日期/时间类型的数据
	 */
	public void setCellDateValue(int rowIndex, int cellnum, Date value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}

	/**
	 * 第五步、设置浮点类型的数据
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--浮点类型的数据
	 */
	public void setCellDoubleValue(int rowIndex, int cellnum, double value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}

	/**
	 * 第五步、设置Bool类型的数据
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--Bool类型的数据
	 */
	public void setCellBoolValue(int rowIndex, int cellnum, boolean value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}

	/**
	 * 第五步、设置日历类型的数据
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--日历类型的数据
	 */
	public void setCellCalendarValue(int rowIndex, int cellnum, Calendar value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}

	/**
	 * 第五步、设置富文本字符串类型的数据。可以为同一个单元格内的字符串的不同部分设置不同的字体、颜色、下划线
	 * @param rowIndex--行值
	 * @param cellnum--列值
	 * @param value--富文本字符串类型的数据
	 */
	public void setCellRichTextStrValue(int rowIndex, int cellnum,RichTextString value) {
		XSSFRow row =sheet.getRow(rowIndex);
		if(null == row){
			row=sheet.createRow(rowIndex);
		}
		row.createCell(cellnum);
		XSSFCell cell = row.getCell(cellnum);
		cell.setCellValue(value);
		if(style==null){
			style= wb.createCellStyle();
			style.setBorderTop(XSSFCellStyle.BORDER_DOTTED);
			style.setBorderBottom(XSSFCellStyle.BORDER_DOTTED);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		}
		cell.setCellStyle(style);
	}

	
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
		}finally{
			try {
				if(null != out){
				out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	 
	public   static   void  main(String args[]) {
		String getPath="";//ExcelUtil.class.getClassLoader().getResource("").toString();
    	getPath=getPath.replaceAll("file:/", "");
    	ExcelUtil2 excelUtil=new ExcelUtil2();
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