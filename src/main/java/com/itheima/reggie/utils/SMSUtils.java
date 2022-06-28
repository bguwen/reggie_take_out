package com.itheima.reggie.utils;

//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信发送工具类
 */
public class SMSUtils {
//	@Autowired
//	private  JavaMailSender javaMailSender;

//	@Value("${spring.mail.username}")
//	private  String fromEmail;


	/**
	 * 邮件发送
	 * @param toEmail 收件人邮箱
	 * @param code 验证码
	 * @param javaMailSender javaMailSender
	 * @param fromEmail 发件人邮箱
	 * @return boolean
	 */
	public static boolean sendEmail(String toEmail, String code,JavaMailSender javaMailSender,String fromEmail) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		//设置发件邮箱
		simpleMailMessage.setFrom(fromEmail);
		//收件人邮箱
		simpleMailMessage.setTo(toEmail);
		//主题标题
		String text="瑞吉外卖登录验证码：\n";
		simpleMailMessage.setSubject(text);
		//信息内容
		simpleMailMessage.setText("您的瑞吉外卖登录验证码为："+code+"\n该验证码5分钟内有效！");
		//执行发送
		try {//发送可能失败
			javaMailSender.send(simpleMailMessage);
			//没有异常返回true，表示发送成功
			return true;
		} catch (Exception e) {
			//发送失败，返回false
			return false;
		}
	}

//	/**
//	 * 发送短信
//	 * @param signName 签名
//	 * @param templateCode 模板
//	 * @param phoneNumbers 手机号
//	 * @param param 参数
//	 */
//	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
//		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
//		IAcsClient client = new DefaultAcsClient(profile);
//
//		SendSmsRequest request = new SendSmsRequest();
//		request.setSysRegionId("cn-hangzhou");
//		request.setPhoneNumbers(phoneNumbers);
//		request.setSignName(signName);
//		request.setTemplateCode(templateCode);
//		request.setTemplateParam("{\"code\":\""+param+"\"}");
//		try {
//			SendSmsResponse response = client.getAcsResponse(request);
//			System.out.println("短信发送成功");
//		}catch (ClientException e) {
//			e.printStackTrace();
//		}
//	}

}
