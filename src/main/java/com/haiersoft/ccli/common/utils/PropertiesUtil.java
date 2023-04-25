package com.haiersoft.ccli.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 配置Properties文件工具类。
 */
public class PropertiesUtil {
	/**
	 * 获取上传信息的员文件
	 * @return
	 */
	public static Properties getProperties() {
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            InputStream io = null;
            String path = cld.getResource("application.properties").getPath();
            path = path.replaceAll("%20", " ");
            io = new FileInputStream(path);
            Properties pro = new Properties();
            pro.load(io);
            return pro;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized static Properties getProperties(String propertyFileName) throws Exception {
        //读取配置文件信息
        try {
            Properties properties = new Properties();
            String url = Thread.currentThread().getContextClassLoader().getResource("") + propertyFileName + ".properties";
            url = url.replace("file:/", "");
            if (url.indexOf(":") == 1) {

            } else {
                url = "/" + url;
            }

            InputStream inStream = new FileInputStream(url);
            properties.load(inStream);
            inStream.close();

            return properties;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 取出配置文件中字段的值
     *
     * @param keyName          字段名
     * @param propertyFileName 配置文件名
     * @return
     */
    public synchronized static String getPropertiesByName(String keyName, String propertyFileName) {
        try {
            String a = getProperties(propertyFileName).getProperty(keyName);
            if (a == null) {
                return "";
            } else {
                return a;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 取出配置文件中字段的值
     *
     * @param keyName          字段名
     * @param propertyFileName 配置文件名
     * @return
     */
    public synchronized static Integer getPropertiesByNameIngeger(String keyName, String propertyFileName) {
        try {
            String a = getProperties(propertyFileName).getProperty(keyName);
            if (a == null) {
                return 0;
            } else {
                return Integer.valueOf(a);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 设置配置文件中字段的值
     *
     * @param name          字段名
     * @param value         字段值
     * @param propertieName 配置文件名
     * @return
     */
    public synchronized static boolean setPropertiesByName(String name, String value, String propertieName) throws Exception {
        try {
            Properties properties = new Properties();//getProperties(propertieName)
            String url = Thread.currentThread().getContextClassLoader().getResource("") + propertieName + ".properties";
            url = url.replace("file:/", "");
            if (url.indexOf(":") == 1) {

            } else {
                url = "/" + url;
            }
            File file = new File(url);
            if (file.isFile()) {
                properties.load(new FileInputStream(url));
            }

            OutputStream ops = new FileOutputStream(url);
            properties.setProperty(name, value);
            properties.store(ops, "");
            ops.flush();
            ops.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static boolean setPropertiesByName(String name[], String value[], String propertieName) throws Exception {
        try {
            Properties properties = new Properties();
            String url = Thread.currentThread().getContextClassLoader().getResource("") + propertieName + ".properties";
            url = url.replace("file:/", "");
            if (url.indexOf(":") == 1) {

            } else {
                url = "/" + url;
            }
            properties.load(new FileInputStream(url));
            OutputStream ops = new FileOutputStream(url);
            for (int i = 0; i < value.length; i++) {
                if (name[i] != null) {
                    properties.setProperty(name[i], value[i]);
                }
            }
            properties.store(ops, "");
            ops.flush();
            ops.close();
            properties = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
