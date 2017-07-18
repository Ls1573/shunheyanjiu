package com.shyj.shop.shunheapp.integral;

import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shyj.shop.shunheapp.R;
import com.shyj.shop.shunheapp.base.App;
import com.shyj.shop.shunheapp.base.BaseActivity;
import com.shyj.shop.shunheapp.domain.Integral;
import com.shyj.shop.shunheapp.domain.Member;
import com.shyj.shop.shunheapp.integral.pop.BirthdayPopupWindow;
import com.shyj.shop.shunheapp.member.IntegralRecordActivity;
import com.shyj.shop.shunheapp.utils.AlertUtil;
import com.shyj.shop.shunheapp.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Ls
 * @description
 * @date 2017/7/17
 * @package com.shyj.shop.shunheapp.integral
 */

public class IntegralInsertActivity extends BaseActivity implements View.OnClickListener {

    private TextView phone_num_tv;
    private TextView birth_tv;
    private CheckBox add_cb, reduce_cb;
    private TextView integral_et;
    private TextView login_confirm_tv;
    private TextView integral_now_tv;

    private Member member;
    private String type;
    private String teleNum;
    private String objId;

    private TextView tv_title, tv_right;
    private ImageView iv_back;

    private BirthdayPopupWindow birthdayPopupWindow;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_insert_integral);
        phone_num_tv = findView(R.id.phone_num_tv);
        birth_tv = findView(R.id.birth_tv);
        add_cb = findView(R.id.add_cb);
        reduce_cb = findView(R.id.reduce_cb);
        integral_et = findView(R.id.integral_et);
        login_confirm_tv = findView(R.id.login_confirm_tv);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        integral_now_tv = (TextView) findViewById(R.id.integral_now_tv);
        iv_back = (ImageView) findViewById(R.id.iv_back);


        login_confirm_tv.setOnClickListener(this);
        add_cb.setOnClickListener(this);
        reduce_cb.setOnClickListener(this);
        birth_tv.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        tv_title.setText("会员积分");
        tv_right.setText("积分记录");
    }

    @Override
    public void initData() {
        type = getIntent().getStringExtra("type");

        if (type.equals("1")) {
            //该会员已存在
            member = getIntent().getParcelableExtra("member");
            objId = getIntent().getStringExtra("objId");
            integral_now_tv.setText(member.getIntegral());
            phone_num_tv.setText(member.getTeleNum());
            reduce_cb.setVisibility(View.VISIBLE);
            if (!StringUtil.isEmpty(member.getBirth())) {
                birth_tv.setText(member.getBirth());
            } else {
                birth_tv.setText("请设置会员生日");
            }

            //会员存在可以查看记录
            tv_right.setVisibility(View.VISIBLE);
        } else {
            //该会员第一次
            teleNum = getIntent().getStringExtra("phoneNum");
            integral_now_tv.setText("0");
            phone_num_tv.setText(teleNum);
            birth_tv.setText("请设置会员生日");
            birth_tv.setEnabled(true);
            birth_tv.setClickable(true);
            reduce_cb.setVisibility(View.GONE);

            //会员不存在不可以查看记录
            tv_right.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                //积分记录
                Intent intent = new Intent(this, IntegralRecordActivity.class);
                intent.putExtra("objId", objId);
                intent.putExtra("member", (Parcelable) member);
                startActivity(intent);
                break;
            case R.id.login_confirm_tv:
                //确认提交
                if (StringUtil.isEmpty(integral_et.getEditableText().toString())) {
                    AlertUtil.t(App.context, "请输入积分");
                    return;
                }
                confirm();
                break;
            case R.id.add_cb:
                add_cb.setChecked(true);
                reduce_cb.setChecked(false);
                break;
            case R.id.reduce_cb:
                add_cb.setChecked(false);
                reduce_cb.setChecked(true);
                break;
            case R.id.birth_tv:
                //选择生日
                getSelectBirthday();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 生日
     */
    private void getSelectBirthday() {
        birthdayPopupWindow = new BirthdayPopupWindow(IntegralInsertActivity.this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                birthdayPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.birthday_submit:// 确定
                        if (!"null".equals(birthdayPopupWindow.result) && birthdayPopupWindow.result != null)
                            birth_tv.setText(birthdayPopupWindow.result);
                        else
                            birth_tv.setText(getData());
                        break;

                    default:
                        break;
                }
            }
        });
        birthdayPopupWindow.showAtLocation(login_confirm_tv, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }


    /**
     * @return 获取当前时间
     */
    public String getData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }


    private void confirm() {
        Member member = new Member();
        if (type.equals("1")) {
            try {

                if (add_cb.isChecked()) {
                    //增加积分
                    long integral = Long.valueOf(integral_et.getText().toString()) + Long.valueOf(this.member.getIntegral());
                    member.setIntegral(String.valueOf(integral));
                } else {
                    //减少积分
                    long integral = Long.valueOf(this.member.getIntegral()) - Long.valueOf(integral_et.getText().toString());
                    if (integral < 0) {
                        AlertUtil.t(App.context, "可用积分不足");
                        return;
                    }
                    member.setIntegral(String.valueOf(integral));
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //设置会员信息
            member.setTeleNum(phone_num_tv.getText().toString());
            if (!birth_tv.getText().toString().equals("请设置会员生日")) {
                member.setBirth(birth_tv.getText().toString());
            }
            //修改会员信息表
            showLoadingDialog();
            member.update(objId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {

                        //更新积分记录
                        Member memberChild = new Member();
                        memberChild.setObjectId(objId);
                        Integral integral = new Integral();
                        integral.setMember(memberChild);
                        if (add_cb.isChecked()) {
                            integral.setIntegralDetail("+" + integral_et.getEditableText().toString());
                        } else {
                            integral.setIntegralDetail("-" + integral_et.getEditableText().toString());
                        }

                        integral.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    hideLoadingDialog();
                                    AlertUtil.t(App.context, "积分设置成功!");
                                    finish();
                                } else {
                                    hideLoadingDialog();
                                    AlertUtil.t(App.context, "积分记录更新失败!");
                                }
                            }
                        });


                    } else {
                        hideLoadingDialog();
                        AlertUtil.t(App.context, "修改失败!");
                    }
                }
            });
        } else {
            member.setIntegral(integral_et.getText().toString());
            member.setTeleNum(phone_num_tv.getText().toString());
            if (!birth_tv.getText().toString().equals("请设置会员生日")) {
                member.setBirth(birth_tv.getText().toString());
            }
            //增加会员
            showLoadingDialog();
            member.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {

                        //更新积分记录
                        Member memberChild = new Member();
                        memberChild.setObjectId(s);
                        Integral integral = new Integral();
                        integral.setMember(memberChild);
                        integral.setIntegralDetail("+" + integral_et.getEditableText().toString());

                        integral.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    hideLoadingDialog();
                                    AlertUtil.t(App.context, "积分设置成功!");
                                    finish();
                                } else {
                                    hideLoadingDialog();
                                    AlertUtil.t(App.context, "积分记录更新失败!");
                                }
                            }
                        });

                    } else {
                        hideLoadingDialog();
                        AlertUtil.t(App.context, "修改失败!");
                    }
                }
            });
        }
    }
}
