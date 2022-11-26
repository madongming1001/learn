package com.madm.interview_guide;

import java.util.HashSet;

/**
 * @author dongming.ma
 * @date 2022/7/5 17:28
 */
public class OrphanNode {
    public static HashSet<Node> main(String[] args) {
        Node[] root = new Node[2];
        HashSet<Node> seen = new HashSet<>();
        HashSet<Node> notSeen = new HashSet<>();
        for (Node node : root) {
            Node next;
            while ((next = node.next) != null) {
                seen.add(node);
                next = next.next;
            }
        }
        for (Node node : root) {
            if (!seen.add(node)) {
                notSeen.add(node);
            }
        }
        return notSeen;
    }

}
class Node {
    public int index;//用来表示自己位于ROOT[n]中的位置序号
    public Node next;//用来表示自己指向的下一级Node
}