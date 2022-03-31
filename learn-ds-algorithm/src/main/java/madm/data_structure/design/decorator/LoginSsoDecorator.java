package madm.data_structure.design.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 4:52 PM
 */
@Slf4j
public class LoginSsoDecorator implements HandlerInterceptor{
    @Override
    public boolean preHandle(String request, String response, Object handler) {
        // 模拟获取cookie
        String ticket = request.substring(0, 8);
        log.info("拦截初始方法执行");
        // 模拟校验
        return ticket.equals("success");
    }
}
