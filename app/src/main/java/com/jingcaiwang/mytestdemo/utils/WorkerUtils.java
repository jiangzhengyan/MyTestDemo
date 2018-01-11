package com.jingcaiwang.mytestdemo.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jiang_yan on 2017/9/12.
 * 有关员工的工具类
 */

public class WorkerUtils {

    /**
     * 获取当天工单的 a参数的值
     *
     设备巡检 get_my_patrol_area_today,
     物业巡更 get_my_inspect_sheet_today
     品质管理 get_my_quality_sheet_today
     *
     * @return a参数的值
     */
    /**
     * 获取当天工单的 a参数的值
     *
     * @param nameSheet   设备巡检 get_my_patrol_sheet_today,
     *                    物业巡更 get_my_inspect_sheet_today
     *                    品质管理 get_my_quality_sheet_today
     * @param nameRecords 设备巡检 get_my_patrol_record_today,
     *                    物业巡更 get_my_inspect_record_today
     *                    品质管理 get_my_quality_record_today
     * @param nameTarget  设备巡检 get_my_patrol_target_today,
     *                    物业巡更 get_my_inspect_target_today
     *                    品质管理 get_my_quality_target_today
     * @param nameAreas   设备巡检 get_my_patrol_area_today,
     *                    物业巡更 get_my_inspect_area_today
     *                    品质管理 get_my_quality_area_today
     * @return
     */
    public static String getSheetJsonArray(String nameSheet, String nameRecords, String nameTarget, String nameAreas) {
        if (TextUtils.isEmpty(nameSheet) || TextUtils.isEmpty(nameRecords) ||
                TextUtils.isEmpty(nameTarget) || TextUtils.isEmpty(nameAreas))
            return "";
        try {
            org.json.JSONArray jarrArray = new org.json.JSONArray();

            org.json.JSONObject jsonObject_sheet = new org.json.JSONObject();
            jsonObject_sheet.put("alia", "sheets");
            jsonObject_sheet.put("args", new org.json.JSONObject());
            jsonObject_sheet.put("name", nameSheet);
            jarrArray.put(jsonObject_sheet);


            org.json.JSONObject jsonObject_records = new org.json.JSONObject();
            jsonObject_records.put("alia", "records");
            jsonObject_records.put("args", new org.json.JSONObject());
            jsonObject_records.put("name", nameRecords);
            jarrArray.put(jsonObject_records);

            org.json.JSONObject jsonObject_target = new org.json.JSONObject();
            jsonObject_target.put("alia", "target");
            jsonObject_target.put("args", new org.json.JSONObject());
            jsonObject_target.put("name", nameTarget);
            jarrArray.put(jsonObject_target);

            org.json.JSONObject jsonObject_areas = new org.json.JSONObject();
            jsonObject_areas.put("alia", "areas");
            jsonObject_areas.put("args", new org.json.JSONObject());
            jsonObject_areas.put("name", nameAreas);
            jarrArray.put(jsonObject_areas);

            return jarrArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 文本字数的监听
     *
     * @param et_content_marker 输入文本空间
     * @param tv_text_count     字数限制文本
     * @param mInputNumLimit    限制字数
     */
    public static void editContent(EditText et_content_marker, final TextView tv_text_count, final int mInputNumLimit) {
        if (et_content_marker == null || tv_text_count == null)
            return;
        et_content_marker.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputNumLimit)});
        //设置默认剩余字数
        tv_text_count.setText(0 + "/" + mInputNumLimit);
        et_content_marker.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_text_count.setText(s.length() + "/" + mInputNumLimit);
            }
        });

    }

}
