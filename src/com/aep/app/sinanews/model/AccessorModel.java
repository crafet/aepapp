/**
 * 
 */
package com.aep.app.sinanews.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Administrator
 *
 */
public class AccessorModel 
{
	private static final int LENGTH=36;
	private static final String charSets="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String appSecret = "79c242600fdf0e6c";
	private static final String appKey = "9e45190b41bf4b55b534ba4049b3bd39";
	
	
	public String getNonce()
	{
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		for (int i=0; i<LENGTH; i++)
		{
			int idx = rand.nextInt(charSets.length());
			sb.append(charSets.charAt(idx));
		}
		return sb.toString();
	}
	
	
	public String getCreatedTime()
	{
		SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		dateForm.setTimeZone(timeZone);
		String dateStr = dateForm.format(new Date());
		return dateStr;
	}
	/*
	public String getCreatedTime()
	{
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		//Local time zone   
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

		//Time in GMT
		try {
			System.out.println(dateFormatLocal.parse( dateFormatGmt.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	public String getPasswordDigest(String nonce, String createdTime)
	{
		String passwordDigest = null;
		
		String combinedStr = nonce + createdTime + appSecret;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		md.update(combinedStr.getBytes());
		byte[] aggregateBytes = md.digest();
		passwordDigest = new String(Base64.encodeBase64(aggregateBytes));
		//passwordDigest = new String(java.util.Base64.getEncoder().encode(aggregateBytes));
		return passwordDigest;
	}
	
	public HashMap<String, String> getAuthHeader(String giveNonce, String giveCreatedTime)
	{	
		//giveNonce = "9sgs7xE0kMvkfymLOfNI8zmkmuioXxkGJulB";
		//giveCreatedTime = "2014-10-12T18:38:49Z";
		HashMap<String, String> m = new HashMap<String, String>();
		
		String authPrototype = "WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"Appkey\"";
		m.put("Authorization", authPrototype);
		
		String xWssePrototype = "UsernameToken Username=\"%s\", PasswordDigest=\"%s\", Nonce=\"%s\", Created=\"%s\"";
		String xWsseValue = String.format(xWssePrototype, 
				appKey, this.getPasswordDigest(giveNonce, giveCreatedTime),
				//this.getNonce(), this.getCreatedTime());
				giveNonce, giveCreatedTime);
		m.put("X-WSSE", xWsseValue);
		
		System.out.println("-----------------");
		System.out.println("Authorization:" + m.get("Authorization"));
		System.out.println("X-WSSE:" + m.get("X-WSSE"));
		System.out.println("-----------------");
		
		return m;
	}
	
	public static void main(String [] args)
	{
		AccessorModel accessorModel = new AccessorModel();
		String nonce = accessorModel.getNonce();
		//String nonce = "RVlQSXFYUGp6NnRoY0c1dFBsaGQ0YUgzRA==";
		String createdTime = accessorModel.getCreatedTime();
		//String createdTime = "2014-10-10T16:10:37Z";
		String passwordDigest = accessorModel.getPasswordDigest(nonce, createdTime);
		
		System.out.println("--------------------------");
		System.out.println("nonce: " + nonce + " " + nonce.length());
		System.out.println("createdTime: " + createdTime);
		System.out.println("passwordDigest: " + passwordDigest);
		
		accessorModel.getAuthHeader(nonce, createdTime);
		System.out.println("--------------------------");
	}
}
