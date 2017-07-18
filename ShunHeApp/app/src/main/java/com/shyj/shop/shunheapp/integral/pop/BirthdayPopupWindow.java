package com.shyj.shop.shunheapp.integral.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shyj.shop.shunheapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BirthdayPopupWindow extends PopupWindow {
	public String result;// 日期的选择结果
	private TextView birthday_disimess, birthday_submit;// 取消确定
	private View mMenuView;

	public BirthdayPopupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_birthday, null);
		birthday_disimess = (TextView) mMenuView.findViewById(R.id.birthday_disimess);
		birthday_submit = (TextView) mMenuView.findViewById(R.id.birthday_submit);
		CustomDatePicker cdp = (CustomDatePicker) mMenuView.findViewById(R.id.cdp);
		cdp.addChangingListener(new CustomDatePicker.ChangingListener() {

			public void onChange(Calendar c) {
				// 获取日历的结果
				result = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
			}
		});
		birthday_disimess.setOnClickListener(itemsOnClick);
		birthday_submit.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.anim.bottom_dialog_enter);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.birthday_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
