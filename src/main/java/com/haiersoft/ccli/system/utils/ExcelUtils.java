/**
 * 
 */
package com.haiersoft.ccli.system.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
/**
 * @author Administrator
 *
 */
public class ExcelUtils {
	private static Configuration configuration =null;
    public ExcelUtils(){
        throw new AssertionError();
    }
    /**
     * 创建excel
     * @param dataMap
     * @param type
     * @return
     */
	public static File createExcel(String path,Map<?, ?> dataMap, String type, String valueName,long nowTime) {
		File file=null;
		try {
			configuration = new Configuration();                         
			configuration.setDirectoryForTemplateLoading(new File(path+"/exceltemplate/freemarker"));
			configuration.setObjectWrapper(new DefaultObjectWrapper());
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		    Template template =configuration.getTemplate(valueName+".ftl","UTF-8");
		    Writer out = new OutputStreamWriter(new FileOutputStream(path+ "/exceltemplate/freemarker/"+nowTime+valueName+".xls"), "UTF-8");
		    template.process(dataMap, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        file = new File(path+ "/exceltemplate/freemarker/"+nowTime+valueName+".xls");
		return file;
	}
}
