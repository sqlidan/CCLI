package com.haiersoft.ccli.base.web;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.common.web.BaseController;
/**
 * 用户controller
 * @author PYL 
 * @date 2016年5月24日
 */
@Controller
@RequestMapping("base/down")
public class downloadController extends BaseController {

	/*跳转列表页面*/
	@RequestMapping(value="list",method = RequestMethod.GET)
	public String downList(){
		return "base/download";
	}
	
	/**
	 *  java实现文件下载功能代码
	 *  创建时间：2014年12月23日
	 * @version
	 */
	@RequestMapping(value = "download/{state}", method = RequestMethod.POST)
	@ResponseBody
	public void fileDownLoad(HttpServletRequest request, HttpServletResponse response,@PathVariable("state") String state) throws ServletException {
			  String fileName = "";
 			  String  filePath=PropertiesUtil.getPropertiesByName("downloadexcel", "application");
// 			  String  realpath = filePath + "WEB-INF\\classes\\importExcel\\入库联系单明细导入模板.xls";
 			  String realpath = "";
			  if(state.equals("1")){
				   fileName = "Lodop6.204_CLodop.zip";
  				   realpath = filePath + "WEB-INF\\classes\\downloadPrint\\Lodop6.204_CLodop.zip";
// 				   realpath = "C:\\Lodop6.204_CLodop.zip";
			  }else{
				   fileName = "install_lodop64.exe";
				   realpath = filePath + "WEB-INF\\classes\\downloadPrint\\install_lodop64.exe";
			  }
//			  System.out.println(realpath);
			  BufferedInputStream bis = null;
			  BufferedOutputStream bos = null;
			  OutputStream fos = null;
			  InputStream fis = null;
			  try {
			   response.setContentType("application/x-download");
		       String formatFileName = new String(fileName.getBytes("GB2312"),"ISO-8859-1");
		//	   response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") +"\"");
			   response.setHeader("Content-disposition", "attachment;filename=\"" + formatFileName +"\"");
			   fis = new FileInputStream(realpath);
			   bis = new BufferedInputStream(fis);
			   fos = response.getOutputStream();
			   bos = new BufferedOutputStream(fos);
			   int bytesRead = 0;
			   byte[] buffer = new byte[5 * 1024];
			   while ((bytesRead = bis.read(buffer)) != -1) {
			    bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			   }
			   bos.close();
			   bis.close();
			   fos.close();
			   fis.close();
			  } catch (IOException e) {
			   response.reset();
			   e.printStackTrace();
			  } finally {
			   try {
			    if (fos != null) {
			     fos.close();
			    }
			    if (bos != null) {
			     bos.close();
			    }
			    if (fis != null) {
			     fis.close();
			    }
			    if (bis != null) {
			     bis.close();
			    }
			   } catch (IOException e) {
			    System.err.print(e);
			   }
			  }
			 }
}
