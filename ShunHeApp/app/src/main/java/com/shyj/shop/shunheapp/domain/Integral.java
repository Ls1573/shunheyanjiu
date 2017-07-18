package com.shyj.shop.shunheapp.domain;

import cn.bmob.v3.BmobObject;

/**
 * @author Ls
 * @description 积分记录
 * @date 2017/7/17
 * @package com.shyj.shop.shunheapp.domain
 */

public class Integral extends BmobObject {

    private String integralDetail;
    private Member member;

    public String getIntegralDetail() {
        return integralDetail;
    }

    public void setIntegralDetail(String integralDetail) {
        this.integralDetail = integralDetail;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
