package com.hngc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hngc.common.R;
import com.hngc.entity.User;
import com.hngc.service.UserService;
import com.hngc.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
//    /**
//     * 邮箱验证码
//     *
//     * @param user    user
//     * @param session session
//     * @return R
//     */
//@PostMapping("/sendMsg")
//    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
//
////        获取手机号
//        String phone = user.getPhone();
//        if (StringUtils.isNotEmpty(phone)) {
//            //        生成验证码
//            String code = ValidateCodeUtils.generateValidateCode(6).toString();
//
////        发送短信
//
//            boolean flag =SMSUtils.sendEmail(phone, code,javaMailSender,fromEmail);
////        将验证码保存到session
//            session.setAttribute(phone,code);
//            session.setMaxInactiveInterval(300);
//            if (flag) {
//                return R.success("验证码发送成功！");
//            }else {
//                return R.error("验证码发送失败，请重新发送！");
//            }
//        }
//
//        return  R.error("邮箱错误，验证码发送失败！");
//    }

    /**
     * 登录
     *
     * @param map     map
     * @param session session
     * @return R
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {

        String phone = map.get("phone");
        String code = map.get("code");

//        判断验证码是否正确
        if (code!=null&& !code.equals("") &&code.equals(session.getAttribute("code"))) {
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(phone!=null,User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
//            判断该用户是否已注册，未注册则直接注册
            if (user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
//        map.get(user)
        return R.error("登录失败！");
    }

    /**
     * 手机验证码
     *
     * @param user    user
     * @param session session
     * @return R
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

//        获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //        生成验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("验证码："+code);
//        发送短信

//        将验证码保存到session
            session.setAttribute("code",code);
            session.setMaxInactiveInterval(300);
        }

        return R.success("成功");
    }
}
