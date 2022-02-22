package com.la3ypotato.toolrenter.rentalagreement;

import com.la3ypotato.toolrenter.tool.Tool;
import com.la3ypotato.toolrenter.tool.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentalAgreement {
    // Constants
    private final String INPUT_DATE_FORMAT = "M/d/yyyy";
    private final String OUTPUT_DATE_FORMAT = "MM/dd/yyyy";
    private final int ROUNDING_PRECISION = 2;
    private final Locale locale = new Locale("en", "US");
    // Must have class properties
    private Map<String, Tool> availableToolsForRent;
    private Tool targetTool = new Tool();
    private int rentalDays;
    private int discount;
    private LocalDate checkoutDate;
    // Calculated class properties
    private LocalDate dueDate;
    private int chargeDays;
    private double preDiscountAmount;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement() {
        Tools toolsInstance = Tools.getInstance();
        this.availableToolsForRent = toolsInstance.getAvailableTools();
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) throws IllegalArgumentException {
        if (checkoutDate == null) {
            throw new IllegalArgumentException("Passed checkout date cannot be null!");
        }

        return checkoutDate.plusDays(rentalDays);
    }

    private int calculateChargeDays(LocalDate checkoutDate, LocalDate dueDate, Tool targetTool) {
        // TODO: Check if the checkout data is after the due date.
        int weekdays = 0;
        int weekendDays = 0;
        int holidays = 0;
        int chargeDays = 0;
        List<LocalDate> holidayList = getHolidays(checkoutDate.getYear(), dueDate.getYear());

        // Adding one day to exclude the checkout day as specified in the requirements.
        LocalDate date = checkoutDate.plusDays(1);
        //
        while(date.isBefore(dueDate) || date.isEqual(dueDate)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                weekdays++;
            } else {
                weekendDays++;
            }

            if (holidayList.contains(date)) {
                holidays++;
            }

            date = date.plusDays(1);
        }

        // Tally up all of the charge days based on the targetTool settings.
        chargeDays = targetTool.weekdayCharge == true ? chargeDays + weekdays : chargeDays;
        chargeDays = targetTool.weekendCharge == true ? chargeDays + weekendDays : chargeDays;
        chargeDays = targetTool.holidayCharge == true ? chargeDays + holidays : chargeDays;

        return chargeDays;
    }

    private double calculatePreDiscountCharge(int chargeDays, double dailyCharge) {
        BigDecimal result = new BigDecimal(0);
        BigDecimal cd = BigDecimal.valueOf(chargeDays);
        BigDecimal dc = BigDecimal.valueOf(dailyCharge);
        // Rounding the currency up as defined in the specification.
        result = cd.multiply(dc).setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    private double calculateDiscountAmount(double discount, double preDiscountCharge) {
        // No need to calculate the discount if there isn't one.
        if (discount == 0) {
            return discount;
        }

        BigDecimal result = new BigDecimal(0);
        discount = discount * 0.01;
        BigDecimal d = BigDecimal.valueOf(discount);
        BigDecimal pdc = BigDecimal.valueOf(preDiscountCharge);
        // discount_price = original_price - (original_price * discount)
        result = pdc.multiply(d).setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    private double calculateFinalCharge(double preDiscountCharge, double discountAmount) {
        return preDiscountCharge - discountAmount;
    }

    private List<LocalDate> getHolidays(int startYear, int endYear) {
        List<LocalDate> holidays = new ArrayList<>();

        while (startYear <= endYear) {
            holidays.add(getLaborDay(startYear));
            holidays.add(getIndependenceDay(startYear));
            startYear++;
        }

        return holidays;
    }

    private LocalDate getLaborDay(int year) {
        LocalDate firstOfSept = LocalDate.of(year, 9, 1);
        return firstOfSept.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    private LocalDate getIndependenceDay(int year) {
        LocalDate julyFourth = LocalDate.of(year, 7, 4);
        // Check
        // If July 4th falls on a Saturday, set the day back to Friday.
        if (julyFourth.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return julyFourth.minusDays(1);
        // If July 4th falls on a Sunday, set the day forward to Monday.
        } else if (julyFourth.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return julyFourth.plusDays(1);
        }

        return julyFourth;
    }

    public void setTargetTool(String toolCode) throws IllegalArgumentException {
        // If the target tool for rent does not exist in the loaded
        // availableTools HashMap, throw am IllegalArgumentException.
        String key = toolCode.toUpperCase();
        if (availableToolsForRent.containsKey(key)) {
            targetTool = availableToolsForRent.get(key);
        } else {
            throw new IllegalArgumentException("Tool does not exist in availableTools HashMap!");
        }
    }

    // Note, there is an upper bound her such that if the user enters a number greater
    // than 2147483647 (which is the maximum int size in Java) then a NumberFormatException
    // will be thrown when attempting to parse the primitive integer.
    public void setRentalDays(String rentalDaysStr) throws IllegalArgumentException {
        int rentalDays = 0;
        try {
            rentalDays = Integer.parseInt(rentalDaysStr);
            // Check if the rental days are within the requirement.
            if (rentalDays >= 1) {
                this.rentalDays = rentalDays;
            } else {
                throw new IllegalArgumentException("Passed rental day argument must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to parse rentalDaysStr argument.");
        }
    }

    public void setDiscount(String discountStr) {
        try {
            int discount = Integer.parseInt(discountStr);
            if (discount >= 0 && discount <= 100) {
                this.discount = discount;
            } else {
                throw new IllegalArgumentException("Discount argument is not within bounds [0,100]");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to parse discount argument");
        }
    }

    public void setCheckoutDate(String checkoutDateStr) throws IllegalArgumentException {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT);
            checkoutDate = LocalDate.parse(checkoutDateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Unable to parse date argument");
        }
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    public void setPreDiscountCharge(double preDiscountAmount) {
        this.preDiscountAmount = preDiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setFinalCharge(double finalCharge) {
        this.finalCharge = finalCharge;
    }

    public boolean isRentalAgreementComplete() {
        // No need to check discount here as it can be zero. When an int is uninitialized, it's default value is 0.
        if (targetTool == null || rentalDays == 0 || checkoutDate == null) {
            return false;
        }
        return true;
    }

    public void finalizeRentalAgreement() throws IllegalStateException {
        if (!isRentalAgreementComplete()) {
            // TODO: Provide which required property is missing in the exception message.
            throw new IllegalStateException("Rental Agreement is not complete!");
        }
        // Perform the rental agreement calculations per the specification.
        LocalDate dueDate = calculateDueDate(checkoutDate, rentalDays);
        setDueDate(dueDate);
        int chargeDays = calculateChargeDays(checkoutDate, dueDate, targetTool);
        setChargeDays(chargeDays);
        double preDiscountCharge = calculatePreDiscountCharge(chargeDays, targetTool.dailyCharge);
        setPreDiscountCharge(preDiscountCharge);
        double discountAmount = calculateDiscountAmount(discount, preDiscountCharge);
        setDiscountAmount(discountAmount);
        double finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount);
        setFinalCharge(finalCharge);
    }


    // Display the following information:
    // tool code
    // tool type
    // tool brand
    // rental days
    // Check out date
    // Due date
    // daily rental charge
    // charge days
    // pre-discount charge
    // discount percent
    // discount amount
    // final charge
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);
        NumberFormat numFormatter = NumberFormat.getCurrencyInstance(locale);
        String checkoutDateStr = checkoutDate.format(formatter);
        String dueDateStr = dueDate.format(formatter);
        String dailyChargeStr = numFormatter.format(targetTool.dailyCharge);
        String preDiscountAmountStr = numFormatter.format(preDiscountAmount);
        String discountAmountStr = numFormatter.format(discountAmount);
        String finalChargeStr = numFormatter.format(finalCharge);

        String retString = "Tool Code: " + targetTool.toolCode + "\n" +
                           "Tool Type: " + targetTool.toolType + "\n" +
                           "Tool Brand: " + targetTool.brand + "\n" +
                           "Rental Days: " + rentalDays + "\n" +
                           "Checkout Date: " + checkoutDateStr + "\n" +
                           "Due Date: " + dueDateStr + "\n" +
                           "Daily Rental Charge: " + dailyChargeStr + "\n" +
                           "Charge Days: " + chargeDays + "\n" +
                           "Pre-discount Charge: " + preDiscountAmountStr + "\n" +
                           "Discount Percent: " + discount + "%\n" +
                           "Discount Amount: " + discountAmountStr + "\n" +
                           "Final Charge: " + finalChargeStr;
        return retString;
    }
}
