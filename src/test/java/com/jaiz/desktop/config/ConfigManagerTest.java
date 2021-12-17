package com.jaiz.desktop.config;

import cn.unicom.wireless.common.util.JSONUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jaiz.desktop.entities.AppProperties;
import org.junit.Test;

import java.io.IOException;

public class ConfigManagerTest {

    @Test
    public void validateAndInitTest() throws IOException {
        ConfigManager<AppProperties> cm=new ConfigManager<>("xxx");
        cm.validateAndInit(new TypeReference<>() {});
        System.out.println(JSONUtils.toJsonString(cm.configBean()));
    }

}
