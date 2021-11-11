package com.madm.learnroute.javaee;

import org.springframework.util.StringUtils;

public class TwoVersionCompare {

    public static void main(String[] args) {
        String version1 = "1.01";
        String version2 = "1.001";
        System.out.println(twoVersionCompare(version1, version2));
    }

    public static int twoVersionCompare(String version1, String version2) {
        if (!StringUtils.hasLength(version1) || !StringUtils.hasLength(version2)) {
            return 0;
        }
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        return twoVersionCompare(v1, v2, 0);
    }

    public static int twoVersionCompare(String[] version1, String[] version2, int vl) {
        if(version1.length == version2.length && version1.length == vl){
            return 0;
        }
        int result;
        Integer v1 = Integer.valueOf(vl < version1.length ? version1[vl] : "-1");
        Integer v2 = Integer.valueOf(vl < version2.length ? version2[vl] : "-1");
        return (result = v1.compareTo(v2)) == 0 ? twoVersionCompare(version1, version2, vl + 1) : result;
    }

}
