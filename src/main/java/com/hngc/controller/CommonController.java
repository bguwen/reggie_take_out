package com.hngc.controller;

import com.hngc.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file file
     * @return R
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
//        原始文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//        使用UUID重新生成文件名
        String fileName = UUID.randomUUID() + substring;
//        创建一个目录对象
        File dir = new File(basePath);
//        判断目录是否存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param response response
     * @param name     name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try {
//            通过输入流读取文件内容

            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

//            通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
//            设置响应文件类型
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
//             关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
