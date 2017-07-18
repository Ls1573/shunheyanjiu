package com.shyj.shop.shunheapp.member;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.shyj.shop.shunheapp.base.BaseFragment;
import com.shyj.shop.shunheapp.domain.Member;
import com.shyj.shop.shunheapp.utils.AlertUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Ls
 * @description 查询所有会员
 * @date 2017/7/17
 * @package com.shyj.shop.shunheapp
 */

public class MemberFragment extends BaseFragment {

    private TextView tv_title;
    private ImageView iv_back;
    private PullToRefreshListView member_list;
    private ListAdapter adapter;
    private TextView textView;

    @Override
    public int setLayout() {
        return R.layout.fragment_member;
    }

    @Override
    public void initView(View view) {
        tv_title = findView(R.id.tv_title);
        iv_back = findView(R.id.iv_back);
        member_list = findView(R.id.member_list);

        iv_back.setVisibility(View.INVISIBLE);
        tv_title.setText("顺和会员");
    }

    @Override
    public void initData() {
        adapter = new ListAdapter(getActivity());
        member_list.getRefreshableView().setAdapter(adapter);
        member_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(true, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(false, false);
            }
        });

        textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("暂无数据");

        getList(true, true);

    }

    private void getList(final boolean isPullDown, final boolean isFirst) {
        showLoadingDialog();
        BmobQuery<Member> query = new BmobQuery<Member>();
        if (isPullDown) {
            query.setSkip(0);
        } else {
            // 跳过之前页数并去掉重复数据
            query.setSkip(adapter.getList().size());
        }

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(25);
        //执行查询方法
        query.findObjects(new FindListener<Member>() {
            @Override
            public void done(List<Member> object, BmobException e) {
                hideLoadingDialog();

                if (e == null) {
                    if (object.size() > 0) {
                        if (isPullDown) {
                            // 当是下拉刷新操作时, 重新添加
                            adapter.setList(object);
                        } else {
                            adapter.addBean(object);
                        }
                        member_list.setPullLabel("上拉刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                        member_list.setReleaseLabel("放开以刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                        member_list.setRefreshingLabel("正在载入", PullToRefreshBase.Mode.PULL_FROM_END);

                    } else {
                        member_list.setEmptyView(textView);
                        member_list.setPullLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                        member_list.setReleaseLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                        member_list.setRefreshingLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                        AlertUtil.t(App.context, "没有数据了");
                        if (isFirst) {
                            adapter.setList(new ArrayList<Member>());
                        }
                    }

                    member_list.onRefreshComplete();
                    adapter.notifyDataSetChanged();

                } else {
                    AlertUtil.t(App.context, "查询失败");
                    member_list.onRefreshComplete();
                }

            }
        });
    }


    class ListAdapter extends BaseAdapter {

        private List<Member> list = new ArrayList<>();
        private Context context;

        public ListAdapter(Context context) {

            this.context = context;
        }

        public void addBean(List<Member> bean) {
            list.addAll(bean);
        }

        public void removeAll() {
            if (list != null && list.size() > 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
            }
        }

        public List<Member> getList() {
            return list;
        }

        public void setList(List<Member> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Member getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            RecordListViewHolder viewHolder;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent,
                        false);
                viewHolder = new RecordListViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RecordListViewHolder) convertView.getTag();
            }

            // list设置数据
            viewHolder.item_mem_tv.setText(list.get(position).getTeleNum());
            viewHolder.item_integral_tv.setText(list.get(position).getIntegral());
            viewHolder.item_birth_tv.setText(list.get(position).getBirth());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), IntegralRecordActivity.class);
                    intent.putExtra("objId", list.get(position).getObjectId());
                    intent.putExtra("member", (Parcelable) list.get(position));
                    startActivity(intent);
                }
            });

            return convertView;
        }

        class RecordListViewHolder {

            TextView item_mem_tv;// 手机号
            TextView item_integral_tv;// 积分
            TextView item_birth_tv;// 生日

            public RecordListViewHolder(View itemView) {

                item_mem_tv = (TextView) itemView.findViewById(R.id.item_mem_tv);
                item_integral_tv = (TextView) itemView.findViewById(R.id.item_integral_tv);
                item_birth_tv = (TextView) itemView.findViewById(R.id.item_birth_tv);

            }

        }

    }

}
