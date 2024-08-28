import java.time.LocalDate;

//This program creates rental agreements for tools given a tool code, checkout date, rental days, and discount amount
//This implementation lends itself to using a database in the future, but for the sake of simplicity in setup and
// testing and due to the limited scope of required functionality we're just using "ToolRepository", "Tool", and
// "ToolChargeCategory" classes in place of database connection and tables
public class ToolRental {

    public static void main(String[] args) {
        // Example usage
        RentalAgreement agreement = checkout("LADW", LocalDate.of(2020, 7, 2), 2, 10);
        System.out.println(agreement);
    }

    public static RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDays, int discount) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        //generate a new agreement given the user provided values
        RentalAgreement agreement = new RentalAgreement(toolCode, checkoutDate, rentalDays, discount);
        //using the tool listing in the agreement calculate and set the pricing
        agreement.calculate();
        return agreement;
    }
}