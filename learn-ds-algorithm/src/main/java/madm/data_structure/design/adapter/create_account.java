package madm.data_structure.design.adapter;

import lombok.Data;

import java.util.Date;

@Data
public class create_account {
    private String number; // 开户编号
    private String address; // 开户地
    private Date accountDate; // 开户时间
    private String desc; // 开户描述
}
