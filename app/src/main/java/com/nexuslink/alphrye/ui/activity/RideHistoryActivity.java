package com.nexuslink.alphrye.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;
import com.nexuslink.alphrye.model.RideHistoryModel;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *    author : alphrye
 *    time   : 2018/12/29
 *    desc   : 骑行历史页面
 */
public class RideHistoryActivity extends MyActivity implements SimpleAdapter.OnItemClickListener {
    private SimpleAdapter mSimpleAdapter;

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ride_history;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_history_title;
    }

    @Override
    protected void initView() {
        //测试数据
        mSimpleAdapter = new SimpleAdapter.Builder(this)
                .recyclerView(mRecyclerView)
                .itemClickListener(this)
                .build();
    }

    @Override
    protected void initData() {
        // TODO: 2019/4/8 SimpleAdapter与Retrofit的请求封装
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance();
        wrapper.enqueue(wrapper.getCommonCall().requestMyRideHistory(), new RetrofitWrapper.CommonCallBack<List<RideHistoryModel>>() {
            @Override
            public void onSuccess(List<RideHistoryModel> response) {
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
