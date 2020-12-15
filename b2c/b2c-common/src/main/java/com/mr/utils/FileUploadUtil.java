package com.mr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


public class FileUploadUtil {
    /**
     * 上传
     * @param file 上传的文件
     * @param request 请求信息
     * @return
     */
    public static String upload(MultipartFile file,HttpServletRequest request){

        //上传
        if(file.isEmpty()){//判断上传过来的文件是否为空
            return "1";
        }
        String fileName = file.getOriginalFilename();//获取上传文件的文件名
        String path = request.getServletContext().getRealPath("/upload");//获取当前项目目录下的upload文件夹
        fileName = UUID.randomUUID() + fileName;//防止文件重复

        File dest = new File(path + File.separator +  fileName);//新建一个文件

        if (!dest.getParentFile().exists()) {//判断文件夹存不存在
            dest.getParentFile().mkdirs();//不存在的话创建文件夹
        }

        try {
            file.transferTo(dest);//上传操作
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileName;
    }


    /**
     * 下载
     * @param imgUrl 图片路径
     * @param request 请求
     * @param response 返回
     */
    public static  void download(String imgUrl,HttpServletRequest request,HttpServletResponse response){

        // 1. 获得文件的真实路径
        String newPath = request.getServletContext().getRealPath("/upload/" + imgUrl);
        // 2. 实例化 文件对象
        File f = new File(newPath);
        // 3. 获得文件名
        String fileName = f.getName();
        // 4. 定义输入输出流
        BufferedInputStream buffIn = null;
        BufferedOutputStream buffOut = null;
        try {
            // 5. 获得输入流
            buffIn = new BufferedInputStream(new FileInputStream(f));
            // 6. 获得 response  和  request (获得 request 和 response )
            // 7. 获得输入流       response.getOutputStream() 是servlet的输出流  浏览器的输出流
            buffOut = new BufferedOutputStream(response.getOutputStream());

            // 8. 对浏览器进行设置========================================================
            //解决浏览器兼容问题
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                fileName = new String(fileName.getBytes("GB2312"),"ISO-8859-1");
            } else {
                // 对文件名进行编码处理中文问题
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");// 处理中文文件名的问题
                fileName = new String(fileName.getBytes("UTF-8"), "GBK");// 处理中文文件名的问题
            }
            response.reset();
            response.setContentType("application/x-msdownload");// 不同类型的文件对应不同的MIME类型
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            //==========================================================================
            // 9. 循环 读写 赋值
            byte[] b = new byte[1024];
            int s = 0;
            while((s=buffIn.read(b))!=(-1)){
                buffOut.write(b);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                if(buffIn!=null){
                    buffIn.close();
                }
                if(buffOut!=null){
                    buffOut.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}