package com.gsc.shopcart.utils;

import lombok.extern.log4j.Log4j;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;


@Log4j
public class ShopCartUtils {

    public static boolean isProductInPromotion(LocalDate dtPromoStartLc, LocalDate dtPromoEndLc) {
        Date dtPromoStart = null;
        Date dtPromoEnd = null;

        try {
            dtPromoStart = Date.valueOf(dtPromoStartLc);
        }  catch (Exception e) {
            log.error("Error parsing date ", e);
        }

        try {
            dtPromoEnd = Date.valueOf(dtPromoEndLc);
        }  catch (Exception e) {
            log.error("Error parsing date ", e);
        }


        boolean isProductInPromotion = false;
        Calendar calNow = Calendar.getInstance();
        if (dtPromoStart != null && dtPromoEnd != null) {
            Calendar calPromoStart = Calendar.getInstance();
            Calendar calPromoEnd = Calendar.getInstance();
            calPromoStart.setTime(dtPromoStart);
            calPromoEnd.setTime(dtPromoEnd);
            // if (calNow.compareTo(calPromoStart)>=0 && calNow.compareTo(calPromoEnd)<=0)
            if (calNow.after(calPromoStart) && calNow.before(calPromoEnd))
                isProductInPromotion = true;
            // if (calNow.compareTo(calPromoEnd)>=0 && ShopCartUtils.isSameDay(calPromoStart, calPromoEnd))
            if (calNow.after(calPromoEnd) && ShopCartUtils.isSameDay(calPromoStart, calPromoEnd))
                isProductInPromotion = false;
            if (ShopCartUtils.isSameDay(calNow, calPromoEnd))
                isProductInPromotion = true;
        } else if (dtPromoStart != null) {
            Calendar calPromoStart = Calendar.getInstance();
            calPromoStart.setTime(dtPromoStart);
            // if (calNow.compareTo(calPromoStart)>=0)
            if (calNow.after(calPromoStart))
                isProductInPromotion = true;
        } else if (dtPromoEnd != null) {
            Calendar calPromoEnd = Calendar.getInstance();
            calPromoEnd.setTime(dtPromoEnd);
            // if (calNow.compareTo(calPromoEnd)<=0)
            if (calNow.before(calPromoEnd))
                isProductInPromotion = true;
        }
        return isProductInPromotion;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("Os calend�rios n�o podem ser nulos");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
