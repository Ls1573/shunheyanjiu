package com.shyj.shop.shunheapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.shyj.shop.shunheapp.utils.QuickSetParcelableUtil;

import cn.bmob.v3.BmobObject;

/**
 * @author Ls
 * @description 会员信息
 * @date 2017/7/17
 * @package com.shyj.shop.shunheapp.domain
 */

public class Member extends BmobObject implements Parcelable{

    private String teleNum;
    private String birth;
    private String integral;
    private String memberName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        QuickSetParcelableUtil.write(dest, this);
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {

        @Override
        public Member createFromParcel(Parcel source) {
            Member obj = (Member) QuickSetParcelableUtil
                    .read(source, Member.class);
            return obj;
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }

    };


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTeleNum() {
        return teleNum;
    }

    public void setTeleNum(String teleNum) {
        this.teleNum = teleNum;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
