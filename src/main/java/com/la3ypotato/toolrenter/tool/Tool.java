package com.la3ypotato.toolrenter.tool;

public class Tool {;
    public static final double DEFAULT_CHARGE = 1.00;
    public static final boolean DEFAULT_WEEKDAY_CHARGE = true;
    public static final boolean DEFAULT_WEEKEND_CHARGE = false;
    public static final boolean DEFAULT_HOLIDAY_CHARGE = false;

    public String toolCode;
    public String toolType;
    public String brand;
    public double dailyCharge;
    public boolean weekdayCharge;
    public boolean weekendCharge;
    public boolean holidayCharge;

    public Tool(String toolCode, String toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = DEFAULT_CHARGE;
        this.weekdayCharge = DEFAULT_WEEKDAY_CHARGE;
        this.weekendCharge = DEFAULT_WEEKEND_CHARGE;
        this.holidayCharge = DEFAULT_HOLIDAY_CHARGE;
    }
}
