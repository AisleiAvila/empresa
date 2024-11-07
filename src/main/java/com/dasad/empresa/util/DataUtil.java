package com.dasad.empresa.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DataUtil {

    public static LocalDate convertStringToLocalDate(String strData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Optional.ofNullable(strData)
                .filter(data -> !data.trim().isEmpty())
                .map(data -> LocalDate.parse(data, formatter))
                .orElse(null);
    }


    public static String convertLocalDateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Optional.ofNullable(localDate)
                .filter(date -> !date.toString().trim().isEmpty())
                .map(date -> date.format(formatter))
                .orElse(null);
    }



}
