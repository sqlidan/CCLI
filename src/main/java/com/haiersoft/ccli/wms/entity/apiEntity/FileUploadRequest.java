package com.haiersoft.ccli.wms.entity.apiEntity;
import java.util.List;

/**
 * @ClassName: FileUploadRequest
 * @Description: TODO
 * @Author chenp
 * @Date 2021/3/13 11:19
 *
 */
//@Data
public class FileUploadRequest {

//    @ApiModelProperty("随附单证列表")
    private List<FileMessage> files;
//    @ApiModelProperty(value = "电子口岸卡密码")
    private String pass;
//    @ApiModelProperty(value = "关务平台租户编码")
    private String memberCode;
//    @ApiModelProperty(value = "海关卡卡号")
    private String icCode;
//    @ApiModelProperty(value = "api中台秘钥")
    private String key;

    public List<FileMessage> getFiles() {
        return files;
    }

    public void setFiles(List<FileMessage> files) {
        this.files = files;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getIcCode() {
        return icCode;
    }

    public void setIcCode(String icCode) {
        this.icCode = icCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}