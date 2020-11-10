package com.keegan.commondemo.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.keegan.common.util.CityUtils;

import java.util.Date;
import java.util.List;

public class PickerSelectTools {

    interface TimePickerListeren {
        void onTimeSelect(Date date, View v);
    }

    interface OptionsPickerListeren {
        void onOptionsSelectCallBack(int options1);
    }

    interface OptionsCityPickerListeren {
        void onOptionsCitySelect(String options1, String options2, String options3);
    }

    /**
     * 时间选择器
     * @param isShowHour
     * @param listeren
     */
    public static void showTimePickerView(Context context,boolean isShowHour, TimePickerListeren listeren) {
        TimePickerView pvTime = new TimePickerBuilder(context, (date, v) -> listeren.onTimeSelect(date, v))
                .setType(new boolean[]{true, true, true, isShowHour, isShowHour, false})
                .setContentTextSize(18)
                .setTitleBgColor(Color.parseColor("#ffffff"))
                .setSubmitColor(Color.parseColor("#4795ED"))
                .setCancelColor(Color.parseColor("#999999"))
                .setDividerColor(Color.parseColor("#4795ED"))
                .setTextColorCenter(Color.parseColor("#FCB802"))
                .setTextColorOut(Color.parseColor("#999999"))
                .setSubCalSize(16)
                .build();
        pvTime.show(true);
    }

    /**
     * 单选
     * @param optionsItems
     */
    public static void showOptionsPickerView(Context context,List<String> optionsItems,OptionsPickerListeren listeren) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> listeren.onOptionsSelectCallBack(options1)).setContentTextSize(18)
                .setTitleBgColor(Color.parseColor("#ffffff"))
                .setSubmitColor(Color.parseColor("#4795ED"))
                .setCancelColor(Color.parseColor("#999999"))
                .setDividerColor(Color.parseColor("#4795ED"))
                .setSubCalSize(16)
                .setTextColorCenter(Color.parseColor("#FCB802"))
                .setTextColorOut(Color.parseColor("#999999"))
                .build();
        pvOptions.setPicker(optionsItems);
        pvOptions.show();
    }




    public static void showBottomCitySelectView(Context context,OptionsCityPickerListeren listeren) {
        if (CityUtils.isLoaded) {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
                //返回的分别是三个级别的选中位置
                String opt1tx = CityUtils.options1Items.size() > 0 ?
                        CityUtils.options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = CityUtils.options2Items.size() > 0
                        && CityUtils.options2Items.get(options1).size() > 0 ?
                        CityUtils.options2Items.get(options1).get(options2) : "";

                String opt3tx = CityUtils.options2Items.size() > 0
                        && CityUtils.options3Items.get(options1).size() > 0
                        && CityUtils.options3Items.get(options1).get(options2).size() > 0 ?
                        CityUtils.options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;

                listeren.onOptionsCitySelect(opt1tx, opt2tx, opt3tx);
            })
                    .setContentTextSize(18)
                    .setTitleBgColor(Color.parseColor("#ffffff"))
                    .setSubmitColor(Color.parseColor("#4795ED"))
                    .setCancelColor(Color.parseColor("#999999"))
                    .setDividerColor(Color.parseColor("#4795ED"))
                    .setSubCalSize(16)
                    .setTextColorCenter(Color.parseColor("#FCB802"))
                    .setTextColorOut(Color.parseColor("#999999"))
                    .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
            pvOptions.setPicker(CityUtils.options1Items, CityUtils.options2Items, CityUtils.options3Items);//三级选择器
            pvOptions.show();
        }
    }



}
