package com.haiersoft.ccli.api.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

public class SHA256withRSA {

	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	//private static final String ENCODE_ALGORITHM = "SHA-256";
//
//	private static String PUBLIC_KEY = "/pledge_rsa_public.key";
//	private static String PRIVATE_KEY = "/pkcs8_pledge_rsa_private.key";
//	private static String path = "D:\\";
	private static String PUBLIC_KEY ;
	private static String PRIVATE_KEY ;
	private static String path ;

	static {
		Properties properties = new Properties();
        InputStream is = SHA256withRSA.class.getClassLoader().getResourceAsStream("apipledge.properties");
		try {
			properties.load(new InputStreamReader(is,"utf-8"));
			PUBLIC_KEY = properties.getProperty("public_key_file_name");
			PRIVATE_KEY = properties.getProperty("private_key_file_name");
			path =System.getProperty("user.dir")+"\\src\\main\\resources"+ properties.getProperty("keyPath");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/**
	 * 验签
	 * 
	 * 
	 * @param plainTextData 明文
	 * @param signatureText 签名
	 * @throws Exception 
	 * @throws InvalidKeyException 
	 */
	public static boolean verifySign(String plainTextData, String signatureText) throws InvalidKeyException, Exception {

		//byte[] plainText = Base64.decodeBase64(plainTextData);
		Signature signature;

		signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(loadPublicKeyByStr(loadPublicKeyByFile()));
		signature.update(plainTextData.getBytes());

		return signature.verify(Base64.decodeBase64(signatureText));

	}

	/**
	 * 签名
	 * 
	 * @param privateKey
	 *            私钥
	 * @param plain_text
	 *            明文
	 * @return
	 */
	public static String sign(String plainText) {
		PrivateKey privateKey;
		byte[] signed = null;
			Signature Sign;
			try {
				privateKey = loadPrivateKeyByStr(loadPrivateKeyByFile());
				Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
				Sign.initSign(privateKey);
				Sign.update(Base64.decodeBase64(plainText));
				signed = Sign.sign();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return Base64.encodeBase64String(signed);
	}
	
	/**
	 * 从文件中输入流中加载公钥
	 *
	 * @param path 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	private static String loadPublicKeyByFile() throws Exception {

		try {
			BufferedReader br = new BufferedReader(new FileReader(path + PUBLIC_KEY));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new IOException("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new NullPointerException("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");			
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new InvalidKeySpecException("公钥非法");
		} catch (NullPointerException e) {
			throw new NullPointerException("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 *
	 * @param path 私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	private static String loadPrivateKeyByFile() throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + PRIVATE_KEY));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}
	

	private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecException("私钥非法");
        } catch (NullPointerException e) {
            throw new NullPointerException("私钥数据为空");
        }
    }

}
