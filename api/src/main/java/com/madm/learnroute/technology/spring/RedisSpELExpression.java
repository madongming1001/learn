package com.madm.learnroute.technology.spring;

import com.mdm.pojo.User;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author dongming.ma
 * @date 2023/9/3 20:22
 */
public class RedisSpELExpression {


    public static void main(String[] args) {
        new RedisSpELExpression().monitorSystemRunning();
    }

    public void monitorSystemRunning() {
        User user = new User();
        user.setId(89);


        String spel = "#user.getId()>80";

        StandardEvaluationContext sec = new StandardEvaluationContext();
        sec.setVariable("user", user);

        SpelExpressionParser spelParser = new SpelExpressionParser();
        boolean val = (boolean) spelParser.parseExpression(spel).getValue(sec);
        if (val) {
            System.out.println("结果为" + val);
        }

    }

}
