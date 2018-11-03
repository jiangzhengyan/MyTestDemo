package com.jingcaiwang.mytestdemo.pop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jingcaiwang.mytestdemo.R;
import com.jingcaiwang.mytestdemo.views.loopview.LoopView;
import com.jingcaiwang.mytestdemo.views.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 本类的主要功能是 :  选择日期  2018年12月12日   搭配 pop_select_date  布局
 *
 * @author jiang_zheng_yan  2018/9/26 22:25
 */
public class PopSelectDate extends CommentPopUtils implements View.OnClickListener {

    private Button mSure;


    private String yearSelect;// 年
    private String MMSelect;//月
    private String ddSelect;//日

    private View ll_pop_industry;
    private LoopView loopView_yyyy;
    private LoopView loopView_mm;
    private LoopView loopView_dd;
    private TextView tv_title;

    public PopSelectDate(View v, Context context, int layout) {
        super(v, context, layout);
    }

    @Override
    public void initLayout(View v, Context context) {

        mSure = (Button) v.findViewById(R.id.btn_sure);
        ll_pop_industry = v.findViewById(R.id.ll_pop_industry);
        loopView_yyyy = (LoopView) v.findViewById(R.id.loopView_yyyy);
        loopView_mm = (LoopView) v.findViewById(R.id.loopView_mm);
        loopView_dd = (LoopView) v.findViewById(R.id.loopView_dd);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        mSure.setOnClickListener(this);
        ll_pop_industry.setOnClickListener(this);
        createData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_pop_industry) {
            dismiss();
        }
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }
    /**
     *  设置标题
     * @param title
     */
    public void setTitle(String title) {

        tv_title.setText(title);
    }

    /**
     * @return yyyy-MM-dd
     */
    public String getYearSelect() {
        return yearSelect;
    }

    /**
     * @return HH:mm
     */
    public String getMMSelect() {
        return MMSelect;
    }

    /**
     * @return HH:mm
     */
    public String getddSelect() {
        return ddSelect;
    }


    /***
     *  创建时间数据
     */
    private void createData() {
        final ArrayList<String> yyyy = new ArrayList<>();
        final ArrayList<String> MM = new ArrayList<>();
        final ArrayList<String> dd31 = new ArrayList<>();
        final ArrayList<String> dd30 = new ArrayList<>();
        final ArrayList<String> dd29 = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.clear();//避免继承当前系统的时间
        calendar.setTime(new Date());
        // calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
        int year = calendar.get(Calendar.YEAR);
        for (int i = year - 10; i < year + 1; i++) {
            yyyy.add(i + "年");
        }

        for (int i = 1; i <= 12; i++) {
            MM.add(i + "月");
        }
        for (int i = 1; i <= 31; i++) {
            dd31.add(i + "日");
        }
        for (int i = 1; i <= 30; i++) {
            dd30.add(i + "日");
        }
        for (int i = 1; i <= 29; i++) {
            dd29.add(i + "日");
        }

        setLoopView(loopView_yyyy, yyyy);
        setLoopView(loopView_mm, MM);
        //初始化位置

        //选择日期
        loopView_yyyy.setInitPosition(yyyy.size()-1);
        yearSelect = yyyy.get(yyyy.size()-1);

        loopView_yyyy.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                yearSelect = yyyy.get(index);
            }
        });
        initMonthLoopView(loopView_mm);
        initDayLoopView(loopView_dd);
        //选择
        MMSelect = MM.get(loopView_mm.getInitPosition());
        setDayData(dd31,dd30,dd29,MMSelect);
        loopView_mm.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                MMSelect = MM.get(index);
                setDayData(dd31,dd30,dd29,MMSelect);
            }
        });


    }

    private void setDayData(final ArrayList<String> dd31, final ArrayList<String> dd30, final ArrayList<String> dd29, final String mm) {

        int initPosition = loopView_dd.getInitPosition();
        if (mm.equals("1月")||mm.equals("3月")||mm.equals("10月")||mm.equals("5月")||mm.equals("7月")||mm.equals("8月")||mm.equals("12月"))
        {
            setLoopView(loopView_dd, dd31);
            initPosition=initPosition>30?30:initPosition;
            ddSelect = dd31.get(initPosition);
        }else if (mm.equals("4月")||mm.equals("6月")||mm.equals("9月")||mm.equals("11月")){
            setLoopView(loopView_dd, dd30);
            initPosition=initPosition>29?29:initPosition;
            ddSelect = dd30.get(initPosition);
        }else {
            setLoopView(loopView_dd, dd29);
            initPosition=initPosition>28?28:initPosition;
            ddSelect =dd29.get(initPosition);
        }




        loopView_dd.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

                if (mm.equals("1月")||mm.equals("3月")||mm.equals("10月")||mm.equals("5月")||mm.equals("7月")||mm.equals("8月")||mm.equals("12月"))
                {
                    ddSelect = dd31.get(index);
                    //setLoopView(loopView_dd, dd31);
                }else if (mm.equals("4月")||mm.equals("6月")||mm.equals("9月")||mm.equals("11月")){
                    ddSelect = dd30.get(index);
                   // setLoopView(loopView_dd, dd30);
                }else {
                    ddSelect =dd29.get(index);
                   // setLoopView(loopView_dd, dd29);
                }

            }
        });
    }

    private void initMonthLoopView(LoopView loopView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        loopView.setInitPosition(month);
    }

    private void initDayLoopView(LoopView loopView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        loopView.setInitPosition(day-1);


    }
    private void setLoopView(LoopView loopView, ArrayList<String> list) {
        loopView.setItems(list);
        loopView.setNotLoop();
        loopView.setTextSize(17);
    }


}