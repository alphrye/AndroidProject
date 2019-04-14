package com.nexuslink.alphrye.ui.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
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

    @BindView(R.id.v_recycler)
    RecyclerView mRecyclerView;

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
                .recyclerView(mRecyclerView)
                .drag(false)
                .itemClickListener(this)
                .itemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        view.setBottom(20);
                    }

                    @Override
                    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.onDraw(c, parent, state);
                    }
                })
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
