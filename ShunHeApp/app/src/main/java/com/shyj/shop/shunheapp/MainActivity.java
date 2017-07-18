package com.shyj.shop.shunheapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.shyj.shop.shunheapp.base.BaseActivity;
import com.shyj.shop.shunheapp.integral.IntegralFragment;
import com.shyj.shop.shunheapp.member.MemberFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton radio_integral, radio_account;
    private IntegralFragment integralFragment;
    private MemberFragment memberFragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        radio_integral = (RadioButton) findViewById(R.id.radio_integral);
        radio_account = (RadioButton) findViewById(R.id.radio_account);

        radio_integral.setOnClickListener(this);
        radio_account.setOnClickListener(this);
    }


    @Override
    public void initData() {
        invokeIntegralFragment();
        showFragment(integralFragment);
    }

    private void invokeIntegralFragment() {

        if (integralFragment == null) {
            integralFragment = new IntegralFragment();
            addFragment(integralFragment, "integralFragment");
        }
    }

    private void invokeMemberFragment() {

        if (memberFragment == null) {
            memberFragment = new MemberFragment();
            addFragment(memberFragment, "memberFragment");
        }
    }

    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main, fragment, tag).commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (integralFragment != null) {
            transaction.hide(integralFragment);
        }

        if (memberFragment != null) {
            transaction.hide(memberFragment);
        }

        transaction.show(fragment).commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_integral:
                invokeIntegralFragment();
                showFragment(integralFragment);
                break;
            case R.id.radio_account:
                invokeMemberFragment();
                showFragment(memberFragment);
                break;
        }
    }
}
