package com.cchao.insomnia.model.javabean.home;

/**
 * 抽屉Menu的实体
 */
public class NavItem {

    public int ID;
    public String mTitle;
    public int mIconRes;

    public String mLabelText;
    public String mValueText;
    private Margin mMargin = Margin.none;

    public enum Margin {
        top, bottom, none
    }

    public NavItem(int id, String title, int iconRes) {
        this.ID = id;
        this.mTitle = title;
        this.mIconRes = iconRes;
    }

    /**
     * 设置menu参数
     *
     * @param id      view 的id
     * @param title   选项文本
     * @param iconRes 图片资源ID
     * @param margin  1 上边距，2 下边距
     */
    public NavItem(int id, String title, int iconRes, Margin margin) {
        this.ID = id;
        this.mTitle = title;
        this.mIconRes = iconRes;
        mMargin = margin;
    }

    public Margin getMargin() {
        return mMargin;
    }

    public NavItem setMargin(Margin margin) {
        mMargin = margin;
        return this;
    }

}
