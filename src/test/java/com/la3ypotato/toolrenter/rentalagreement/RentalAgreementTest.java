package com.la3ypotato.toolrenter.rentalagreement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

public class RentalAgreementTest {

    // HELPER METHODS //

    public void testToolCodeAgainstRentalAgreement(RentalAgreement rentalAgreement, String toolCode, boolean shouldFail) {
        try {
            rentalAgreement.setTargetTool(toolCode);
        } catch (IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("Passed tool code " + toolCode + " was expected to fail. Passing test!");
            } else {
                Assertions.fail("The passed target rental tool: " + toolCode + "should be accepted and an exception should" +
                            " not be thrown. Failing test!");
            }
        }
    }

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

    @Test
    public void validateAvailableTools() {
        RentalAgreement rentalAgreement = new RentalAgreement();
        if (rentalAgreement.getAvailableToolsForRent().isEmpty()) {
            Assertions.fail("The loaded rental agreement should not be empty! The available tools failed to load.");
        }

        // TODO: Add logic to check the content of the tools map.
    }

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
        // The below test should fail as expected! 101 is out of bounds. No need to perform any further tests.
        testDiscountAgainstRentalAgreement(rentalAgreement, testDiscount, true);
    }

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

        LocalDate expectedDueDate = LocalDate.of(2020, 7, 5);
        // Only expecting 2 charge days as the 3rd is an observed holiday in 2020 and
        // ladders contain a weekend charge.
        int expectedChargeDays = 2;
        double expectedPreDiscountCharge = 3.98;
        // Expecting 0.4 here as (3.98 * .1) = 0.398 is rounded to 0.4
        double expectedDiscountAmount = 0.4;
        double expectedFinalCharge = 3.58;
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

        LocalDate expectedDueDate = LocalDate.of(2015, 7, 7);
        // Only expecting 3 charge days here because
        int expectedChargeDays = 3;
        double expectedPreDiscountCharge = 4.47;
        // Expecting 1.12 here as (4.47 * .25) = 1.1175 is rounded to 1.12
        double expectedDiscountAmount = 1.12;
        double expectedFinalCharge = 3.35;
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
        // Only expecting 3 charge days here because while July 3rd is an observed holiday,
        // chainsaws are charged as a rental day on holidays, but not weekends.
        int expectedChargeDays = 3;
        double expectedPreDiscountCharge = 8.97;
        double expectedDiscountAmount = 0;
        // Since there is no discount applied, the final charge should be the same as the pre-discount amount.
        double expectedFinalCharge = 8.97;
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

        LocalDate expectedDueDate = LocalDate.of(2015, 7, 11);
        // Only expecting 3 charge days here because while July 3rd is an observed holiday,
        // chainsaws are charged as a rental day on holidays, but not weekends.
        int expectedChargeDays = 5;
        double expectedPreDiscountCharge = 14.95;
        double expectedDiscountAmount = 0;
        // Since there is no discount applied, the final charge should be the same as the pre-discount amount.
        double expectedFinalCharge = 14.95;
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

        LocalDate expectedDueDate = LocalDate.of(2020, 7, 6);
        // Only expecting 3 charge days here because while July 3rd is an observed holiday,
        // chainsaws are charged as a rental day on holidays, but not weekends.
        int expectedChargeDays = 1;
        double expectedPreDiscountCharge = 2.99;
        double expectedDiscountAmount = 1.5;
        // Since there is no discount applied, the final charge should be the same as the pre-discount amount.
        double expectedFinalCharge = 1.49;
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
