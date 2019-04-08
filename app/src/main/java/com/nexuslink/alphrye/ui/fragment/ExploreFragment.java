package com.nexuslink.alphrye.ui.fragment;

import android.view.View;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.api.CommonApiService;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;
import com.nexuslink.alphrye.net.bean.CommonNetBean;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 探索页面
 */
public class ExploreFragment extends MyLazyFragment implements SimpleAdapter.OnItemClickListener {

    private SimpleAdapter mSimpleAdapter;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_explore_title;
    }

    @Override
    protected void initView() {
        mSimpleAdapter = new SimpleAdapter.Builder(getContext())
                .recyclerView(R.id.v_recycler)
                .drag(false)
                .itemClickListener(this)
                .build();
    }

    @Override
    protected void initData() {
        // TODO: 2019/4/7 Map缓存，内存优化
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance();
        wrapper.enqueue(wrapper.getCommonCall().requestFeeds(), new RetrofitWrapper.CommonCallBack<List<FeedModel>>() {
            @Override
            public void onSuccess(List<FeedModel> response) {
                if (response == null ||
                        response.isEmpty()) {
                    return;
                }
                List<SimpleModel> simpleModels = new ArrayList<SimpleModel>(response);
                mSimpleAdapter.appendData(simpleModels);
            }

            @Override
            public void onFail(String errorTips) {

            }
        });
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onItemClick(View v, SimpleModel model, int position) {

    }
}
