package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.SearchTipModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends MyActivity implements Inputtips.InputtipsListener, SimpleAdapter.OnItemClickListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;

    private SimpleAdapter mSimpleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        mSimpleAdapter = new SimpleAdapter.Builder(this)
                .recyclerView(R.id.v_recycler)
                .itemClickListener(this)
                .build();
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputtipsQuery inputquery = new InputtipsQuery(String.valueOf(s), "");
                inputquery.setCityLimit(true);
                Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
                inputTips.setInputtipsListener(SearchActivity.this);
                inputTips.requestInputtipsAsyn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<SimpleModel> simpleModels = new ArrayList<>();
        for (Tip tip : list) {
            simpleModels.add(new SearchTipModel(tip));
        }
        mSimpleAdapter.setData(simpleModels);
    }

    @Override
    public void onItemClick(View v, SimpleModel model, int position) {
        if (model instanceof SearchTipModel) {
            Intent intent = new Intent();
            intent.putExtra("search_tip", ((SearchTipModel) model).mTip);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
