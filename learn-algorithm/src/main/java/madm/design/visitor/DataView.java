package madm.design.visitor;

import java.util.ArrayList;
import java.util.List;

public class DataView {
    List<User> userList = new ArrayList<User>();

    public DataView() {
        userList.add(new Student("谢飞机", "重点班", "一年一班"));
        userList.add(new Student("windy", "重点班", "一年一班"));
        userList.add(new Student("大毛", "普通班", "二年三班"));
        userList.add(new Student("shing", "普通班", "三年四班"));
        userList.add(new Student("BK", "特级教师", "一年一班"));
        userList.add(new Student("娜娜Goddess", "特级教师", "一年一班"));
        userList.add(new Student("dangdang", "普通教师", "二年三班"));
        userList.add(new Student("泽东", "实习教师", "三年四班"));
    }

    // 展示
    public void show(Visitor visitor) {
        for (User user : userList) {
            user.accept(visitor);
        }
    }
}
