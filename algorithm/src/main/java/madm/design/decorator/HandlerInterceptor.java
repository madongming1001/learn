package madm.design.decorator;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 4:52 PM
 */
public interface HandlerInterceptor {

    boolean preHandle(String request, String response, Object handler);

}
