package com.haiersoft.ccli.common.utils;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

public class PdfSealUtils {
    public static final float DEFAULT_SEAL_WIDTH = 80f;
    public static final float DEFAULT_SEAL_HEIGHT = 80f;

    /**
     * 在PDF指定位置添加电子章
     * @param sourcePdf 源PDF路径
     * @param targetPdf 目标PDF路径
     * @param sealImagePath 印章图片路径
     * @param pageNumber 页码（从1开始）
     * @param x x坐标
     * @param y y坐标
     * @return 是否成功
     */
    public static boolean addSealToPdf(String sourcePdf, String targetPdf,
                                       String sealImagePath, int pageNumber,
                                       float x, float y) {
        PdfReader reader = null;
        PdfStamper stamper = null;

        try {
            reader = new PdfReader(sourcePdf);
            stamper = new PdfStamper(reader, new FileOutputStream(targetPdf));

            // 验证页码有效性
            if (pageNumber < 1 || pageNumber > reader.getNumberOfPages()) {
                throw new IllegalArgumentException("无效的页码: " + pageNumber);
            }

            // 加载印章图片
            Image seal = Image.getInstance(sealImagePath);
            seal.scaleToFit(DEFAULT_SEAL_WIDTH, DEFAULT_SEAL_HEIGHT);
            seal.setAbsolutePosition(x, y);

            for (int i = 1; i <= 20; i++) {
                // 获取指定页面的内容
                PdfContentByte canvas = stamper.getOverContent(i);
                canvas.addImage(seal);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

}
