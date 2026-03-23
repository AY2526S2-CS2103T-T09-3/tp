package seedu.address.model.billing;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents payment record for a tutee.
 * Guarantees: immutable.
 */
public class Payment {

    public static final Payment EMPTY = new Payment();

    private final Set<LocalDate> paidDates;

    /**
     * Creates a {@code Payment} object with payment history
     * @param paidDates Payment dates (varargs for convenience)
     */
    public Payment(LocalDate... paidDates) {
        requireNonNull(paidDates);
        this.paidDates = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(paidDates)));
    }


    public Set<LocalDate> getPaidDates() {
        return paidDates;
    }

    /**
     * Record payment made on {@code date}
     * @param date A valid date
     * @return {@code Payment} object with updated payment history
     */
    public Payment recordPayment(LocalDate date) {
        requireNonNull(date);
        Set<LocalDate> next = new LinkedHashSet<>(paidDates);
        next.add(date);
        return new Payment(next.toArray(new LocalDate[0]));
    }

    /**
     * Checks if given {@code date} is in recorded payment history
     * @param date A valid date
     * @return {@code True} if date is present in payment history,
     *      {@code False} otherwise
     */
    public boolean hasPaidOn(LocalDate date) {
        requireNonNull(date);
        return paidDates.contains(date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Payment)) {
            return false;
        }
        Payment otherPayment = (Payment) other;
        return paidDates.equals(otherPayment.paidDates);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(paidDates);
    }

    @Override
    public String toString() {
        return String.format("Payment[paidDates=%s]", paidDates);
    }
}
