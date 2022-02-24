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

/**
 * This class defines the tool rental agreement structure based on the specification requirements. This class also
 * provides various validators for the four key requested inputs (tool code, rental days, discount percent, and checkout
 * date) to verify that is input by the user is parsed correctly.
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class RentalAgreement {
    // Constants
    private final String INPUT_DATE_FORMAT = "M/d/yyyy";
    // Specification requests that the date is formatted with a 2 digit year code.
    private final String OUTPUT_DATE_FORMAT = "MM/dd/yy";
    // For currency values, the rounding precision will always be up to 2 decimal places.
    private final int ROUNDING_PRECISION = 2;
    // This application assumes the locale is always en-US.
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

    /**
     * Rental agreement constructor that starts the draft of a customer/merchant tool rental agreement.
     */
    public RentalAgreement() {
        Tools toolsInstance = Tools.getInstance();
        this.availableToolsForRent = toolsInstance.getAvailableTools();
    }

    /**
     * This method calculates the tool rental due date by adding the user defined rental days with the user defined
     * checkout date.
     *
     * @param checkoutDate - checkout date for the target tool to rent.
     * @param rentalDays - number of rental days that the target tool wil be checked out for.
     * @return - the expected return date (or due date) for the targe tool for rent.
     * @throws IllegalArgumentException - thrown if the rental days is <= 0 and if the checkout date is null.
     */
    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) throws IllegalArgumentException {
        if (checkoutDate == null) {
            throw new IllegalArgumentException("Passed checkout date cannot be null!");
        } else if (rentalDays <= 0) {
            throw new IllegalArgumentException("Passed number of rental days must be greater than zero!");
        }
        // Perform the calculation if the above checks clear.
        return checkoutDate.plusDays(rentalDays);
    }

    /**
     * This method calculates the number valid rental charge days based on the setting defined by the target tool.
     *
     * @param checkoutDate - The user defined checkout date
     * @param dueDate - The previously calculated due date.
     * @param targetTool - The user defined target tool for rent.
     * @return - returns the number of valid charge days based on the options defined for the target tool.
     */
    private int calculateChargeDays(LocalDate checkoutDate, LocalDate dueDate, Tool targetTool) throws IllegalArgumentException {
        // Purposefully not checking if the due date is before the checkout date. If the incorrect dates will result in
        // charge days defined as zero.
        if (checkoutDate == null) {
            throw new IllegalArgumentException("The passed checkout date cannot be null!");
        } else if (dueDate == null) {
            throw new IllegalArgumentException("The passed due date cannot be null!");
        } else if (targetTool == null) {
            throw new IllegalArgumentException("The passed target tool cannot be null!");
        }

        int weekdays = 0;
        int weekendDays = 0;
        int chargeDays = 0;
        int holidays = 0;
        List<LocalDate> holidayList = getHolidays(checkoutDate.getYear(), dueDate.getYear());

        // Adding one day to exclude the checkout day as specified in the requirements.
        LocalDate date = checkoutDate.plusDays(1);
        // Checking the checkout date falls on the due date as that is a valid charge day, but excluding the checkout
        // day.
        while(date.isBefore(dueDate) || date.isEqual(dueDate)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            // Find the number of weekdays and weekend days.
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                weekdays++;
            } else {
                weekendDays++;
            }
            // Find the number of holidays if any fall on the holiday list.
            if (holidayList.contains(date)) {
                holidays++;
            }

            date = date.plusDays(1);
        }

        // Filter out holidays that land on weekdays. Note, for this application all of the observed holidays (labor day
        // and 4th of july) land on a weekday. So, deduct from the weekday charges if the holiday charge is set to false.
        if (!targetTool.holidayCharge) {
            weekdays = weekdays - holidays;
        }

        // Tally up all of the remaining charge days based on the targetTool settings.
        chargeDays = targetTool.weekdayCharge == true ? chargeDays + weekdays : chargeDays;
        chargeDays = targetTool.weekendCharge == true ? chargeDays + weekendDays : chargeDays;

        return chargeDays;
    }

    /**
     * This method calculates the pre-discount charge by multiplying the passed charge days with the daily charge. Values
     * are converted to BigDecimal to handle larger decimal values to prevent further issues. Note, the result will be
     * rounded up.
     *
     * @param chargeDays - previously calculated number of valid charge days.
     * @param dailyCharge -
     * @return
     */
    private double calculatePreDiscountCharge(int chargeDays, double dailyCharge) {
        BigDecimal result = new BigDecimal(0);
        BigDecimal cd = BigDecimal.valueOf(chargeDays);
        BigDecimal dc = BigDecimal.valueOf(dailyCharge);
        // Rounding the currency up as defined in the specification.
        result = cd.multiply(dc).setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    /**
     * This method calculates the discount amount by multiplying the user defined discount by 0.01 and then multiplying
     * that result with the pre-discount charge. This will result in the discount amount and prevents any need for
     * division. Note, the result will be rounded up.
     *
     * @param discount - discount amount (Assumed passed value is of percent format i.e. 10 = 10% & 10 != 0.1)
     * @param preDiscountCharge
     * @return
     */
    private double calculateDiscountAmount(double discount, double preDiscountCharge) {
        // No need to calculate the discount if there isn't one.
        if (discount == 0) {
            return discount;
        }

        BigDecimal result = new BigDecimal(0);
        discount = discount * 0.01;
        BigDecimal d = BigDecimal.valueOf(discount);
        BigDecimal pdc = BigDecimal.valueOf(preDiscountCharge);
        // discount_price = (original_price * (discount * 0.01)). Rounding up as defined in specification.
        result = pdc.multiply(d).setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    /**
     * This method calculates the final charge by subtracting the pre-discount amount by the discount amount. Note, the
     * result will be rounded up.
     *
     * @param preDiscountCharge - the previously calculated pre-discount charge.
     * @param discountAmount - the previously calculated discount amount.
     * @return - the result for the final charge.
     */
    private double calculateFinalCharge(double preDiscountCharge, double discountAmount) {
        BigDecimal pdc = BigDecimal.valueOf(preDiscountCharge);
        BigDecimal da = BigDecimal.valueOf(discountAmount);
        BigDecimal result = pdc.subtract(da).setScale(ROUNDING_PRECISION, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    /**
     * This method will attempt to retrieve all of the observed holidays within the passed range of years.
     *
     * @param startYear - the starting year.
     * @param endYear - the endging year.
     * @return - a List of holidays in the range of passed years.
     */
    private List<LocalDate> getHolidays(int startYear, int endYear) {
        List<LocalDate> holidays = new ArrayList<>();
        // There can be multiple holidays across multiple years, so gather a list of all possible holidays in the
        // range of years. Note, there are currently only two observed holidays: 4th of July and Labor day.
        while (startYear <= endYear) {
            holidays.add(getLaborDay(startYear));
            holidays.add(getIndependenceDay(startYear));
            startYear++;
        }

        return holidays;
    }

    /**
     * This method will attempt to fetch a LocalDate object for labor day. Labor day occurs on the first Monday of
     * September.
     *
     * @param year - The calendar year to search for the holiday.
     * @return - LocalDate object containing the observed holiday.
     */
    private LocalDate getLaborDay(int year) {
        LocalDate firstOfSept = LocalDate.of(year, 9, 1);
        return firstOfSept.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    /**
     * This method attempts to fetch a LocalDate object for Independence Day such that if the holiday lands on a Saturday
     * the holiday is observed on Friday. The inverse is true if the holiday lands on a Sunday, the holiday is observed
     * on Monday.
     *
     * @param year - The calendar year to search for the holiday.
     * @return - LocalDate object containing the observed holiday.
     */
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

    // CLASS SETTERS

    /**
     * This method validates if the tool code exists in the Map for tools that are available for rent.
     *
     * @param toolCode - tool code for the target tool to rent.
     * @throws IllegalArgumentException - thrown if the passed tool code does not exist in the availableTools Map.
     */
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

    /**
     * This method validates the passed number of rental days per the rental agreement specification such that the value
     * cannot be less than 0. Note, there is an upper bound here such that if the user enters a number that is greater
     * than 2147483647 (which is the maximum int size in Java) then a NumberFormatException will be thrown when
     * attempting to parse the primitive integer.
     *
     * @param rentalDaysStr
     * @throws IllegalArgumentException - thrown if number of days is less than 0 or greater than 2147483647
     */
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

    /**
     * This method validates the passed discount string is within the bounds of the rental agreement specification such
     * that the discount is between 0 and 100. The passed discount format must be in a percentage format
     * (i.e. 10 = 10% & 10 != 0.1)
     *
     * @param discountStr - user defined discount percent.
     * @throws IllegalArgumentException - thrown if value is less than 0 or greater tha 100.
     */
    public void setDiscount(String discountStr) throws IllegalArgumentException {
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

    /**
     * This method checks that the passed date string is parsable based on the defined date input specification. It was
     * decided to force the user to enter four digit year codes as the ambiguity of two digit year codes.
     *
     * @param checkoutDateStr - user defined checkout date.
     * @throws IllegalArgumentException - thrown if the date cannot be parsed per expected format.
     */
    public void setCheckoutDate(String checkoutDateStr) throws IllegalArgumentException {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT);
            checkoutDate = LocalDate.parse(checkoutDateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Unable to parse date argument");
        }
    }

    /**
     * Setter for dueDate.
     *
     * @param dueDate - LocalDate due date.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Setter for chargeDays.
     *
     * @param chargeDays - int charge days
     */
    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    /**
     * Setter for preDiscountAmount.
     *
     * @param preDiscountAmount - double pre-discount amount
     */
    public void setPreDiscountCharge(double preDiscountAmount) {
        this.preDiscountAmount = preDiscountAmount;
    }

    /**
     * Setter for discountAmount.
     *
     * @param discountAmount - double discount amount
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * Setter for finalCharge.
     *
     * @param finalCharge - double final charge amount
     */
    public void setFinalCharge(double finalCharge) {
        this.finalCharge = finalCharge;
    }

    // CLASS GETTERS

    /**
     * Getter for availableTools.
     *
     * @return - This is the map that contains the available tools for rent.
     */
    public Map<String, Tool> getAvailableToolsForRent() {
        return availableToolsForRent;
    }

    /**
     * Getter for rental dueDate.
     *
     * @return - LocalDate due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Getter for rental chargeDays.
     *
     * @return - int charge days
     */
    public int getChargeDays() {
        return chargeDays;
    }

    /**
     * Getter for preDiscountAmount.
     *
     * @return - double pre-discount amount.
     */
    public double getPreDiscountAmount() {
        return preDiscountAmount;
    }

    /**
     * Getter for discountAmount.
     *
     * @return double discount amount.
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Getter for finalCharge
     *
     * @return double final charge.
     */
    public double getFinalCharge() {
        return finalCharge;
    }

    /**
     * This method determines whether or not the rental agreement has received the required fields in order for the
     * agreement to be finalized. This includes:
     *
     * Target Tool
     * Rental Days
     * Checkout Date
     *
     * Note, not requiring a discount here as this value can be zero.
     *
     * @return - a boolean value noting that the calculations can proceed.
     */
    private boolean isRentalAgreementComplete() {
        // No need to check discount here as it can be zero. When an int is uninitialized, it's default value is 0.
        if (targetTool == null || rentalDays == 0 || checkoutDate == null) {
            return false;
        }
        return true;
    }

    /**
     * This method finalizes the rental agreement by calculating the remaining fields needed to complete the rental
     * transaction.
     *
     * Due Date
     * Charge Days
     * Pre-Discount Amount
     * Discount Amount
     * Final Charge
     *
     * @throws IllegalStateException - thrown if the rental agreement is not complete. (Requires targetTool, rentalDays, and checkoutDate)
     */
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

    /**
     * This method returns a printable format of the rental agreement. This includes formatting the data based on the
     * requirements in the specification. The string will store the rental agreement data in the following order:
     *
     * Tool Code
     * Tool Type
     * Tool Brand
     * Rental Days
     * Checkout Date
     * Due Date
     * Daily Rental Charge
     * Charge Days
     * Pre-discount Charge
     * Discount Percent
     * Discount Amount
     * Final Charge
     *
     * @return - String with a pritable format of the rental agreement.
     */
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
