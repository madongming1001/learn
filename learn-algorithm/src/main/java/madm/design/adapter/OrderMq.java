package madm.design.adapter;

import lombok.Data;

import java.util.Date;

@Data
public class OrderMq {
    private String uid; // 用户ID
    private String sku; // ࠟ商品
    private String orderId; // 订单ID
    private Date createOrderTime; // 下单时间
}
