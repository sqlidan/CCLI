package com.haiersoft.ccli.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: youqiang.wang
 * @date: 2012-7-5
 * Time: 15:46:41
 */
public class ResponseJsonWriter {
    /**
     * 将json数据写入response
     * @param content
     * @param response
     * @throws IOException
     */
    public static void write(String content, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
        out.close();
    }
}
