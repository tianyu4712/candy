package com.candy.bean;

/**
 * TODO
 *
 * @author zhangkai
 * @date 2019年04月14日 10:31
 */
public class CandyDataItem {

    private String views;

    private String clicks;

    private String advnum;

    private String sumadvpay;

    private String planidid;

    private String day;

    private String planname;

    private String adstype;

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getAdvnum() {
        return advnum;
    }

    public void setAdvnum(String advnum) {
        this.advnum = advnum;
    }

    public String getSumadvpay() {
        return sumadvpay;
    }

    public void setSumadvpay(String sumadvpay) {
        this.sumadvpay = sumadvpay;
    }

    public String getPlanidid() {
        return planidid;
    }

    public void setPlanidid(String planidid) {
        this.planidid = planidid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getAdstype() {
        return adstype;
    }

    public void setAdstype(String adstype) {
        this.adstype = adstype;
    }

    @Override
    public String toString() {
        return "CandyDataItem{" +
                "views='" + views + '\'' +
                ", clicks='" + clicks + '\'' +
                ", advnum='" + advnum + '\'' +
                ", sumadvpay='" + sumadvpay + '\'' +
                ", planidid='" + planidid + '\'' +
                ", day='" + day + '\'' +
                ", planname='" + planname + '\'' +
                ", adstype='" + adstype + '\'' +
                '}';
    }
}
