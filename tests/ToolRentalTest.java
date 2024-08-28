import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ToolRentalTest {

    @Test
    void testInvalidDiscount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToolRental.checkout("JAKR", LocalDate.of(2015, 3, 9), 5, 101);
        });
        String expectedMessage = "Discount percent must be between 0 and 100.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test
    void testInvalidRentalDays() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToolRental.checkout("LADW", LocalDate.of(2020, 7, 2), 0, 10);
        });
        String expectedMessage = "Rental day count must be 1 or greater.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidToolCode() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToolRental.checkout("XXXX", LocalDate.of(2015, 3, 9), 5, 0);
        });
        String expectedMessage = "Invalid tool code";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCheckoutOmitHoliday() {
        RentalAgreement agreement = ToolRental.checkout("LADW", LocalDate.of(2020, 7, 2), 3, 10);
        assertEquals(2, agreement.getChargeableDays());
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckoutOmitWeekend() {
        RentalAgreement agreement = ToolRental.checkout("CHNS", LocalDate.of(2015, 7, 2), 5, 25);
        assertEquals(3, agreement.getChargeableDays());
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckoutOmitWeekendAndHoliday() {
        RentalAgreement agreement = ToolRental.checkout("JAKD", LocalDate.of(2015, 9, 3), 6, 0);
        assertEquals(3, agreement.getChargeableDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckoutOmitWeekendAndHolidayRoundDown() {
        RentalAgreement agreement = ToolRental.checkout("JAKR", LocalDate.of(2020, 7, 2), 4, 50);
        assertEquals(1, agreement.getChargeableDays());
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.50, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.49, agreement.getFinalCharge(), 0.01);
    }

}