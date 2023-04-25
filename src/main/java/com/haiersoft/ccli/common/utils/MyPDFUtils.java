package com.haiersoft.ccli.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.haiersoft.ccli.common.utils.PdfReportM1HeaderFooter;

public class MyPDFUtils {

    public Font fontDetail = null;

    public static String sHTML = "";
    public static String sDEST = "";
    public static Document document = null;
    public static PdfWriter writer = null;

    public static void setsHTML(String sHTML) {
        MyPDFUtils.sHTML = sHTML;
    }

    public static void setsDEST(String sDEST) {
        MyPDFUtils.sDEST = sDEST;
    }

    public static void createPdf(Rectangle pageSize, String pdfHead, String pdfTitle) {

        document = new Document(pageSize.rotate(), 5, 5, 80, 30);

        try {

            writer = PdfWriter.getInstance(document, new FileOutputStream(sDEST));

            setFooter(writer, pdfHead, pdfTitle);

            writer.setFullCompression();
            writer.setPdfVersion(PdfWriter.VERSION_1_4);

            document.open();

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
