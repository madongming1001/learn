package madm.design.decorator;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 4:58 PM
 */
public class Test {
    public static void main(String[] args) {
        SsoInterceptor ssoDecorator = new SsoInterceptor(new
                LoginSsoDecorator());
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu",
                "t");
        System.out.println("登录校验：" + request +"="+ (success ? "放行" : "拦截"));
    }
}
