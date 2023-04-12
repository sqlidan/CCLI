package com.haiersoft.ccli.common.utils;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.entity.params.ExcelForEachParams;
import org.jeecgframework.poi.excel.export.styler.IExcelExportStyler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ExcelStyleUtil implements IExcelExportStyler {
    private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");

    private static final short FONT_SIZE_TEN = 10;

    private static final short FONT_SIZE_ELEVEN = 11;

    private static final short FONT_SIZE_TWELVE = 12;
    private static final short FONT_SIZE_EIGHTEEN = 18;

    /**

     * 大标题样式

     */

    private CellStyle headerStyle;

    /**

     * 每列标题样式

     */

    private CellStyle titleStyle;

    /**

     * 数据行样式

     */

    private CellStyle styles;

    public ExcelStyleUtil(Workbook workbook) {
        this.init(workbook);

    }

    /**

     * 初始化样式

     *

     * @param workbook

     */

    private void init(Workbook workbook) {
        this.headerStyle = initHeaderStyle(workbook);

        this.titleStyle = initTitleStyle(workbook);

        this.styles = initStyles(workbook);

    }

    /**

     * 大标题样式

     *

     * @param color

     * @return

     */

    @Override

    public CellStyle getHeaderStyle(short color) {
        return headerStyle;

    }

    /**

     * 每列标题样式

     *

     * @param color

     * @return

     */

    @Override

    public CellStyle getTitleStyle(short color) {
        return titleStyle;

    }

    /**

     * 数据行样式

     *

     * @param parity 可以用来表示奇偶行

     * @param entity 数据内容

     * @return 样式

     */

    @Override

    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return styles;

    }

    /**

     * 获取样式方法

     *

     * @param dataRow 数据行

     * @param obj 对象

     * @param data 数据

     */



    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);

    }

    /**

     * 模板使用的样式设置

     */

    @Override

    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;

    }

    /**

     * 初始化--大标题样式

     *

     * @param workbook

     * @return

     */

    private CellStyle initHeaderStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);

        style.setFont(getFont(workbook, FONT_SIZE_EIGHTEEN, true));

        return style;

    }

    /**

     * 初始化--每列标题样式

     *

     * @param workbook

     * @return

     */

    private CellStyle initTitleStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);

        style.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));

//背景色

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

       style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        return style;

    }

    /**

     * 初始化--数据行样式

     *

     * @param workbook

     * @return

     */

    private CellStyle initStyles(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);

        style.setFont(getFont(workbook, FONT_SIZE_TEN, false));

        style.setDataFormat(STRING_FORMAT);

        return style;

    }

    /**

     * 基础样式

     *

     * @return

     */

    private CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

//下边框

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

//左边框

        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

//上边框

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

//右边框

        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

//水平居中

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//HSSFCellStyle.ALIGN_CENTER    HorizontalAlignment.CENTER

//上下居中

        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// XSSFCellStyle.VERTICAL_CENTER

//设置自动换行

        style.setWrapText(true);

        return style;

    }

    /**

     * 字体样式

     *

     * @param size 字体大小

     * @param isBold 是否加粗

     * @return

     */

    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();

//字体样式

        font.setFontName("宋体");

//是否加粗

       // font.setBold(isBold);
        if(isBold){
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        }
//字体大小

        font.setFontHeightInPoints(size);

        return font;

    }
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                if(null!=o) {
                    map.put(field.getName(), o);
                }else{
                    map.put(field.getName(), "");
                }

                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static Map<String, Object> entityToMap(Map map,Object object) {
       // Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                if(null!=o) {
                    map.put(field.getName(), o);
                }else{
                    map.put(field.getName(), "");
                }
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Map EntityToMap(Map<String,Object> map, Object common) {
        BeanInfo beanInfo ;
        try {
            beanInfo = Introspector.getBeanInfo(common.getClass()) ;
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors() ;
            for (PropertyDescriptor pd : pds ) {
                String name = pd.getName() ;
                Method m = pd.getReadMethod() ;

                Class<?> returnType = m.getReturnType() ;
                if ("class".equals(name) || "rowId".equals(name) || "checked".equals(name))
                    continue ;
                Object o = null;
                try {
                    o = m.invoke(common);
                    if (returnType.getName().equals("java.lang.String") && null == o) {
                        o = "" ;
                    }
                    if (null == o) {
                        map.put(name, "") ;
                    } else {
                        map.put(name, o) ;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e1) {
            e1.printStackTrace();
        }

        return map;
    }

}