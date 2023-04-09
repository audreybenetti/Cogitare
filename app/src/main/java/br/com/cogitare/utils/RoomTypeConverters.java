package br.com.cogitare.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.Objects;

import br.com.cogitare.model.GeneroEnum;

public class RoomTypeConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static GeneroEnum fromStringToEnum(String value) {
        return Objects.equals(value, GeneroEnum.FEMININO.toString()) ? GeneroEnum.FEMININO : GeneroEnum.MASCULINO;
    }

    @TypeConverter
    public static String enumToString(GeneroEnum generoEnum) {
        return generoEnum == GeneroEnum.FEMININO ? GeneroEnum.FEMININO.toString() : GeneroEnum.MASCULINO.toString();
    }


}