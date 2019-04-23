package com.cchao.insomnia.util;

import java.util.Date;

/**
 * @author : cchao
 * @version 2019-04-23
 */
public class TimeHelper {

    public static String getStandardDate(Date date) {
        if (date == null) {
            return getStandardDate(System.currentTimeMillis());
        }
        return getStandardDate(date.getTime());
    }

    /**
     * 返回 格式化过去表达
     *
     * @param timeStr 毫秒数
     * @return 2天前
     */
    public static String getStandardDate(long timeStr) {
        String temp = "";
        try {
            long diff = (System.currentTimeMillis() - timeStr) / 1000;
            long months = diff / (60 * 60 * 24 * 30);
            long days = diff / (60 * 60 * 24);
            long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
            long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
            if (months > 0) {
                temp = months + "月前";
            } else if (days > 0) {
                temp = days + "天前";
            } else if (hours > 0) {
                temp = hours + "小时前";
            } else if (minutes > 1) {
                temp = minutes + "分钟前";
            } else {
                temp = "刚刚";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
