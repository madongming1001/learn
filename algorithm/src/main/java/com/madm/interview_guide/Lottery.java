package com.madm.interview_guide;

import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/7/5 17:55
 */
public class Lottery {

    /**
     * 根据Math.random()产生一个double型的随机数，判断每个奖品出现的概率
     *
     * @param prizes
     * @return random：奖品列表prizes中的序列（prizes中的第random个就是抽中的奖品）
     */
    public int getPrizeIndex(List<Prize> prizes) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int random = -1;
        try {
            //计算总权重
            double sumWeight = 0;
            for (Prize p : prizes) {
                sumWeight += p.getPrize_weight();
            }

            //产生随机数
            double randomNumber;
            randomNumber = Math.random();

            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for (int i = 0; i < prizes.size(); i++) {
                //当前商品权重百分比
                d2 += Double.parseDouble(String.valueOf(prizes.get(i).getPrize_weight())) / sumWeight;
                //第一个商品为0
                if (i == 0) {
                    d1 = 0;
                } else {
                    //其他商品为最后一个的权重
                    d1 += Double.parseDouble(String.valueOf(prizes.get(i - 1).getPrize_weight())) / sumWeight;
                }
                if (randomNumber >= d1 && randomNumber <= d2) {
                    random = i;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("生成抽奖随机数出错，出错原因：" + e.getMessage());
        }
        return random;
    }

}

@Data
class Prize {
    private int id;//奖品id
    private String prize_name;//奖品名称
    private int prize_amount;//奖品（剩余）数量
    private int prize_weight;//奖品权重
}