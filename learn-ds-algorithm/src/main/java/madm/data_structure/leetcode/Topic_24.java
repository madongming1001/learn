package madm.data_structure.leetcode;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) { val = x; }
 * }
 */
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

/**
 * 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
 * <p>
 * 示例:
 * <p>
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 */
public class Topic_24 {
    public ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;

            head = next;
        }
        return pre;
    }

    public static void main(String[] args) {
//            0 iconst_0
//            1 istore_1
        int i = 0;
        while (true) {
            //常数+1
//            2 iinc 1 by 1
            i++;
//            5 iload_1
//            6 istore_2
//            7 goto 2 (-5)
            int j = i;
        }
    }
}
