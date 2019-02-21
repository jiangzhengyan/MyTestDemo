package com.jingcaiwang.mytestdemo.views.passwordinput;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.jingcaiwang.mytestdemo.R;


/**
 * 输入支付密码
 * @author jiang_yan
 */
public class PopEnterPassword extends PopupWindow {

    private PasswordView pwdView;

    private View mMenuView;

    private Activity mContext;

    public PopEnterPassword(final Activity context) {

        super(context);

        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.pop_enter_password, null);

        pwdView = (PasswordView) mMenuView.findViewById(R.id.pwd_view);

        //添加密码输入完成的响应
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String password) {


                if (null != passwordInputFinish) {
                    passwordInputFinish.inputFinish(password);
                }
               // dismiss();
            }
        });

        // 监听X关闭按钮
        pwdView.getImgCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 监听键盘上方的返回
        pwdView.getVirtualKeyboardView().getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 监听忘记密码按钮
        pwdView.getTvForgetPwd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // context.startActivity(new Intent(context, ChangePassWordUI.class));
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_add_ainm);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x66000000);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    /**
     * 输入完成把密码数据回调到当前页面
     */
    private OnPasswordInputFinish passwordInputFinish;

    public void setOnPasswordInputFinish(OnPasswordInputFinish passwordInputFinish) {
        this.passwordInputFinish = passwordInputFinish;
    }

}
