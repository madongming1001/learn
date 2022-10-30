package com.madm.learnroute.technology.mybatis;

import org.apache.ibatis.io.Resources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MybatisPractice {
    static ResourceLoader resourceLoader;
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = (FileInputStream)Resources.getResourceAsStream("mybatis.xml");
        Resource resource = resourceLoader.getResource("classpath:default.xml");
        File file = resource.getFile();
        file.lastModified();

//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(InputStream);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        mapper.selectById();
    }
}
