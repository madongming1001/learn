package com.madm.learnroute.javaee;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.madm.learnroute.dto.UserDto;
import com.mdm.pojo.AuthParam;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * hutool加密第一次会加载很多加密包，导致第一次请求接口过慢，耗时长
 * 解决方案：使用google提供的加密方案
 * https://mp.weixin.qq.com/s/9Xa_ElBR3c8I29JY2k1RRg
 *
 * @author dongming.ma
 * @date 2022/9/2 16:27
 */
public class EncryptPractice {

    @SneakyThrows
    public static void main(String[] args) {
        String source = "收拾收拾";
        String expected1 = SecureUtil.md5().digestHex(source);
        Digester digester = SecureUtil.sha1();
        //hutool提供的摘要加密算法 内部第一次初始化会加载很多第三方加密包 耗时过长
        String expected3 = MD5.create().digestHex16(source);
        String expected2 = MD5Utils.md5Hex(source.getBytes());
        String expected4 = DigestUtils.md5Hex(source);
        System.out.println(expected1.equals(expected3));
        System.out.println(expected3);
        System.out.println(expected2);
        System.out.println(expected1);
        System.out.println(expected4);
    }

    /**
     * 生成数据哈希
     */
    private String generateSnapShotHash(UserDto userDto) {
        StringBuilder builder = new StringBuilder();
        for (AuthParam authParam : userDto.getAuth()) {
            builder.append(authParam.getAppId()).append(authParam.getAppKey());
        }
        return MD5.create().digestHex16(builder.toString());
    }
}
