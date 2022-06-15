package com.madm.learnroute.javaee;

import com.mdm.pojo.User;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author dongming.ma
 * @date 2022/6/15 01:01
 */
public class SpelExpressionPractice {
    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("name");
        User user = new User();
        user.setName("张三");
//        System.out.println(expression.getValue(user));

        String greetingExp = "Hello, #{#user} ---> #{T(System).getProperty('user.home')}";
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", "fsx");

        Expression expression = parser.parseExpression(greetingExp, new TemplateParserContext());
        System.out.println(expression.getValue(context, String.class)); //Hello, fsx ---> C:\Users\fangshixiang
    }
}
