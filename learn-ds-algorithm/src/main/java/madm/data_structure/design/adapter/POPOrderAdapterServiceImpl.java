package madm.data_structure.design.adapter;

public class POPOrderAdapterServiceImpl implements OrderAdapterService {
    private POPOrderService popOrderService = new POPOrderService();

    public boolean isFirst(String uId) {
        return popOrderService.isFirstOrder(uId);
    }
}
