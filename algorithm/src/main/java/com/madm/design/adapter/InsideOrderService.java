package com.madm.design.adapter;

public class InsideOrderService implements OrderAdapterService {
    private OrderService orderService = new OrderService();

    public boolean isFirst(String uId) {
        return orderService.queryUserOrderCount(uId) <= 1;
    }

}
