import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class RentalAgreement {
    private final ToolListing toolListing;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private final int rentalDays;
    private final int discount;
    private double dailyCharge;
    private int chargeableDays;
    private double preDiscountCharge;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement(String toolCode, LocalDate checkoutDate, int rentalDays, int discount) {
        this.toolListing = ToolRepository.getToolListingByCode(toolCode);
        this.checkoutDate = checkoutDate;
        this.rentalDays = rentalDays;
        this.dueDate = checkoutDate.plusDays(rentalDays);
        this.discount = discount;
    }

    public void calculate() {
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        LocalDate currentDate = checkoutDate.plusDays(1);

        ToolChargeCategory chargeCategory = toolListing.getToolChargeCategory();

        while (!currentDate.isAfter(dueDate)) {
            if (isChargeableDay(currentDate, chargeCategory)) {
                chargeableDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        dailyCharge = chargeCategory.getDailyCharge();
        preDiscountCharge = dailyCharge * chargeableDays;
        BigDecimal bd = BigDecimal.valueOf(preDiscountCharge * (discount / 100.0));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        discountAmount = bd.doubleValue();

        finalCharge = preDiscountCharge - discountAmount;
    }

    private boolean isChargeableDay(LocalDate date, ToolChargeCategory chargeCategory) {
        boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean isHoliday = isHoliday(date);

        if (isHoliday && !chargeCategory.isHolidayCharge() ||
                isWeekend && !chargeCategory.isWeekendCharge() ||
                !isWeekend && !chargeCategory.isWeekdayCharge()) {
            return false;
        }
        return true;
    }

    private boolean isHoliday(LocalDate date) {
        LocalDate independenceDay = LocalDate.of(date.getYear(), 7, 4);
        LocalDate laborDay = LocalDate.of(date.getYear(), 9, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }

        return date.equals(independenceDay) || date.equals(laborDay);
    }

    public int getChargeableDays() {
        return chargeableDays;
    }

    public double getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalCharge() {
        return finalCharge;
    }

    @Override
    public String toString() {
        return "Rental Agreement:" +
                "\nTool Code: " + toolListing.getTool().getToolCode() +
                "\nTool Type: " + toolListing.getToolChargeCategory().getToolType() +
                "\nBrand: " + toolListing.getTool().getBrand() +
                "\nRental Days: " + rentalDays +
                "\nCheck Out Date: " + checkoutDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")) +
                "\nDue Date: " + dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")) +
                "\nDaily Rental Charge: $" + String.format("%,.2f", dailyCharge) +
                "\nCharge Days: " + chargeableDays +
                "\nPre-Discount Charge: $" + String.format("%,.2f", preDiscountCharge) +
                "\nDiscount Percent: " + discount + "%" +
                "\nDiscount Amount: $" + String.format("%,.2f", discountAmount) +
                "\nFinal Charge: $" + String.format("%,.2f", finalCharge);
    }
}
