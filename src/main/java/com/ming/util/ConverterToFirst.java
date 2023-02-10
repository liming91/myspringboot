package com.ming.util;


import cn.hutool.core.util.StrUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @description: 文字取首字母大写
 * @author: hq
 * @create: 2021-03-17 13:49
 **/

public class ConverterToFirst {


    /**
     * 文字首字母大写生成字符串
     *
     * @param name
     * @return
     */
    public static String getUpperStr(String name) {
        if (StrUtil.isNotBlank(name)) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                String characters = String.valueOf(name.charAt(i));
                str.append(converterToFirstSpell(characters).toUpperCase().charAt(0));
            }
            return str.toString();
        }
        return "";
    }


    public static String converterToFirstSpell(String str) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = str.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char characters : nameChar) {
            String string = String.valueOf(characters);
            if (string.matches("[\\u4e00-\\u9fa5]")) {
                try {
                    String[] mPinyinArray = PinyinHelper.toHanyuPinyinStringArray(characters, defaultFormat);
                    pinyinName.append(mPinyinArray[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(characters);
            }
        }
        return pinyinName.toString();
    }


    /**
     * steam中  根据指定字段去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }



    public static void main(String[] args) {
        String str = "电表AL21";
        System.out.println(getUpperStr(str).toLowerCase());
        System.out.println(converterToFirstSpell(str).toLowerCase());
    }

}

