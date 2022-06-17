package com.madm.learnroute.javaee;

import com.mdm.pojo.Person;
import com.mdm.pojo.User;
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

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
//        Expression expression = parser.parseExpression("name");
        User user = new User();
        user.setName("张三");
//        System.out.println(expression.getValue(user));

        String greetingExp = "Hello, #{#user} ---> #{T(System).getProperty('user.home')}";
        context.setVariable("user", "fsx");

        Expression expression = parser.parseExpression(greetingExp, new TemplateParserContext());
        System.out.println(expression.getValue(context, String.class)); //Hello, fsx ---> C:\Users\fangshixiang


        context.setRootObject(new Person("fsx", 18)); // 这个就是最终#root取出来的对象  若没有设置  就不能使用#root


        System.out.println(parser.parseExpression("#root").getValue(context) instanceof Person); // true
        System.out.println(parser.parseExpression("#root").getValue(context)); //Person{name='fsx', age=18}
        System.out.println(parser.parseExpression("#root.name").getValue(context)); //fsx
        System.out.println(parser.parseExpression("#root.age").getValue(context)); // 18


        // 若单纯的想获取属性值，请不要使用#  直接使用name即可
        // #root代表把root当作key先去查找出对象，再导航查找。。。。。而不用#类似全文查找（这个做法非常非常像JSP的el表达式的写法）
        System.out.println(parser.parseExpression("#name").getValue(context)); // null
        System.out.println(parser.parseExpression("name").getValue(context)); // fsx


        // el参与计算时，取值方式也可以是#root 或者直接name属性的方式 都是可以的
        System.out.println(parser.parseExpression("name=='孙悟空'").getValue(context)); //false
        System.out.println(parser.parseExpression("name=='fsx'").getValue(context)); //true
        System.out.println(parser.parseExpression("#root.name=='fsx'").getValue(context)); //true

        System.out.println(parser.parseExpression("name.equals('fsx')").getValue(context)); //true
        //org.springframework.expression.spel.SpelEvaluationException: EL1004E: Method call: Method equalsXXX(java.lang.String) cannot be found on type java.lang.String
        System.out.println(parser.parseExpression("name.equalsXXX('fsx')").getValue(context)); // 报错
    }
}
