package com.nexuslink.alphrye.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nexuslink.alphrye.common.CommonConstance;
import com.nexuslink.alphrye.common.MyLazyFragment;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.helper.SPUtil;

import butterknife.BindView;

/**
 * @author yuanrui
 * @date 2019/5/15
 */
public class SettingFragment extends MyLazyFragment {

    @BindView(R.id.switch_auto_flash)
    Switch mSwithcAutoflash;

    @BindView(R.id.switch_speed)
    Switch mSwitchSpeed;

    @BindView(R.id.switch_test)
    Switch mSwitchTest;

    @BindView(R.id.ll_phone)
    LinearLayout mLlPhone;

    @BindView(R.id.phone_detail)
    TextView mPhoneDetail;

    @BindView(R.id.ll_light)
    LinearLayout mLlLight;

    @BindView(R.id.light_detail)
    TextView mLightDetail;

    @BindView(R.id.ll_speed_max)
    LinearLayout mLLSpeedMax;

    @BindView(R.id.speed_detail)
    TextView mSpeedDetail;

    private boolean isAuto;

    private boolean isSpeedOn;

    private boolean isTest;

    private String mPhone;

    private String mMaxSpeed;

    private String mLight;

    public static MyLazyFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_setting_title;
    }

    @Override
    protected void initView() {
        isAuto = SPUtil.getBoolean(CommonConstance.SP_STATUS_FLASH, false);
        mSwithcAutoflash.setChecked(isAuto);

        mSwithcAutoflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAuto = !isAuto;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_FLASH, isAuto);
            }
        });

        isSpeedOn = SPUtil.getBoolean(CommonConstance.SP_STATUS_SPEED, true);
        mSwitchSpeed.setChecked(isSpeedOn);

        mSwitchSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSpeedOn = !isSpeedOn;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_SPEED, isSpeedOn);
            }
        });

        isTest = SPUtil.getBoolean(CommonConstance.SP_STATUS_TEST, false);
        mSwitchTest.setChecked(isTest);

        mSwitchTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTest = !isTest;
                SPUtil.putBoolean(CommonConstance.SP_STATUS_TEST, isTest);
            }
        });

        mPhone = SPUtil.getString(CommonConstance.SP_STATUS_PHONE);

        if (TextUtils.isEmpty(mPhone)) {
            mPhoneDetail.setText("亲友提供骑行监控");
        } else {
            mPhoneDetail.setText(mPhone);
        }

        mLlPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(getContext());
                mPhone = SPUtil.getString(CommonConstance.SP_STATUS_PHONE);
                inputServer.setText(mPhone);
                inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("亲情号码").setView(inputServer);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String phone = inputServer.getText().toString();
                        if (TextUtils.isEmpty(phone)) {
                            mPhone = SPUtil.getString(CommonConstance.SP_STATUS_PHONE);

                            if (TextUtils.isEmpty(mPhone)) {
                                mPhoneDetail.setText("亲友提供骑行监控");
                            } else {
                                mPhoneDetail.setText(mPhone);
                            }                            Toast.makeText(getContext(),"号码设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isNumEnough = phone.length() == 11;
                        if (!TextUtils.isDigitsOnly(phone) || ! isNumEnough) {
                            mPhoneDetail.setText(mPhone);
                            Toast.makeText(getContext(),"号码设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mPhoneDetail.setText(phone);
                        SPUtil.putString(CommonConstance.SP_STATUS_PHONE, phone);
                        Toast.makeText(getContext(),"号码设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        mMaxSpeed = SPUtil.getString(CommonConstance.SP_STATUS_SPEED_MAX);

        if (TextUtils.isEmpty(mMaxSpeed)) {
            mSpeedDetail.setText("最大速度");
        } else {
            mSpeedDetail.setText(mMaxSpeed);
        }

        mLLSpeedMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(getContext());
                mMaxSpeed = SPUtil.getString(CommonConstance.SP_STATUS_SPEED_MAX);
                inputServer.setText(mMaxSpeed);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("超速提示").setView(inputServer);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String maxSpeed = inputServer.getText().toString();
                        if (TextUtils.isEmpty(maxSpeed)) {
                            mMaxSpeed = SPUtil.getString(CommonConstance.SP_STATUS_SPEED_MAX);

                            if (TextUtils.isEmpty(mMaxSpeed)) {
                                mSpeedDetail.setText("最大速度");
                            } else {
                                mSpeedDetail.setText(mMaxSpeed);
                            }
                            Toast.makeText(getContext(),"速度设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isDigitsOnly(maxSpeed)) {
                            mSpeedDetail.setText(mPhone);
                            Toast.makeText(getContext(),"速度设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mSpeedDetail.setText(maxSpeed);
                        SPUtil.putString(CommonConstance.SP_STATUS_SPEED_MAX, maxSpeed);
                        Toast.makeText(getContext(),"速度设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        mLight = SPUtil.getString(CommonConstance.SP_STATUS_LIGHT);

        if (TextUtils.isEmpty(mLight)) {
            mLightDetail.setText("设置亮度阀值");
        } else {
            mLightDetail.setText(mLight);
        }

        mLlLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(getContext());
                mLight = SPUtil.getString(CommonConstance.SP_STATUS_LIGHT);
                inputServer.setText(mLight);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("亮度阀值").setView(inputServer);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String light = inputServer.getText().toString();
                        if (TextUtils.isEmpty(light)) {
                            mLight = SPUtil.getString(CommonConstance.SP_STATUS_LIGHT);

                            if (TextUtils.isEmpty(mLight)) {
                                mLightDetail.setText("设置亮度阀值");
                            } else {
                                mLightDetail.setText(mLight);
                            }
                            Toast.makeText(getContext(),"亮度阀值设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isDigitsOnly(light)) {
                            mLightDetail.setText(light);
                            Toast.makeText(getContext(),"亮度阀值设置失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mLightDetail.setText(light);
                        SPUtil.putString(CommonConstance.SP_STATUS_LIGHT, light);
                        Toast.makeText(getContext(),"亮度阀值设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
