package com.madm.learnroute.technology.mybatis.plugin;

/**
 * @author dongming.ma
 * @date 2022/6/12 19:38
 */

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MapperConfig {
    //将插件加入到mybatis插件拦截链中
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
                MyInterceptor myPlugin = new MyInterceptor();
                //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
                Properties properties = new Properties();
                //这里设置慢查询阈值为1毫秒，便于测试
                properties.setProperty("time", "1");
                myPlugin.setProperties(properties);
                configuration.addInterceptor(myPlugin);
            }
        };
    }
}