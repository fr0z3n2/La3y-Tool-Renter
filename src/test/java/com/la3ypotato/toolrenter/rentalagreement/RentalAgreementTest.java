package com.la3ypotato.toolrenter.rentalagreement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

/**
 * This test class is used to run through the 6 main testing scenarios to validate the logic for the RentalAgreement
 * class such that the expected results are obtained based on the four main pieces of data.
 *
 * Tool Code
 * Rental Days
 * Discount Percent
 * Checkout Date
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class RentalAgreementTest {

    // HELPER METHODS //

    /**
     * Helper method for validating the tool code against the rental agreement validators.
     *
     * @param rentalAgreement - current rental agreement
     * @param toolCode - test tool code
     * @param shouldFail - whether or not the test is expecting a failure
     */
    public void testToolCodeAgainstRentalAgreement(RentalAgreement rentalAgreement, String toolCode, boolean shouldFail) {
        try {
            rentalAgreement.setTargetTool(toolCode);
        } catch (IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("Passed tool code " + toolCode + " was expected to fail. Passing test!");
            } else {
                Assertions.fail("The passed target rental tool: " + toolCode + " should be accepted and an exception should" +
                            " not be thrown. Failing test!");
            }
        }
    }

    /**
     * Helper method for validating the rental days against the rental agreement validators.
     *
     * @param rentalAgreement - current rental agreement
     * @param rentalDays - test rental days
     * @param shouldFail - whether or not the test is expecting a failure
     */
    public void testRentalDaysAgainstRentalAgreement(RentalAgreement rentalAgreement, String rentalDays, boolean shouldFail) {
        try {
            rentalAgreement.setRentalDays(rentalDays);
        } catch (IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("Passed number of rental days (" + rentalDays + ") was expected to fail. Passing test!");
            } else {
                Assertions.fail("The test rental days (" + rentalDays + ") should be accepted and an exception should" +
                            " not be thrown. Failing test!");
            }
        }
    }

    /**
     * Helper method for validating the discount against the rental agreement validators.
     *
     * @param rentalAgreement - current rental agreement
     * @param discount - test discount
     * @param shouldFail - whether or not the test is expecting a failure
     */
    public void testDiscountAgainstRentalAgreement(RentalAgreement rentalAgreement, String discount, boolean shouldFail) {
        try {
            rentalAgreement.setDiscount(discount);
        } catch (IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("Test discount percent (" + discount + ") was expected to fail. Passing test!");
            } else {
                Assertions.fail("The test discount percent (" + discount + ") should be accepted and an exception should" +
                            " not be thrown. Failing test!");
            }
        }
    }

    /**
     * Helper method for validating the checkout date against the rental agreement validators.
     *
     * @param rentalAgreement - current rental agreement
     * @param checkoutDate - test checkout date
     * @param shouldFail - whether or not the test is expecting a failure
     */
    public void testCheckoutDateAgainstRentalAgreement(RentalAgreement rentalAgreement, String checkoutDate, boolean shouldFail) {
        try {
            rentalAgreement.setCheckoutDate(checkoutDate);
        } catch(IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("The test checkout date (" + checkoutDate + ") was expected to fail. Passing test!");
            } else {
                Assertions.fail("The test checkout date (" + checkoutDate + ") should be accepted and an exception should" +
                            " not be thrown. Failing test!");
            }
        }
    }

    // TEST METHODS //

    /**
     * Checks if the available tools for rent have been initialized.
     */
    @Test
    public void validateAvailableTools() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        if (rentalAgreement.getAvailableToolsForRent().isEmpty()) {
            Assertions.fail("The loaded rental agreement should not be incomplete! The available tools Map failed to load.");
        }
    }

    /**
     * Validates that the calculation for Test 1 in the specification is calculating as expected.
     */
    @Test
    public void validateTest1() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "JAKR";
        String testRentalDays = "5";
        String testDiscount = "101";
        String testCheckoutDate = "9/3/2015";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should fail as expected! 101 is out of bounds for discount. No need to perform any further tests.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, true);
    }

    /**
     * Validates that the calculation for Test 2 in the specification is calculating as expected.
     */
    @Test
    public void validateTest2() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "LADW";
        String testRentalDays = "3";
        String testDiscount = "10";
        String testCheckoutDate = "7/2/2020";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should not fail as this is a valid discount amount.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, false);
        // Attempt to finalize the RentalAgreement. This will calculate the remaining values.
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            Assertions.fail("Failing test! The rental agreement has not been completed as expected.");
        }
        // Define the expected values.
        // Three days after 7/2/2020 is 7/5/2020.
        LocalDate expectedDueDate = LocalDate.of(2020, 7, 5);
        // Only expecting 2 charge days as the 3rd is an observed holiday in 2020 and
        // ladders contain a weekend charge.
        int expectedChargeDays = 2;
        // Daily charge for ladder is 1.99, so (1.99 * 2) = 3.98
        double expectedPreDiscountCharge = 3.98;
        // Expecting 0.4 here as (3.98 * .1) = 0.398 is rounded up to 0.4
        double expectedDiscountAmount = 0.4;
        // Expecting 3.58 here as (3.98 - .4) = 3.58.
        double expectedFinalCharge = 3.58;
        // Obtain the actual values from the finalized rental agreement.
        LocalDate actualDueDate = rentalAgreement.getDueDate();
        int actualChargeDays = rentalAgreement.getChargeDays();
        double actualPreDiscountCharge = rentalAgreement.getPreDiscountAmount();
        double actualDiscountAmount = rentalAgreement.getDiscountAmount();
        double actualFinalCharge = rentalAgreement.getFinalCharge();
        // Check the actual values versus the expected.
        Assertions.assertEquals(expectedDueDate, actualDueDate);
        Assertions.assertEquals(expectedChargeDays, actualChargeDays);
        Assertions.assertEquals(expectedPreDiscountCharge, actualPreDiscountCharge);
        Assertions.assertEquals(expectedDiscountAmount, actualDiscountAmount);
        Assertions.assertEquals(expectedFinalCharge, actualFinalCharge);
    }

    /**
     * Validates that the calculation for Test 3 in the specification is calculating as expected.
     */
    @Test
    public void validateTest3() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "CHNS";
        String testRentalDays = "5";
        String testDiscount = "25";
        String testCheckoutDate = "7/2/2015";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should fail as expected! 101 is out of bounds.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, false);
        // Attempt to finalize the RentalAgreement. This will calculate the remaining values.
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            Assertions.fail("Failing test! The rental agreement has not been completed as expected.");
        }
        // Define the expected values.
        // Five days after 7/2/2015 is 7/7/2015.
        LocalDate expectedDueDate = LocalDate.of(2015, 7, 7);
        // Only expecting 3 charge days here because
        int expectedChargeDays = 3;
        // Daily charge for chainsaw is 1.49, so (1.49 * 3) = 4.47
        double expectedPreDiscountCharge = 4.47;
        // Expecting 1.12 here as (4.47 * .25) = 1.1175 is rounded to 1.12
        double expectedDiscountAmount = 1.12;
        // Expecting 3.35 here as (4.47 - 1.12) = 3.35
        double expectedFinalCharge = 3.35;
        // Obtain the actual values from the finalized rental agreement.
        LocalDate actualDueDate = rentalAgreement.getDueDate();
        int actualChargeDays = rentalAgreement.getChargeDays();
        double actualPreDiscountCharge = rentalAgreement.getPreDiscountAmount();
        double actualDiscountAmount = rentalAgreement.getDiscountAmount();
        double actualFinalCharge = rentalAgreement.getFinalCharge();
        // Check the actual values versus the expected.
        Assertions.assertEquals(expectedDueDate, actualDueDate);
        Assertions.assertEquals(expectedChargeDays, actualChargeDays);
        Assertions.assertEquals(expectedPreDiscountCharge, actualPreDiscountCharge);
        Assertions.assertEquals(expectedDiscountAmount, actualDiscountAmount);
        Assertions.assertEquals(expectedFinalCharge, actualFinalCharge);
    }

    /**
     * Validates that the calculation for Test 4 in the specification is calculating as expected.
     */
    @Test
    public void validateTest4() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "JAKD";
        String testRentalDays = "6";
        String testDiscount = "0";
        String testCheckoutDate = "9/3/2015";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should fail as expected! 101 is out of bounds.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, false);
        // Attempt to finalize the RentalAgreement. This will calculate the remaining values.
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            Assertions.fail("Failing test! The rental agreement has not been completed as expected.");
        }

        LocalDate expectedDueDate = LocalDate.of(2015, 9, 9);
        // Only expecting 3 charge days as this date range includes labor day + weekend and jackhammers do not have
        // a holiday charge nor a weekend charge.
        int expectedChargeDays = 3;
        // Daily charge for jackhammers is 2.99, so (2.99 * 3) = 8.97
        double expectedPreDiscountCharge = 8.97;
        // No discount applied.
        double expectedDiscountAmount = 0;
        // Since there is no discount applied, the final charge should be the same as the pre-discount amount.
        double expectedFinalCharge = expectedPreDiscountCharge;
        // Obtain the actual values from the finalized rental agreement.
        LocalDate actualDueDate = rentalAgreement.getDueDate();
        int actualChargeDays = rentalAgreement.getChargeDays();
        double actualPreDiscountCharge = rentalAgreement.getPreDiscountAmount();
        double actualDiscountAmount = rentalAgreement.getDiscountAmount();
        double actualFinalCharge = rentalAgreement.getFinalCharge();
        // Check the actual values versus the expected.
        Assertions.assertEquals(expectedDueDate, actualDueDate);
        Assertions.assertEquals(expectedChargeDays, actualChargeDays);
        Assertions.assertEquals(expectedPreDiscountCharge, actualPreDiscountCharge);
        Assertions.assertEquals(expectedDiscountAmount, actualDiscountAmount);
        Assertions.assertEquals(expectedFinalCharge, actualFinalCharge);
    }

    /**
     * Validates that the calculation for Test 5 in the specification is calculating as expected.
     */
    @Test
    public void validateTest5() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "JAKR";
        String testRentalDays = "9";
        String testDiscount = "0";
        String testCheckoutDate = "7/2/2015";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should fail as expected! 101 is out of bounds.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, false);
        // Attempt to finalize the RentalAgreement. This will calculate the remaining values.
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            Assertions.fail("Failing test! The rental agreement has not been completed as expected.");
        }
        // Define the expected values.
        // 9 days after 7/2/2015 is 7/11/2015
        LocalDate expectedDueDate = LocalDate.of(2015, 7, 11);
        // Only expecting 5 charge days as jackhammers only include weekday charges and the defined date range includes
        // 3 weekend charges and one holiday charge.
        int expectedChargeDays = 5;
        // Daily charge for jackhammers is 2.99, so (2.99 * 5) = 14.95
        double expectedPreDiscountCharge = 14.95;
        // No discount applied
        double expectedDiscountAmount = 0;
        // Since there is no discount applied, the final charge should be the same as the pre-discount amount.
        double expectedFinalCharge = expectedPreDiscountCharge;
        // Obtain the actual values from the finalized rental agreement.
        LocalDate actualDueDate = rentalAgreement.getDueDate();
        int actualChargeDays = rentalAgreement.getChargeDays();
        double actualPreDiscountCharge = rentalAgreement.getPreDiscountAmount();
        double actualDiscountAmount = rentalAgreement.getDiscountAmount();
        double actualFinalCharge = rentalAgreement.getFinalCharge();
        // Check the actual values versus the expected.
        Assertions.assertEquals(expectedDueDate, actualDueDate);
        Assertions.assertEquals(expectedChargeDays, actualChargeDays);
        Assertions.assertEquals(expectedPreDiscountCharge, actualPreDiscountCharge);
        Assertions.assertEquals(expectedDiscountAmount, actualDiscountAmount);
        Assertions.assertEquals(expectedFinalCharge, actualFinalCharge);
    }

    /**
     * Validates that the calculation for Test 6 in the specification is calculating as expected.
     */
    @Test
    public void validateTest6() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        String testToolCode = "JAKR";
        String testRentalDays = "4";
        String testDiscount = "50";
        String testCheckoutDate = "7/2/2020";
        // The below test should not fail as this is a valid tool code.
        testToolCodeAgainstRentalAgreement(rentalAgreement, testToolCode, false);
        // The below test should not fail as this is a valid number of rental days.
        testRentalDaysAgainstRentalAgreement(rentalAgreement, testRentalDays, false);
        // The below test should not fail as this is a valid checkout date.
        testCheckoutDateAgainstRentalAgreement(rentalAgreement, testCheckoutDate, false);
        // The below test should fail as expected! 101 is out of bounds.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, false);
        // Attempt to finalize the RentalAgreement. This will calculate the remaining values.
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            Assertions.fail("Failing test! The rental agreement has not been completed as expected.");
        }
        // Define the expected values.
        // Four days after 7/2/2020 is 7/6/2020.
        LocalDate expectedDueDate = LocalDate.of(2020, 7, 6);
        // Only expecting 1 charge day here as jackhammers only include weekday charges and the defined date range
        // includes 2 weekend days and 1 holiday.
        int expectedChargeDays = 1;
        // Daily charge for a jackhammer is 2.99, so (2.99 * 1) = 2.99.
        double expectedPreDiscountCharge = 2.99;
        // Expecting 1.5 here because (2.99 * .5) = 1.495 which rounded up 1o 1.5
        double expectedDiscountAmount = 1.5;
        // Expecting 1.49 here because (2.99 - 1.5) = 1.49
        double expectedFinalCharge = 1.49;
        // Obtain the actual values from the finalized rental agreement.
        LocalDate actualDueDate = rentalAgreement.getDueDate();
        int actualChargeDays = rentalAgreement.getChargeDays();
        double actualPreDiscountCharge = rentalAgreement.getPreDiscountAmount();
        double actualDiscountAmount = rentalAgreement.getDiscountAmount();
        double actualFinalCharge = rentalAgreement.getFinalCharge();
        // Check the actual values versus the expected.
        Assertions.assertEquals(expectedDueDate, actualDueDate);
        Assertions.assertEquals(expectedChargeDays, actualChargeDays);
        Assertions.assertEquals(expectedPreDiscountCharge, actualPreDiscountCharge);
        Assertions.assertEquals(expectedDiscountAmount, actualDiscountAmount);
        Assertions.assertEquals(expectedFinalCharge, actualFinalCharge);
    }
}
