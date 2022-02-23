package com.la3ypotato.toolrenter.tool;

public class Tool {;
    public static final String DEFAULT_TOOL_CODE = "DEFAULT";
    public static final String DEFAULT_TOOL_TYPE = "DEFAULT";
    public static final String DEFAULT_BRAND = "DEFAULT";
    public static final double DEFAULT_DAILY_CHARGE = 1.00;
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

    public Tool() {
        this.toolCode = DEFAULT_TOOL_CODE;
        this.toolType = DEFAULT_TOOL_TYPE;
        this.brand = DEFAULT_BRAND;
        this.dailyCharge = DEFAULT_DAILY_CHARGE;
        this.weekdayCharge = DEFAULT_WEEKDAY_CHARGE;
        this.weekendCharge = DEFAULT_WEEKEND_CHARGE;
        this.holidayCharge = DEFAULT_HOLIDAY_CHARGE;
    }

    public Tool(String toolCode, String toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = DEFAULT_DAILY_CHARGE;
        this.weekdayCharge = DEFAULT_WEEKDAY_CHARGE;
        this.weekendCharge = DEFAULT_WEEKEND_CHARGE;
        this.holidayCharge = DEFAULT_HOLIDAY_CHARGE;
    }
}
