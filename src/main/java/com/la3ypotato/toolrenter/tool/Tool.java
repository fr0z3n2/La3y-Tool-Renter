package com.la3ypotato.toolrenter.tool;

/**
 * This class serves as the Tool object structure for a rentable tool.
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class Tool {;
    // Default values if not specified.
    public static final String DEFAULT_TOOL_CODE = "DEFAULT";
    public static final String DEFAULT_TOOL_TYPE = "DEFAULT";
    public static final String DEFAULT_BRAND = "DEFAULT";
    public static final double DEFAULT_DAILY_CHARGE = 1.00;
    public static final boolean DEFAULT_WEEKDAY_CHARGE = true;
    public static final boolean DEFAULT_WEEKEND_CHARGE = false;
    public static final boolean DEFAULT_HOLIDAY_CHARGE = false;
    // Class properties.
    public String toolCode;
    public String toolType;
    public String brand;
    public double dailyCharge;
    public boolean weekdayCharge;
    public boolean weekendCharge;
    public boolean holidayCharge;

    /**
     * No argument constructor that initializes the tool object with default values.
     */
    public Tool() {
        this.toolCode = DEFAULT_TOOL_CODE;
        this.toolType = DEFAULT_TOOL_TYPE;
        this.brand = DEFAULT_BRAND;
        this.dailyCharge = DEFAULT_DAILY_CHARGE;
        this.weekdayCharge = DEFAULT_WEEKDAY_CHARGE;
        this.weekendCharge = DEFAULT_WEEKEND_CHARGE;
        this.holidayCharge = DEFAULT_HOLIDAY_CHARGE;
    }

    /**
     * This provides a starting structure of the tool object based on the three passed params. The daily
     * charge, weekday charge, weekend charge, and holiday charge values will have to be overridden after
     * the object has been initialized. This is to shorten the constructor parameter length.
     *
     * @param toolCode - 4 digit (preferred, but not enforced) tool identifier code.
     * @param toolType - The type of tool (ex: chainsaw, ladder, jackhammer, etc.)
     * @param brand - The brand of the tool (ex: Stihl, DeWalt, Werner, etc.)
     */
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
