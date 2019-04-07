package com.nexuslink.alphrye.ui.fragment;

import android.view.View;

import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : alphrye
 *    time   : 2018/12/28
 *    desc   : 探索页面
 */
public class ExploreFragment extends MyLazyFragment implements SimpleAdapter.OnItemClickListener {

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
        List<SimpleModel> models = new ArrayList<>();
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        models.add(new FeedModel());
        new SimpleAdapter.Builder(getContext())
                .recyclerView(R.id.v_recycler)
                .data(models)
                .drag(false)
                .itemClickListener(this)
                .build();
    }

    @Override
    protected void initData() {

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
