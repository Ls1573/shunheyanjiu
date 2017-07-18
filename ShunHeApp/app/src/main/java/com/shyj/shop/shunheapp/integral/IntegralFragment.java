package com.shyj.shop.shunheapp.integral;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shyj.shop.shunheapp.R;
import com.shyj.shop.shunheapp.base.App;
import com.shyj.shop.shunheapp.base.BaseFragment;
import com.shyj.shop.shunheapp.domain.Integral;
import com.shyj.shop.shunheapp.domain.Member;
import com.shyj.shop.shunheapp.utils.AlertUtil;
import com.shyj.shop.shunheapp.utils.ClassPathResource;
import com.shyj.shop.shunheapp.utils.StringUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author Ls
 * @description
 * @date 2017/7/17
 * @package com.shyj.shop.shunheapp
 */

public class IntegralFragment extends BaseFragment implements View.OnClickListener {

    private EditText login_tele_num_et;
    private TextView login_confirm_tv;


    @Override
    public int setLayout() {
        return R.layout.fragment_integral;
    }

    @Override
    public void initView(View view) {

        login_tele_num_et = (EditText) view.findViewById(R.id.login_tele_num_et);
        login_confirm_tv = (TextView) view.findViewById(R.id.login_confirm_tv);

        login_confirm_tv.setOnClickListener(this);
        view.findViewById(R.id.tv_create).setOnClickListener(this);
        view.findViewById(R.id.tv_create1).setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_confirm_tv:
                if (StringUtil.isEmpty(login_tele_num_et.getEditableText().toString())) {
                    AlertUtil.t(App.context, "请输入会员手机号码");
                    return;
                }

                if (!ClassPathResource.isMobileNO(login_tele_num_et.getEditableText().toString())){
                    AlertUtil.t(App.context, "请输入正确的手机号码");
                    return;
                }
                queryMember();

                break;
            case R.id.tv_create:

                final Member member = new Member();
                member.setIntegral("0");
                member.setTeleNum("18742518490");
                member.setBirth("1993/04/23");
                member.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "创建数据成功：" + s, Toast.LENGTH_SHORT);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });


                break;

            case R.id.tv_create1:

                BmobQuery<Member> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("teleNum", "18742518490");
                bmobQuery.findObjects(new FindListener<Member>() {
                    @Override
                    public void done(List<Member> list, BmobException e) {
                        for (Member member1 : list) {
                            Member member2 = new Member();
                            Log.d("IntegralFragment", member1.getObjectId());
                            member2.setObjectId(member1.getObjectId());
                            Integral integral = new Integral();
                            integral.setMember(member2);
                            integral.setIntegralDetail("+100");
                            integral.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "ok" + s, Toast.LENGTH_SHORT);
                                    } else {
                                        Log.e("bmob", e.toString());
                                    }
                                }
                            });
                        }
                    }
                });


                break;
        }

    }

    private void queryMember() {

        showLoadingDialog();
        BmobQuery<Member> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("teleNum", login_tele_num_et.getEditableText().toString());
        bmobQuery.findObjects(new FindListener<Member>() {
            @Override
            public void done(List<Member> list, BmobException e) {
                if (e == null) {
                    hideLoadingDialog();
                    if (list != null && list.size() > 0) {
                        //跳转
                        Intent intent = new Intent(getActivity(), IntegralInsertActivity.class);
                        intent.putExtra("member", (Parcelable) list.get(0));
                        intent.putExtra("objId", list.get(0).getObjectId());
                        intent.putExtra("type", "1");
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), IntegralInsertActivity.class);
                        intent.putExtra("phoneNum", login_tele_num_et.getEditableText().toString());
                        intent.putExtra("type", "2");
                        startActivity(intent);
                    }

                } else {
                    hideLoadingDialog();
                    AlertUtil.t(App.context, "会员查找失败!");
                }
            }
        });

    }
}
