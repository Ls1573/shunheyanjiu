package com.shyj.shop.shunheapp.member;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shyj.shop.shunheapp.R;
import com.shyj.shop.shunheapp.base.App;
import com.shyj.shop.shunheapp.base.BaseActivity;
import com.shyj.shop.shunheapp.domain.Integral;
import com.shyj.shop.shunheapp.domain.Member;
import com.shyj.shop.shunheapp.utils.AlertUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Ls
 * @description 积分记录
 * @date 2017/7/18
 * @package com.shyj.shop.shunheapp.menber
 */

public class IntegralRecordActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView pullToRefreshListView;
    private TextView integral_usable_tv;
    private IntegralListAdapter adapter;

    private TextView textView;
    private TextView tv_title;
    private ImageView iv_back;

    private String objId;
    private Member member;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_integral_records);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.integral_list);
        integral_usable_tv = (TextView) findViewById(R.id.integral_usable_tv);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        member = getIntent().getParcelableExtra("member");
        objId = getIntent().getStringExtra("objId");

        tv_title.setText(member.getTeleNum());
        integral_usable_tv.setText(member.getIntegral());
        adapter = new IntegralListAdapter(this);
        pullToRefreshListView.getRefreshableView().setAdapter(adapter);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(true, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(false, false);
            }
        });

        textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText("暂无数据");

        getList(true, true);

    }

    private void getList(final boolean isPullDown, final boolean isFirst) {
        showLoadingDialog();
        BmobQuery<Integral> query = new BmobQuery<>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        if (isPullDown) {
            query.setSkip(0);
        } else {
            // 跳过之前页数并去掉重复数据
            query.setSkip(adapter.getList().size());
        }

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(25);

        Member member = new Member();
        member.setObjectId(objId);
        query.addWhereEqualTo("member", new BmobPointer(member));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.findObjects(
                new FindListener<Integral>() {
                    @Override
                    public void done(List<Integral> object, BmobException e) {
                        hideLoadingDialog();

                        if (e == null) {
                            if (object.size() > 0) {
                                if (isPullDown) {
                                    // 当是下拉刷新操作时, 重新添加
                                    adapter.setList(object);
                                } else {
                                    adapter.addBean(object);
                                }
                                pullToRefreshListView.setPullLabel("上拉刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                                pullToRefreshListView.setReleaseLabel("放开以刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                                pullToRefreshListView.setRefreshingLabel("正在载入", PullToRefreshBase.Mode.PULL_FROM_END);

                            } else {
                                pullToRefreshListView.setEmptyView(textView);
                                pullToRefreshListView.setPullLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                                pullToRefreshListView.setReleaseLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                                pullToRefreshListView.setRefreshingLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                                AlertUtil.t(App.context, "没有数据了");
                                if (isFirst) {
                                    adapter.setList(new ArrayList<Integral>());
                                }
                            }

                            pullToRefreshListView.onRefreshComplete();
                            adapter.notifyDataSetChanged();

                        } else {
                            AlertUtil.t(App.context, "查询失败");
                            pullToRefreshListView.onRefreshComplete();
                        }

                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }


    class IntegralListAdapter extends BaseAdapter {

        private List<Integral> list = new ArrayList<>();
        private Context context;

        public IntegralListAdapter(Context context) {

            this.context = context;
        }

        public void addBean(List<Integral> bean) {
            list.addAll(bean);
        }

        public void removeAll() {
            if (list != null && list.size() > 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
            }
        }

        public List<Integral> getList() {
            return list;
        }

        public void setList(List<Integral> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Integral getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IntegralListViewHolder viewHolder;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_inegral, parent,
                        false);
                viewHolder = new IntegralListViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (IntegralListViewHolder) convertView.getTag();
            }

            // list设置数据
            viewHolder.item_integral_data_tv.setText(list.get(position).getUpdatedAt());
            viewHolder.item_integral_num_tv.setText(list.get(position).getIntegralDetail());
            if (list.get(position).getIntegralDetail().contains("+")) {
                viewHolder.item_integral_title_tv.setText("消费得积分");
            } else {
                viewHolder.item_integral_title_tv.setText("积分兑换");
            }

            return convertView;
        }

        class IntegralListViewHolder {

            private final TextView item_integral_title_tv;
            private final TextView item_integral_num_tv;
            private final TextView item_integral_data_tv;

            public IntegralListViewHolder(View itemView) {
                item_integral_title_tv = (TextView) itemView.findViewById(R.id.item_integral_title_tv);
                item_integral_data_tv = (TextView) itemView.findViewById(R.id.item_integral_data_tv);
                item_integral_num_tv = (TextView) itemView.findViewById(R.id.item_integral_num_tv);
            }

        }

    }

}
