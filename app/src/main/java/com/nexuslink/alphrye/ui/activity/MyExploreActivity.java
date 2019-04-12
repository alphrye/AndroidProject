package com.nexuslink.alphrye.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *    author : alphrye
 *    time   : 2018/12/29
 *    desc   : 我的动态页面
 */
public class MyExploreActivity extends MyActivity implements SimpleAdapter.OnItemClickListener {

    private SimpleAdapter mSimpleAdapter;

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_explore;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_my_explore_title;
    }

    @Override
    protected void initView() {
        mSimpleAdapter = new SimpleAdapter.Builder(this)
                .recyclerView(mRecyclerView)
                .itemClickListener(this)
                .build();
    }

    @Override
    protected void initData() {
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance();
        wrapper.enqueue(wrapper.getCommonCall().requestMyFeeds(), new RetrofitWrapper.CommonCallBack<List<FeedModel>>() {
            @Override
            public void onSuccess(List<FeedModel> response) {
                if (response == null || response.isEmpty()) {
                    return;
                }
                mSimpleAdapter.appendData(new ArrayList<SimpleModel>(response));
            }

            @Override
            public void onFail(String errorTips) {

            }
        });
    }

    @Override
    public void onItemClick(View v, SimpleModel model, int position) {

    }
}
