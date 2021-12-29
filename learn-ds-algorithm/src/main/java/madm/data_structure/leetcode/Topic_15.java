package madm.data_structure.leetcode;

import java.util.*;

/**
 * 给你一个包含 n 个整数的数组nums，判断nums中是否存在三个元素 a，b，c ，使得a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 *
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [-1,0,1,2,-1,-4]
 * 输出：[[-1,-1,2],[-1,0,1]]
 */
public class Topic_15 {
    public static void main(String[] args) {
        Topic_15 topic = new Topic_15();
        int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println(topic.threeSum(nums));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> res = new HashSet<>();
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int l = i + 1;
            int r = n - 1;
            while (l < r) {
                if ((nums[i] + nums[l] + nums[r]) == 0) {
                    //当走到这里的时候坑定是组成一对了，所以这一对不能用了 得换遍历了
                    res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    l++;
                    r--;
                } else if ((nums[i] + nums[l] + nums[r]) < 0) {
                    l++;
                } else if ((nums[i] + nums[l] + nums[r]) > 0) {
                    r--;
                }
            }
        }
        return new ArrayList<>(res);
    }

}
