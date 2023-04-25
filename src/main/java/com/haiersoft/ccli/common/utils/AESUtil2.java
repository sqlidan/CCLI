package com.haiersoft.ccli.common.utils;

/**
 * Author mac
 * Date 2020-03-20
 **/

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version V1.0
 * @desc AES 加密工具类
 */
public class AESUtil2 {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    //自定义密码
    private static final String ASSETS_DEV_PWD_FIELD = "LH@2022";

    public static String getAssetsDevPwdField() {
        return ASSETS_DEV_PWD_FIELD;
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64Utils.encodeToString(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(AESUtil2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content) {
        return encrypt(content,ASSETS_DEV_PWD_FIELD);
    }
    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            String s = new String(result, "utf-8");
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        return decrypt(content,ASSETS_DEV_PWD_FIELD);
    }
    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtil2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*
    * 对费用编号加工
    * */
    /**

     * 获得4个长度的十六进制的UUID

     * @return UUID

     */public static String get4UUID(String password){
         UUID id=UUID.randomUUID();
         String arr = "LL_";
         String[] idd=id.toString().split("-");
         arr+=idd[1];
         arr+=password;
         return arr;
    }


    public static void main(String[] args) {
        //String origin = "{\"userid\":\"LHT\",\"pswd\":\"LH@2020\",\"newtime\":\"2020-08-03 08:02:22\",\"starCode\":\"3702\",\"endCode\":\"3704\",\"starType\":\"0\",\"endType\":\"0\"}";
      // String origin = "{\"userid\":\"LHT\",\"pswd\":\"LH@2020\",\"newtime\":\"2020-09-30 15:03:40\",\"sequenceOfficial\":\"X9006\"}";
     String origin="{\"userid\": \"YZH\",\n" +
             "\t\"pswd\": \"bx7bz2dBpK5wUy85xTuaNA==\",\n" +
             "\t\"newtime\": \"2022-04-27 14:05:22\",\n" +
             "\t\"direction\": \"exitCard\",\n" +
             "\t\"data\": {\n" +
             "\t\t\"licensePlateNumber\": \"鲁B67666\",\n" +
             "\t\t\"cargoCode\": \"01\",\n" +
             "\t\t\"cargoName\": \"集装箱\",\n" +
             "\t\t\"casesNumber\": \"123\",\n" +
             "\t\t\"companyCode\": \"YZH\",\n" +
             "\t\t\"companyName\": \"黄岛港站\",\n" +
             "\t\t\"weight\": \"8987\",\n" +
             "\t\t\"workCompleteTime\": \"2022-04-27 14:05:22\",\n" +
             "\t\t\"remark\": \"测试\",\n" +
             "\t\t\"shipname\": \"船名\",\n" +
             "\t\t\"shipper\": \"CMA\",\n" +
             "\t\t\"cartonNo\": \"箱号1\"\n" +
             "\t}\n" +
             "}";


      //String origin1 = "quz7iV97xe/e9d0Qha2rOGmD1FSVdE3uRgJ9yzQckBydjtoTJbcq45t5/DDUp/BEfb7hFvmSj6Y0iRh42G7wlcDlpC4U0JgZQzzhdD6Hfkp1ejSfqvoSwC4u3cB/yYSW";
    String origin1 = "+U/y71pfVFBOhyeq1a8OLmwEOXGRgH6aiy7WhBPTnzio0yUSo3TndNKr75cKm0+i1M51LVX+2Fp34+jeQNymNjvIQbOeOcJUI8wM9i5TuM77z/5X64B5hjEgx1AeILC0ofwyAPZFNY9EyDT0cctRnfZK535lF3arNWs16pNAGs6ZKuGp+H52ebh4yV65kOJL8c5nqW9TKAa7CoidZF51lvWBf6m1j9NHT7fWMu+wxll6zOO3iH0M0wbfccUFZkiIx73xpJok1kCHDVXqx/Vvt5Mp5F/fgNGIepAqAp2eqSNOKVLAM0Y5xiW2mMYFqPOqpQ2BWSjBfyo/xJi9fYV2XUsUkyvi/YcRSjVqz3cLbo35UeFg1JthDuRzvjW20SkhIBw4IRbIxJTZmBH/L9jjeawNjeP9k1sKLN5Cq+LroO0zRNsk3sHtp2Nh0rJOopBVDA4hpbgxU1pVcFQdFCirZb4rYLrGjZuTGSKR8Z7fn2qrcNtiG/Tm0Ly5NSNRUoCgL6xk2by9WanowcPgXszXwpN/8BqmVbIN4M2BN3IN+p4ym7X3K87G4ULdj+fY123sNMqJ5EByWmGnwSxhjnSArg==";

      // String test="evbXFASZ4qSXZqs6876W6EHaSa8+lQqii7aajsZ677lAk8yhCMgZoyP9aKioNQUHi79lFBKKRI3uT1OTW1MaLsgIuYV0jRXcqK4Ne8M0T7d19VaI/DpoYwoq/tdQXWHu0M6r46JpqmC19a03ckCqKX3EAML7jcVxZrf6+/weTPRRBQm9q3LE2h84IboYWKHzluIRTaKzq/OPzvUlZDo9pTjpLGqkzmD96EinZu6SLfnCjlOB4XC35cIYD86fOcOBff18aEm4FSnLxYxMs48ubejOFPcr19e0pHIIzXVe2vQgWYOVBUyqmhpDKkeFeUhvXi0Hn24GCRc7T1/BXcnsnZ00Fk05UbduS5Dxcz3torpxZpvujCxZlH2XFmsY3Hy9gCpRXWw3KRdN/ymHZ+gAvkCblTKZKWN2o7bCFitqXRdOmtWiwTaasy1nihfCfzcrlpLqoe6kBLUfTJ7HWmc8wMBxka9MI64Mg+d3SDQ11So=";
      //  String testt = AESUtil2.decrypt(test, AESUtil2.ASSETS_DEV_PWD_FIELD);

      String encrypt = AESUtil2.encrypt(origin, AESUtil2.ASSETS_DEV_PWD_FIELD);
      String decrypt = AESUtil2.decrypt(origin1, AESUtil2.ASSETS_DEV_PWD_FIELD);
        //System.out.println(origin);
        System.out.println(encrypt);
       System.out.println(decrypt);
       // System.out.println(testt);
    }

}