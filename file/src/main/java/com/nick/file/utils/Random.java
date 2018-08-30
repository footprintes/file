/**
 * 
 */
package com.nick.file.utils;

import java.security.SecureRandom;
import java.util.UUID;


/**
 * @version V1.0
 * @ClassName：Random
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public class Random {

	public static Long utcTimeRandom(){
		long time = DateUtil.getTimeOfEastEight();
		int max = 99999;
		int min = 10000;
		
		long s = (long)(min + Math.random()*max);
		time = time + s;
		return time;
	}
	public static int getLimitedRandom(int Max,int Min){
		int random = Min + (int)(Math.random() * ((Max - Min) + 1));
		return random;
	}
	
	public static int getRandom(int length){
		length = (int) Math.pow(10, length);
		int random = (int) ((Math.random() * 9 + 1) * length) ;
		return random;
	}
	/**
	 * 
	* <b>Description:</b>随机数生成算法<br> 
	* @return
	* @Note
	* <b>Author:maamin
	* <br><b>Date:</b> 2018年3月16日 下午4:39:50
	* <br><b>Version:</b> 1.0
	 */
	public static int getSecureRandom(){
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextInt();
	}
	
	
	public static long getSecureRandomLong(){
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextLong();
	}

	/**
	 *
	 * @Title: 生成uuid
	 * @Description: 1,
	 * @param: []
	 *            description
	 * @return: java.lang.String
	 * @auther: hbj
	 * @date: 2018/7/17 20:40
	 */
	public static String getUUid(){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
}
