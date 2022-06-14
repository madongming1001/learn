package com.madm.learnroute.javaee;

import com.mdm.pojo.User;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author dongming.ma
 * @date 2022/6/15 01:01
 */
public class SpelExpressionPractice {
    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("name");
        User user = new User();
        user.setName("张三");
        System.out.println(expression.getValue(user));
    }
}
