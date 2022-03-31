package madm.data_structure.design.decorator;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author MaDongMing
 * @Date 2022/3/31 4:55 PM
 */
@Slf4j
public class SsoInterceptor extends SsoDecorator {

    private static Map<String, String> authMap = new
            ConcurrentHashMap<String, String>();

    static {
        authMap.put("huahua", "queryUserInfo");
        authMap.put("doudou", "queryUserInfo");
    }

    public SsoInterceptor(HandlerInterceptor handlerInterceptor) {
        super(handlerInterceptor);
    }

    @Override
    public boolean preHandle(String request, String response, Object
            handler) {
        boolean success = super.preHandle(request, response, handler);
        if (!success) return false;
        String userId = request.substring(8);
        String method = authMap.get(userId);
        log.info("模拟单点登录方法访拦截校验：{} {}", userId, method);
        //模拟方法校验
        return "queryUserInfo".equals(method);
    }
}
