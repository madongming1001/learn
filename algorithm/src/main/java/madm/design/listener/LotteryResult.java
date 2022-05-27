package madm.design.listener;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:48 PM
 */
@Data
@AllArgsConstructor
public class LotteryResult {
    private String uid;
    private String msg;
    private Date date;
}
