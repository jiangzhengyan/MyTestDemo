package com.jingcaiwang.mytestdemo.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;
import android.widget.PopupMenu;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_5_3 extends AppCompatActivity {


    @Bind(R.id.btn_popmenu)
    Button mBtnPopmenu;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_3);
        ButterKnife.bind(this);
        actionBar = getActionBar();


    }


    /**
     * 创建按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu sbm = menu.addSubMenu("字体大小");
        sbm.setIcon(R.mipmap.icon_play);
        sbm.setHeaderIcon(R.mipmap.ic_launcher);
        sbm.setHeaderTitle("请选择文字大小");
        sbm.add(0, 12, 0, "10号字体");
        sbm.add(0, 12, 0, "10号字体");
        sbm.add(0, 12, 0, "10号字体");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 按钮的响应
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ToastUtil.toastLongCenter(getApplicationContext(), "item");


        return true;
    }

    @OnClick(R.id.btn_popmenu)
    public void onViewClicked() {
        PopupMenu popupMenu = new PopupMenu(this, mBtnPopmenu);
        getMenuInflater().inflate(R.menu.navigation,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.toastLongCenter(Activity_5_3.this, "[[[");
                actionBar.show();
                return false;
            }
        });
        popupMenu.show();

    }
}

