package com.durui.feat.software_interface.data.converter;

import androidx.room.TypeConverter;

import java.util.Date;

//定义这些类型转换器后，您就可以在实体和 DAO 中使用自定义类型，就像使用基元类型一样
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
