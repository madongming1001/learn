package madm.design.adapter;

import com.google.gson.Gson;
import com.mdm.utils.GsonObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class TestMain {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Gson gson = GsonObject.createGson();
        create_account create_account = new create_account();
        create_account.setNumber("100001");
        create_account.setAddress("河南省.廊坊市.广阳区.大学里职业技术学院");
        create_account.setAccountDate(new Date());
        create_account.setDesc("在校开户");

        System.out.println(gson.toJson(create_account));


//        HashMap<String, String> link01 = new HashMap<String, String>();
//        link01.put("userId", "number");
//        link01.put("bizId", "number");
//        link01.put("bizTime", "accountDate");
//        link01.put("desc", "desc");
//        RebateInfo rebateInfo01 = MQAdapter.filter(JSONObject.toJSONString(create_account),
//                link01);
//        System.out.println("mq.create_account(适配前)" +
//                create_account.toString());
//        System.out.println("mq.create_account(适配后)" +
//                JSON.toJSONString(rebateInfo01));
//        System.out.println("");
//        OrderMq orderMq = new OrderMq();
//        orderMq.setUid("100001");
//        orderMq.setSku("10928092093111123");
//        orderMq.setOrderId("100000890193847111");
//        orderMq.setCreateOrderTime(new Date());
//        HashMap<String, String> link02 = new HashMap<String, String>();
//        link02.put("userId", "uid");
//        link02.put("bizId", "orderId");
//        link02.put("bizTime", "createOrderTime");
//        RebateInfo rebateInfo02 = MQAdapter.filter(JSON.toJSONString(orderMq),
//                link02);
//        System.out.println("mq.orderMq(适配前)" + orderMq.toString());
//        System.out.println("mq.orderMq(适配后)" +
//                JSON.toJSONString(rebateInfo02));
    }
}
