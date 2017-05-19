package net.mamian.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 * 日期计算（包括时区转换）
 *
 * @author mamian
 * @mail mamianskyma@aliyun.com
 * @date 2015-7-15 16:15:58
 * @copyright ©2015 马面 All Rights Reserved
 */
public class DateUtils {

    /**
     * rawOffset所在时区，今天0点所对应的服务器时间
     */
    public static Date getServerDateStartByClient(int rawOffset) {
        //此刻rawOffset所在时区的时间
        Date result = DateUtils.getDate(new Date().getTime(), rawOffset);
        return LocalDate.fromDateFields(result).toDate();
    }

    /**
     * rawOffset所在时区，今天0点在days天后所对应的服务器时间
     */
    public static Date getServerDateEndByClient(int rawOffset, int days) {
        //此刻rawOffset所在时区的时间
        Date result = DateUtils.getDate(new Date().getTime(), rawOffset);

        return LocalDate.fromDateFields(result).plusDays(days).toDate();
    }

    /**
     * 两段时间是否有交叉
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean isCrossHour(Date start1, Date end1, Date start2, Date end2) {
        if (LocalDate.fromDateFields(end1).isEqual(LocalDate.fromDateFields(start2))) {
            if (end1.getMinutes() == 0 && end1.getSeconds() == 0 && end1.getTime() % 1000 == 0) {//end1是整点
                if (start2.getHours() >= end1.getHours()) {
                    return false;
                }
            } else {//end1不是整点
                if (start2.getHours() >= end1.getHours() + 1) {
                    return false;
                }
            }
        }
        if (LocalDate.fromDateFields(start1).isEqual(LocalDate.fromDateFields(end2))) {
            if (end2.getMinutes() == 0 && end2.getSeconds() == 0 && end2.getTime() % 1000 == 0) {//end2是整点
                if (start1.getHours() >= end2.getHours()) {
                    return false;
                }
            } else {//end2不是整点
                if (start1.getHours() >= end2.getHours() + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取rawOffset1时区的hours1点，所对应的rawOffset2的整点
     *
     * @param hours1
     * @param rawOffset1
     * @param rawOffset2
     * @return
     */
    public static int getHoursByRawOffset(int hours1, int rawOffset1, int rawOffset2) {
        int result = (rawOffset2 - rawOffset1) / (60 * 60 * 1000) + hours1;
        result = result < 0 ? result + 24 : result;
        result = result > 24 ? result - 24 : result;
        return result;
    }

    /**
     * 将time转换为timeZone时区下的Date
     *
     * @param time
     * @param rawoffset
     * @return
     */
    public static Date getDate(long time, int rawoffset) {
        return new Date(time - TimeZone.getDefault().getRawOffset() + rawoffset);
    }

    /**
     * 求rawoffset时区的date所对应的时间戳(不考虑夏令时)
     *
     * @param date
     * @param rawoffset
     * @return
     */
    public static long getTime(Date date, int rawoffset) {
        return date.getTime() + (rawoffset - TimeZone.getDefault().getRawOffset());
    }

    /**
     * 根据rawoffset获取时区
     *
     * @param rawoffset
     * @return
     */
    private static TimeZone getTimeZone(int rawoffset) {
        String[] timeZoneIds = TimeZone.getAvailableIDs(rawoffset);
        if (timeZoneIds.length == 0) {
            return TimeZone.getDefault();
        }
        TimeZone result = TimeZone.getTimeZone(timeZoneIds[0]);
        return null == result ? TimeZone.getDefault() : result;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date str2Date(final String dateStr, final String dateFormat) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String date2Str(final Date date, final String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 是否是周末
     *
     * @param date
     * @return
     * */
    public static boolean isWeekend(LocalDate date){
        return date.dayOfWeek().get()>5;
    }

    /**
     * 是否为中国法定假日
     *
     * 元旦：阳历 1.1
     * 春节：农历 除夕~正月初六
     * 清明：阳历 清明当日
     * 五一：阳历 5.1
     * 端午：农历 5.5
     * 中秋：农历 8.15
     * 国庆：阳历 10.1~10.7
     *
     * @param date 阳历日期
     * @return
     * */
    public static boolean isCnHoliday(LocalDate date){
        switch (date.getMonthOfYear()){
            case 1://元旦
                if(1==date.getDayOfMonth()){
                    return true;
                }
                break;
            case 4://清明
                int year2 = date.getYear()%100;
                double day = (year2*0.2422+4.81)-(year2/4);
                int dayInt = (int)day;
                if(day-dayInt>=0.5){
                    ++dayInt;
                }
                if(dayInt == date.getDayOfMonth()){
                    return true;
                }
                break;
            case 5://五一
                if(1==date.getDayOfMonth()){
                    return true;
                }
                break;
            case 10://国庆
                if(date.getDayOfMonth()<8){
                    return true;
                }
                break;
        }

        LocalDate lunar = solar2lunar(date);
        switch (lunar.getMonthOfYear()){
            case 1://正月
                if(lunar.getDayOfMonth()<7){
                    return true;
                }
                break;
            case 8://中秋
                if(15==lunar.getDayOfMonth()){
                    return true;
                }
                break;
            case 5://端午
                if(5==lunar.getDayOfMonth()){
                    return true;
                }
                break;
            case 12://除夕
                if(30==lunar.getDayOfMonth() || 29==lunar.getDayOfMonth() && 1 == lunar.plusDays(1).getDayOfMonth()){
                    return true;
                }
                break;
            default:
                return false;
        }

        return false;
    }

    //支持农历1900年正月初一到2099年腊月三十之间的农历日期
    /**
     * 支持转换的最小农历年份
     */
    private static final int MIN_YEAR = 1900;

    /**
     * 支持转换的最大农历年份
     */
    private static final int MAX_YEAR = 2099;

    /**
     * 公历每月前的天数
     */
    private static final int DAYS_BEFORE_MONTH[] = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };

    /**
     * 用来表示1900年到2099年间农历年份的相关信息，共24位bit的16进制表示，其中：
     * 1. 前4位表示该年闰哪个月；
     * 2. 5-17位表示农历年份13个月的大小月分布，0表示小，1表示大；
     * 3. 最后7位表示农历年首（正月初一）对应的公历日期。
     *
     * 以2014年的数据0x955ABF为例说明：
     *                 1001 0101 0101 1010 1011 1111
     *                 闰九月                                  农历正月初一对应公历1月31号
     */
    private static final int LUNAR_INFO[] = {
            0x84B6BF,/*1900*/
            0x04AE53,0x0A5748,0x5526BD,0x0D2650,0x0D9544,0x46AAB9,0x056A4D,0x09AD42,0x24AEB6,0x04AE4A,/*1901-1910*/
            0x6A4DBE,0x0A4D52,0x0D2546,0x5D52BA,0x0B544E,0x0D6A43,0x296D37,0x095B4B,0x749BC1,0x049754,/*1911-1920*/
            0x0A4B48,0x5B25BC,0x06A550,0x06D445,0x4ADAB8,0x02B64D,0x095742,0x2497B7,0x04974A,0x664B3E,/*1921-1930*/
            0x0D4A51,0x0EA546,0x56D4BA,0x05AD4E,0x02B644,0x393738,0x092E4B,0x7C96BF,0x0C9553,0x0D4A48,/*1931-1940*/
            0x6DA53B,0x0B554F,0x056A45,0x4AADB9,0x025D4D,0x092D42,0x2C95B6,0x0A954A,0x7B4ABD,0x06CA51,/*1941-1950*/
            0x0B5546,0x555ABB,0x04DA4E,0x0A5B43,0x352BB8,0x052B4C,0x8A953F,0x0E9552,0x06AA48,0x6AD53C,/*1951-1960*/
            0x0AB54F,0x04B645,0x4A5739,0x0A574D,0x052642,0x3E9335,0x0D9549,0x75AABE,0x056A51,0x096D46,/*1961-1970*/
            0x54AEBB,0x04AD4F,0x0A4D43,0x4D26B7,0x0D254B,0x8D52BF,0x0B5452,0x0B6A47,0x696D3C,0x095B50,/*1971-1980*/
            0x049B45,0x4A4BB9,0x0A4B4D,0xAB25C2,0x06A554,0x06D449,0x6ADA3D,0x0AB651,0x095746,0x5497BB,/*1981-1990*/
            0x04974F,0x064B44,0x36A537,0x0EA54A,0x86B2BF,0x05AC53,0x0AB647,0x5936BC,0x092E50,0x0C9645,/*1991-2000*/
            0x4D4AB8,0x0D4A4C,0x0DA541,0x25AAB6,0x056A49,0x7AADBD,0x025D52,0x092D47,0x5C95BA,0x0A954E,/*2001-2010*/
            0x0B4A43,0x4B5537,0x0AD54A,0x955ABF,0x04BA53,0x0A5B48,0x652BBC,0x052B50,0x0A9345,0x474AB9,/*2011-2020*/
            0x06AA4C,0x0AD541,0x24DAB6,0x04B64A,0x6a573D,0x0A4E51,0x0D2646,0x5E933A,0x0D534D,0x05AA43,/*2021-2030*/
            0x36B537,0x096D4B,0xB4AEBF,0x04AD53,0x0A4D48,0x6D25BC,0x0D254F,0x0D5244,0x5DAA38,0x0B5A4C,/*2031-2040*/
            0x056D41,0x24ADB6,0x049B4A,0x7A4BBE,0x0A4B51,0x0AA546,0x5B52BA,0x06D24E,0x0ADA42,0x355B37,/*2041-2050*/
            0x09374B,0x8497C1,0x049753,0x064B48,0x66A53C,0x0EA54F,0x06AA44,0x4AB638,0x0AAE4C,0x092E42,/*2051-2060*/
            0x3C9735,0x0C9649,0x7D4ABD,0x0D4A51,0x0DA545,0x55AABA,0x056A4E,0x0A6D43,0x452EB7,0x052D4B,/*2061-2070*/
            0x8A95BF,0x0A9553,0x0B4A47,0x6B553B,0x0AD54F,0x055A45,0x4A5D38,0x0A5B4C,0x052B42,0x3A93B6,/*2071-2080*/
            0x069349,0x7729BD,0x06AA51,0x0AD546,0x54DABA,0x04B64E,0x0A5743,0x452738,0x0D264A,0x8E933E,/*2081-2090*/
            0x0D5252,0x0DAA47,0x66B53B,0x056D4F,0x04AE45,0x4A4EB9,0x0A4D4C,0x0D1541,0x2D92B5          /*2091-2099*/
    };

    /**
     * 阳历转农历
     *
     * @param solar 阳历
     * @return
     * */
    private static LocalDate solar2lunar(LocalDate solar){
        return solar;
    }

    /**
     * 农历转阳历
     *
     * @param lunar 农历
     * @return
     * */
    private static LocalDate lunar2solar(LocalDate lunar){
        return lunar;
    }

}
