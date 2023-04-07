package br.com.cogitare.utils;

import android.arch.persistence.room.TypeConverter;
import android.os.Build;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import br.com.cogitare.model.GeneroEnum;

public class RoomTypeConverters {

    @TypeConverter
    public static LocalDate fromString(String value) {
        return value == null ? null : toLocalDate(value);
    }

    @TypeConverter
    public static String dataToString(LocalDate date) {
        return date == null ? null : date.toString();
    }

    private static LocalDate toLocalDate(String data) {
        DateTimeFormatter parser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDate.from(LocalDate.parse(data, parser));
        }
        return null;
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