package com.nexuslink.alphrye.ui.activity;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.EditTextInputHelper;
import com.hjq.widget.CountdownView;
import com.nexuslink.alphrye.net.bean.CommonNetBean;
import com.nexuslink.alphrye.net.wrapper.RetrofitWrapper;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 注册界面
 */
public class RegisterActivity extends MyActivity
        implements View.OnClickListener {

    public static final String TAG = "RegisterActivity";

    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
    @BindView(R.id.cv_register_countdown)
    CountdownView mCountdownView;

//    @BindView(R.id.et_register_code)
//    EditText mCodeView;

    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_register_password2)
    EditText mPasswordView2;

    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    @BindView(R.id.tv_back)
    TextView mTvBack;

    @BindView(R.id.v_close)
    ImageView mIvClose;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_register_title;
    }

    @Override
    protected void initView() {
        mCountdownView.setOnClickListener(this);
        mCommitView.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mIvClose.setOnClickListener(this);

        mEditTextInputHelper = new EditTextInputHelper(mCommitView);
        mEditTextInputHelper.addViews(mPhoneView, mPasswordView1, mPasswordView2);
    }

    @Override
    protected void initData() {
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finishResult(RESULT_OK);
//            }
//        }, 2000);
    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCountdownView) { //获取验证码

            if (mPhoneView.getText().toString().length() != 11) {
                // 重置验证码倒计时控件
                mCountdownView.resetState();
                toast(getResources().getString(R.string.phone_input_error));
                return;
            }

            toast(getResources().getString(R.string.countdown_code_send_succeed));

        }else if (v == mCommitView) { //提交注册

            if (mPhoneView.getText().toString().length() != 11) {
                toast(getResources().getString(R.string.phone_input_error));
                return;
            }

            if (!mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString())) {
                toast(getResources().getString(R.string.two_password_input_error));
                return;
            }

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("注册中...");
            dialog.show();
            final android.os.Handler handler = new android.os.Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    dialog.dismiss();
//                    finish();
//                    toast("注册成功，赶快去登录吧");
//                }
//            }, 1000);
            RetrofitWrapper.getInstance()
                    .getCommonCall()
                    .register(mPhoneView.getText().toString(), mPasswordView1.getText().toString())
                    .enqueue(new Callback<CommonNetBean>() {
                        @Override
                        public void onResponse(Call<CommonNetBean> call, Response<CommonNetBean> response) {
                            dialog.dismiss();
                            if (!response.isSuccessful()) {
                                toast("注册失败");
                                return;
                            }
                            CommonNetBean commonNetBean = response.body();
                            if (commonNetBean == null) {
                                toast("注册失败");
                                return;
                            }
                            if (!"ok".equals(commonNetBean.status)) {
                                toast("注册失败");
                                return;
                            }
                            toast("注册成功");
                            Log.d(TAG, "onResponse: " + commonNetBean.prompts);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<CommonNetBean> call, Throwable t) {
                            dialog.dismiss();
                            toast("注册失败");
                        }
                    });
        } else if (v == mTvBack || v == mIvClose) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }
}