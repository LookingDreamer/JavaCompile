package com.pack;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: cjianquan
 * date: 2016/11/11
 */
@Service("packService")
public class PackService {

    public boolean packFiles(PackBean packBean) throws Exception{
        String propath = packBean.getPropath();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String clazzPath = propath+"\\"+sdf.format(new Date());
        File clazzFile = new File(clazzPath);
        // cjianquan 2016/9/6
        //删除目录
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(clazzFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clazzFile.mkdirs();
        if(packBean.getDestPackPath()==null || "".equals(packBean.getDestPackPath())){
            packBean.setDestPackPath(clazzPath);
        }
        try {
            for(String i_pack:packBean.getPackFiles()){
                if(i_pack.endsWith(".java")){
                    String javaName = PackUtils.getFilenameWithoutExt(i_pack);
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getSrcPath())+packBean.getSrcPath().length(),i_pack.lastIndexOf("\\"));
                    String javaPath = packBean.getPropath()+packBean.getCompilePath()+packagePath;
                    File javaFile = new File(javaPath);
                    System.out.println(" javaFile:"+javaFile);
                    for(File tmpFile:javaFile.listFiles()){
                        if(tmpFile.getName().contains(javaName+".class") || tmpFile.getName().matches(javaName + "\\$.*\\.class")){
                            org.apache.commons.io.FileUtils.copyFileToDirectory(tmpFile,new File(packBean.getDestPackPath()+PackUtils.webinfocls+packagePath));
                        }
                    }
                    System.out.println(" end javaPath:"+javaPath);
                }else if(i_pack.contains(PackUtils.resources)){
                    //将该文件复制到WebRoot/WEB-INF/classes/中
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getResourcesPath())+packBean.getResourcesPath().length(),i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack),new File(packBean.getDestPackPath()+PackUtils.webinfocls+packagePath));
                }else if(i_pack.contains(packBean.getWrPath())){
                    //将该文件复制到WebRoot中
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getWrPath())+packBean.getWrPath().length(),i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack),new File(packBean.getDestPackPath()+packagePath));
                }else {
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getSrcPath())+packBean.getSrcPath().length(),i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack),new File(packBean.getDestPackPath()+PackUtils.webinfocls+packagePath));
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return true;
    }

    public List<String> getUpdatefiles(PackBean packBean) throws Exception{
        List<String> fileList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println("开始获取更新文件....");
            Date packTime = sdf.parse(packBean.getPacktime());
            System.out.println("更新日期:"+packBean.getPacktime()+"   getPropath:"+packBean.getPropath() + "    getSrcPath:"+packBean.getSrcPath()+"");
            //获取src文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getSrcPath(),packTime ,fileList);
            //获取resource文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getResourcesPath(),packTime ,fileList);

//            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getWrPath(),packBean.getCompilePath(),packTime,fileList);
        } catch (ParseException e) {
            System.out.println("发生了异常");
            throw new Exception(e);
        }catch (Exception e){
            throw new Exception(e);
        }

        return fileList;
    }

    public List<String> javaComplier(PackBean packBean) throws Exception{
        List<String> fileList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println("开始获取更新文件....");
            Date packTime = sdf.parse(packBean.getPacktime());
            System.out.println("更新日期:"+packBean.getPacktime()+"   getPropath:"+packBean.getPropath() + "    getSrcPath:"+packBean.getSrcPath()+"");
            //获取src文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getSrcPath(),packTime ,fileList);
            //获取resource文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getResourcesPath(),packTime ,fileList);

//            PackUtils.getUpdateFiles(packBean.getPropath()+packBean.getWrPath(),packBean.getCompilePath(),packTime,fileList);
        } catch (ParseException e) {
            System.out.println("发生了异常");
            throw new Exception(e);
        }catch (Exception e){
            throw new Exception(e);
        }

        return fileList;
    }


}
