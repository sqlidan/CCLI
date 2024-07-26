package com.haiersoft.ccli.wms.web.passPort;

import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.supervision.web.FTPUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;


public class PassPortFTPUtils {

    private static final Logger logger= LoggerFactory.getLogger(FTPUtils.class);

    private static PassPortFTPUtils ftpUtils;
    private FTPClient ftpClient;
    private String url; //服务器地址
    private String port; // 服务器端口
    private String username; // 用户登录名
    private String password; // 用户登录密码
    private String rootDirectory; //文件存放根目录
    private InputStream is; // 文件下载输入流

    /**
     * 私有构造方法
     */
    private PassPortFTPUtils() {
        try {
            initConfig();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (null == ftpClient) {
            ftpClient = new FTPClient();
        }
    }

    /**
     * 初始化FTP服务器连接属性
     */
    public void initConfig() throws IOException {
        // 定义配置文件输入流
        InputStream is = null;
        try {
            // 读取配置文件
//            url ="10.135.252.42"; // 设置服务器地址
//            port ="21"; // 设置端口
//            username ="yzh"; // 设置用户名
////            password ="yzh"; // 设置密码
//            password ="Eimskip0804"; // 设置密码
//            rootDirectory ="/Send/"; //设置文件存放根目录
            // 读取配置文件
            url ="localhost"; // 设置服务器地址
            port ="21"; // 设置端口
            username ="administrator"; // 设置用户名
            password ="XZ"; // 设置密码
            rootDirectory ="/Send/"; //设置文件存放根目录
        } finally {
            // 判断输入流是否为空
            if (null != is) {
                try {
                    // 关闭输入流
                    is.close();
                } catch (IOException e) {
                    logger.error("加载配置文件ftp.properties异常!"+e.getMessage());
                    throw new RuntimeException("FTP初始化失败，关闭输入流异常" , e);
                }
            }
        }
    }

    //实例化对象
    public synchronized static PassPortFTPUtils getInstance() {
        if (null == ftpUtils) {
            ftpUtils = new PassPortFTPUtils();
        }
        return ftpUtils;
    }

    /**
     * 下载FTP服务器文件至本地
     * 操作完成后需调用logout方法与服务器断开连接
     * @param remotePath 下载文件存储路径
     * @param fileName 下载文件存储名称
     * @return InputStream：文件输入流
     */
    public InputStream retrieveFile(String remotePath,String fileName) {
        try {
            boolean result = false;
            // 连接至服务器
            result = connectToTheServer(remotePath);
            // 判断服务器是否连接成功
            if (result) {
                // 获取文件输入流
                is = ftpClient.retrieveFileStream(fileName);
            }
        } catch (IOException e) {
            logger.error("从FTP下载文件到本地异常"+e.getMessage());
            throw new RuntimeException("从FTP下载文件到本地异常" , e);
        }
        return is;
    }

    /**
     * 连接（配置通用连接属性）至服务器
     * @param remotePath 当前访问目录
     * @return  true ：连接成功 false ：连接失败
     */
    public boolean connectToTheServer(String remotePath) {
        // 定义返回值
        boolean result = false;
        try {
            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftpClient.connect(url, Integer.parseInt(port));
            // 登录服务器
            ftpClient.login(username, password);
            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                // 不合法时断开连接
                ftpClient.disconnect();
                // 结束程序
                return result;
            }
            // 设置文件操作目录
            result = ftpClient.changeWorkingDirectory(remotePath);
            // 设置文件类型，二进制
            result = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftpClient.setBufferSize(3072);
            // 设置字符编码
            ftpClient.setControlEncoding("UTF-8");
        } catch (IOException e) {
            logger.error("连接FTP服务器异常"+e.getMessage());
            throw new RuntimeException("连接FTP服务器异常" , e);
        }
        return result;
    }

    /**
     * 登出服务器并断开连接
     * @return true 操作成功  false 操作失败
     */
    public boolean logout() {
        boolean result = false;
        if (null != is) {
            try {
                // 关闭输入流
                is.close();
            } catch (IOException e) {
                logger.error("登录FTP服务器异常"+e.getMessage());
                throw new RuntimeException("登录FTP服务器异常" , e);
            }
        }
        if (null != ftpClient) {
            try {
                // 登出服务器
                result = ftpClient.logout();
            } catch (IOException e) {
                logger.error("登录FTP服务器异常"+e.getMessage());
                throw new RuntimeException("登录FTP服务器异常" , e);
            } finally {
                // 判断连接是否存在
                if (ftpClient.isConnected()) {
                    try {
                        // 断开连接
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        logger.error("关闭FTP服务器异常"+e.getMessage());
                        throw new RuntimeException("关闭FTP服务器异常" , e);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取目录下所有的文件名称
     * @param remotePath 指定的目录
     * @return 文件列表,或者null
     */
    public FTPFile[] getFilesList(String remotePath ) {
        // 初始表示下载失败
        FTPFile[] fs = null;
        try {
            int reply;
            // 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.connect(url,Integer.parseInt(port));
            //登陆ftp
            ftpClient.login(username, password);
            reply = ftpClient.getReplyCode();
            /*
             * 判断是否连接成功
             */
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return fs;
            }
            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            // 列出该目录下所有文件
            fs = ftpClient.listFiles();
            Arrays.sort(fs, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File file1 = (File)o1;
                    File file2 = (File)o2;
                    if (file1.lastModified() > file2.lastModified()) { return 1; }
                    else if (file1.lastModified() < file2.lastModified()) { return -1; }
                    else { return 0; }
                }
            });
            return fs;
        } catch (IOException e) {
            logger.error("从FTP服务器下载文件异常"+e.getMessage());
            throw new RuntimeException("从FTP服务器下载文件异常" , e);
        } finally {
            // 登出服务器并断开连接
            ftpUtils.logout();
            return fs;
        }
    }

    /**
     * 下载文件
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 文件名称
     * @param localPath FTP服务器上的相对路径
     */
    public boolean downFile(String remotePath, String fileName, String localPath) throws IOException {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("utf-8");
        try {
            int reply;
            // 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(url,Integer.parseInt(port));
            //登陆ftp
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            /*
             * 判断是否连接成功
             */
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            } else {
                ftp.changeWorkingDirectory(remotePath);//修改操作空间
                FTPFile[] fs = ftp.listFiles(remotePath);
                // 遍历所有文件，找到指定的文件
                for (FTPFile ff : fs) {
                    if (ff.getName().equals(fileName)) {
                        InputStream in = ftp.retrieveFileStream(fileName);
                        //创建文件路径
                        File descFiledir = new File(localPath);
                        if (!descFiledir.exists()){
                            Boolean resultTmp = descFiledir.mkdirs();
                        }
                        //创建空白文件
                        File descFile = new File(descFiledir+"\\"+fileName);
                        // 判断文件是否存在
                        if (!descFile.exists()) {
                            Boolean resultTmp = descFile.createNewFile();
                        }
                        FileUtils.copyInputStreamToFile(in,descFile);
                        result = true;

                        //暂时不删除
//                        ftp.deleteFile(fileName);//删除FTP原文件
                    }
                }
            }
        } catch (IOException e) {
            logger.error("异常,{}", e.getMessage());
            return false;
        } finally {
            ftp.logout();
            return result;
        }
    }

    /**
     * 上传文件
     * @param localPath FTP服务器上的相对路径
     * @param fileName 文件名称
     * @param targetPath FTP服务器上的相对路径
     */
    public boolean upFile(String localPath,String fileName,String targetPath) throws IOException {
        boolean result = false;
        StringBuilder sBuilder = new StringBuilder();
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("utf-8");
        try {
            int reply;
            // 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(url,Integer.parseInt(port));
            //登陆ftp
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            /*
             * 判断是否连接成功
             */
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            } else {
                boolean pathIsExis = ftp.changeWorkingDirectory(targetPath);
                if (!pathIsExis) {//不存在文件夹
                    String[] pah = targetPath.split("/");
                    // 先创建目录，在转到当前目录，再保存文件
                    // 分层创建目录
                    for (String pa : pah) {
                        if (StringUtils.isNotBlank(pa)) {
                            // 每创建一层文件夹，在对应的
                            sBuilder.append("/").append(pa);
                            ftp.makeDirectory(sBuilder.toString());
                            // 切到到对应目录
                            ftp.changeWorkingDirectory(sBuilder.toString());
                        }
                    }
                }
                File fileOrg = new File(localPath);
                File[] fileList = fileOrg.listFiles();
                for (File file : fileList) {
                    if (file.getName().equals(fileName)) {
                        InputStream in = new FileInputStream(file);
                        ftp.changeWorkingDirectory(targetPath);//修改操作空间
                        // 设置文件类型，二进制
                        result = ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        ftp.enterLocalPassiveMode();
                        result = ftp.storeFile(fileName,in);
                        in.close();

                        //暂时不删除
//                        file.delete();//删除本地文件
                    }
                }
            }
        } catch (IOException e) {
            logger.error("异常,{}", e.getMessage());
            return false;
        } finally {
            ftp.logout();
            return result;
        }
    }
}