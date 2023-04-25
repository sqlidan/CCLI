package com.haiersoft.ccli.common.web;

import java.beans.PropertyEditorSupport;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haiersoft.ccli.common.entity.ResultBean;
import com.haiersoft.ccli.common.entity.ResultBeanFactory;
import com.haiersoft.ccli.common.utils.parameterReflect;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.system.entity.User;


/**
 * 基础控制器
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 *
 * @author ty
 * @description
 * @date 2014年3月19日
 */
public class BaseController {

    protected static final String SUCCESS = "success";

    protected static final String FAILED = "failed";

    protected static final String ERROR = "error";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });

        // Timestamp 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    /**
     * 获取page对象
     *
     * @param request
     * @return page对象
     */
    public <T> Page<T> getPage(HttpServletRequest request) {
        int pageNo = 1;    //当前页码
        int pageSize = 20;    //每页行数
        String orderBy = "id";    //排序字段
        String order = "asc";    //排序顺序
        if (StringUtils.isNotEmpty(request.getParameter("page")))
            pageNo = Integer.valueOf(request.getParameter("page"));
        if (StringUtils.isNotEmpty(request.getParameter("rows")))
            pageSize = Integer.valueOf(request.getParameter("rows"));
        if (StringUtils.isNotEmpty(request.getParameter("sort")))
            orderBy = request.getParameter("sort").toString();
        if (StringUtils.isNotEmpty(request.getParameter("order")))
            order = request.getParameter("order").toString();
        return new Page<T>(pageNo, pageSize, orderBy, order);
    }

    /**
     * 获取easyui分页数据
     *
     * @param page
     * @return map对象
     */
    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", page.getResult());
        map.put("total", page.getTotalCount());
        return map;
    }

    /**
     * 用来获取当前登录用户
     *
     * @return 当前登录用户
     */
    public User getCurUser() {
        User curUser = (User) SecurityUtils.getSubject().getPrincipal();
        return curUser;
    }

    /**
     * 字符串是否为空
     *
     * @return true 字符串为空；false 字符串不为空
     */
    protected static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否不为空
     *
     * @return true 字符串不为空；false 字符串为空
     */
    protected static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * HttpServletRequest参数转换到实体类
     */
    protected static <T> T reflectParameter(HttpServletRequest request, Class<T> target) {
        return parameterReflect.reflectParameter(request, target);
    }

    /**
     * 下载已经生成的Excel表格
     *
     * @param response 响应
     * @param desPath  生成后excel绝对路径
     * @param fileName 下载后文件名
     */
    protected void transportExcel(HttpServletResponse response, String desPath, String fileName) throws IOException {

        //设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        //定义输出类型
        response.setContentType("application/msexcel");

        FileInputStream in = new FileInputStream(new File(desPath));

        OutputStream out = response.getOutputStream();

        byte[] buffer = new byte[1024];

        int len = 0;

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        if (null != in) {
            in.close();
        }

        if (null != out) {
            out.close();
        }

    }

    protected <T> ResultBean<T> getResult() {
        return ResultBeanFactory.generate();
    }

}
