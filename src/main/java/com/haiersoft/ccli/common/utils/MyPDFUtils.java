package com.haiersoft.ccli.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.haiersoft.ccli.common.utils.PdfReportM1HeaderFooter;

public class MyPDFUtils {

    public Font fontDetail = null;

    public static String sHTML = "";
    public static String sDEST = "";
    public static String sDESTTemp = "";
    public static String sImage = "";
    public static Document document = null;
    public static PdfWriter writer = null;

    public static void setsHTML(String sHTML) {
        MyPDFUtils.sHTML = sHTML;
    }

    public static void setsDEST(String sDEST) {
        MyPDFUtils.sDEST = sDEST;
    }
    public static void setsDESTTemp(String sDESTTemp) {
        MyPDFUtils.sDESTTemp = sDESTTemp;
    }
    public static void setsImage(String sImage) {
        MyPDFUtils.sImage = sImage;
    }

    public static void createPdf(Rectangle pageSize, String pdfHead, String pdfTitle) {

        document = new Document(pageSize.rotate(), 5, 5, 80, 30);

        try {

            writer = PdfWriter.getInstance(document, new FileOutputStream(sDEST));

            setFooter(writer, pdfHead, pdfTitle);

            writer.setFullCompression();
            writer.setPdfVersion(PdfWriter.VERSION_1_4);

            document.open();

            //TODO 即墨冷库使用，黄岛仓库未使用 2024-01-16 徐峥
//            BaseFont baseFont = BaseFont.createFont("c://windows//fonts//simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            Font font = new Font(baseFont, 4, Font.NORMAL);
//            Paragraph paragraph = new Paragraph(sHTML, font);
//            document.add(paragraph);

            //	setHeader(writer,pdfHead,pdfTitle) ;
            // 打开文档，将要写入内容
/*
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,  new FileInputStream(sHTML), Charset.forName("GB2312"));
*/
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(sHTML), Charset.forName("UTF-8"));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                }
            }
            if (writer.isCloseStream() == false) {
                writer.close();
            }

        }

    }

    public static void createPdf2(Rectangle pageSize, String pdfHead, String pdfTitle) {
        document = new Document(pageSize.rotate(), 5, 5, 80, 30);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(sDESTTemp));
            setFooter(writer, pdfHead, pdfTitle);
            writer.setFullCompression();
            writer.setPdfVersion(PdfWriter.VERSION_1_4);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(sHTML), Charset.forName("UTF-8"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                }
            }
            if (writer.isCloseStream() == false) {
                writer.close();
            }
        }

        float x = pageSize.getWidth() - 80 - 20;
        float y = 20;
        PdfSealUtils.addSealToPdf(sDESTTemp, sDEST,sImage,1,x,y);
    }

    private static void setFooter(PdfWriter writer, String pdfHead, String pdfTitle) throws DocumentException, IOException {
        // HeaderFooter headerFooter = new HeaderFooter(this);
        // 更改事件，瞬间变身 第几页/共几页 模式。
        PdfReportM1HeaderFooter headerFooter = new PdfReportM1HeaderFooter();// 就是上面那个类
        headerFooter.setHeader(pdfHead);
        headerFooter.setHeadert(pdfTitle);
        headerFooter.setPresentFontSize(12);
        writer.setBoxSize("art", PageSize.A3);
        writer.setPageEvent(headerFooter);
    }

}
