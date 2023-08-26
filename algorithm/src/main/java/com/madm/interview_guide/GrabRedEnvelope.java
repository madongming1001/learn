package com.madm.interview_guide;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/7/15 17:05
 */
public class GrabRedEnvelope {

    public static void main(String[] args) {
        redEnvelopeLuckyDraw();
    }

    private static List<Account> redEnvelopeLuckyDraw() {
        BigDecimal redEnvelopeAmount = BigDecimal.valueOf(100);
        BigDecimal divide = redEnvelopeAmount.divide(BigDecimal.valueOf(100));
        return Collections.emptyList();
    }

}

@Data
class Account {
    private String name;
    private BigDecimal amount;
    private Account holder;
}