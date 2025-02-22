package util;

import com.dasad.empresa.util.DataUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataUtilTest {

    @Test
    void testConvertStringToLocalDate() {
        String dateStr = "25/12/2020";
        LocalDate expectedDate = LocalDate.of(2020, 12, 25);

        LocalDate result = DataUtil.convertStringToLocalDate(dateStr);

        assertNotNull(result);
        assertEquals(expectedDate, result);
    }

//    @Test
//    void testConvertStringToLocalDateWithNull() {
//        String dateStr = null;
//
//        LocalDate result = DataUtil.convertStringToLocalDate(dateStr);
//
//        assertNull(result);
//    }

//    @Test
//    void testConvertStringToLocalDateWithEmptyString() {
//        String dateStr = "";
//
//        LocalDate result = DataUtil.convertStringToLocalDate(dateStr);
//
//        assertNull(result);
//    }

    @Test
    void testConvertLocalDateToString() {
        LocalDate date = LocalDate.of(2020, 12, 25);
        String expectedDateStr = "25/12/2020";

        String result = DataUtil.convertLocalDateToString(date);

        assertNotNull(result);
        assertEquals(expectedDateStr, result);
    }

    @Test
    void testConvertLocalDateToStringWithNull() {
        LocalDate date = null;

        String result = DataUtil.convertLocalDateToString(date);

        assertNull(result);
    }
}