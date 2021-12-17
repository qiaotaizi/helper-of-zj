package com.jaiz.desktop.config;

import cn.unicom.wireless.common.util.JSONUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jaiz.desktop.entities.AppProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

@RequiredArgsConstructor
public class ConfigManager<T> {

    private final String configName;

    private T configBean;

    public T configBean(){
        return this.configBean;
    }

    /**
     * 初始化config文件夹
     * 初始化配置文件
     * 从配置文件初始化配置对象
     * @param appPropertiesTypeReference
     * @return
     */
    public void validateAndInit(TypeReference<T> ref) throws IOException {

        var userHome = System.getProperty("user.home");

        System.out.println(userHome);

        //目录？
        File configDir=new File(userHome+File.separatorChar+"."+configName);
        if (!configDir.exists()){
            var mkFlag=configDir.mkdirs();
            if (mkFlag){
                System.out.println("创建配置目录成功");
            }
        }
        //文件？
        File configJson=new File(configDir.getPath()+File.separatorChar+"config.json");
        if (!configJson.exists()){
            var mkFlag=configJson.createNewFile();
            if (mkFlag){
                System.out.println("创建配置文件成功");
            }
        }

        //读取json内容
        var json=readAll(configJson);
        if(StringUtils.isBlank(json)){
            json="{}";
        }

        configBean = JSONUtils.toObject(json,ref);
    }

    private String readAll(File configJson) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(configJson));
        String line;
        StringBuilder sb=new StringBuilder();
        while ((line=br.readLine())!=null){
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 同步配置内容到文件
     * @param configObject
     */
    public void syncConfigFile() throws IOException {
        var json=JSONUtils.toJsonString(configBean());
        File configFile = new File(System.getProperty("user.home")+File.separatorChar+"."+configName+File.separatorChar+"config.json");
        writeAll(configFile,json);
    }

    private void writeAll(File configFile, String json) throws IOException {
        BufferedWriter bw=new BufferedWriter(new FileWriter(configFile));
        bw.write(json);
        bw.flush();
        bw.close();
    }
}
