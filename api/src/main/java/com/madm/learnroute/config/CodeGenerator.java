package com.madm.learnroute.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongming.ma
 * @date 2022/11/9 22:12
 */
@Configuration
@RefreshScope
public class CodeGenerator {

    private static String finalProjectPath;

    static {
        finalProjectPath = System.getProperty("user.dir") + "/api";
    }

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String username;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;
    @Value("${mybatiscode.generate:false}")
    private boolean generate;

    public void generator() {
        if (generate) {
            final String projectPath = finalProjectPath;
            FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
                builder.author("dongming.ma") // 设置作者
                        .enableSwagger() // 开启 swagger 模式
                        .fileOverride() // 覆盖已生成文件
                        .disableOpenDir() //禁止打开输出目录
                        .outputDir(projectPath + "/src/main/java"); // 指定输出目录
            }).packageConfig(builder -> {
                builder.parent("com.madm.learnroute") // 设置父包名
                        .moduleName("test") // 设置父包模块名
                        .entity("model.entity") //设置entity包名
                        .other("model.dto") // 设置dto包名
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径

            }).injectionConfig(consumer -> {
                Map<String, String> customFile = new HashMap<>();
                // DTO
                customFile.put("DTO.java", "/templates/entityDTO.java.ftl");
                consumer.customFile(customFile);
            });

        }
    }

}
