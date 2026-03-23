package seedu.address.model.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class PaymentTest {

    private static final LocalDate DATE_1 = LocalDate.of(2026, 1, 1);
    private static final LocalDate DATE_2 = LocalDate.of(2026, 2, 1);
    private static final LocalDate DATE_3 = LocalDate.of(2026, 3, 1);

    @Test
    public void constructor_validPaidDates_success() {
        Set<LocalDate> dates = new HashSet<>();
        dates.add(DATE_1);
        Payment payment = new Payment(dates.toArray(new LocalDate[0]));
        assertEquals(1, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
    }

    @Test
    public void constructor_emptyPaidDates_success() {
        Set<LocalDate> dates = new HashSet<>();
        Payment payment = new Payment(dates.toArray(new LocalDate[0]));
        assertEquals(0, payment.getPaidDates().size());
    }

    @Test
    public void constructor_nullPaidDates_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Payment(null));
    }

    @Test
    public void constructor_varargs_validDates_success() {
        Payment payment = new Payment(DATE_1, DATE_2);
        assertEquals(2, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
        assertTrue(payment.getPaidDates().contains(DATE_2));
    }

    @Test
    public void constructor_varargs_singleDate_success() {
        Payment payment = new Payment(DATE_1);
        assertEquals(1, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
    }

    @Test
    public void constructor_varargs_empty_success() {
        Payment payment = new Payment();
        assertEquals(0, payment.getPaidDates().size());
    }

    @Test
    public void constructor_varargs_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Payment((LocalDate[]) null));
    }

    @Test
    public void recordPayment_validDate_returnsNewPaymentWithAddedDate() {
        Payment original = new Payment(DATE_1);
        Payment updated = original.recordPayment(DATE_2);
        assertTrue(updated.getPaidDates().contains(DATE_1));
        assertTrue(updated.getPaidDates().contains(DATE_2));
        assertFalse(original.getPaidDates().contains(DATE_2)); // Original unchanged (immutable)
    }

    @Test
    public void recordPayment_addMultipleDates_returnsPaymentWithAllDates() {
        Payment payment = Payment.EMPTY;
        payment = payment.recordPayment(DATE_1);
        payment = payment.recordPayment(DATE_2);
        payment = payment.recordPayment(DATE_3);
        assertEquals(3, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
        assertTrue(payment.getPaidDates().contains(DATE_2));
        assertTrue(payment.getPaidDates().contains(DATE_3));
    }

    @Test
    public void recordPayment_nullDate_throwsNullPointerException() {
        Payment payment = Payment.EMPTY;
        assertThrows(NullPointerException.class, () -> payment.recordPayment(null));
    }

    @Test
    public void recordPayment_duplicateDate_ignoresDuplicate() {
        Payment payment = new Payment(DATE_1);
        Payment updated = payment.recordPayment(DATE_1);
        assertEquals(1, updated.getPaidDates().size());
    }

    @Test
    public void hasPaidOn_dateInHistory_returnsTrue() {
        Payment payment = new Payment(DATE_1);
        assertTrue(payment.hasPaidOn(DATE_1));
    }

    @Test
    public void hasPaidOn_dateNotInHistory_returnsFalse() {
        Payment payment = new Payment(DATE_1);
        assertFalse(payment.hasPaidOn(DATE_2));
    }

    @Test
    public void hasPaidOn_nullDate_throwsNullPointerException() {
        Payment payment = Payment.EMPTY;
        assertThrows(NullPointerException.class, () -> payment.hasPaidOn(null));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Payment payment = Payment.EMPTY;
        assertTrue(payment.equals(payment));
    }

    @Test
    public void equals_emptyPayments_returnsTrue() {
        Payment payment1 = Payment.EMPTY;
        Payment payment2 = Payment.EMPTY;
        assertTrue(payment1.equals(payment2));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Payment payment1 = new Payment(DATE_1, DATE_2);
        Payment payment2 = new Payment(DATE_1, DATE_2);
        assertTrue(payment1.equals(payment2));
    }

    @Test
    public void equals_differentDates_returnsFalse() {
        Payment payment1 = new Payment(DATE_1);
        Payment payment2 = new Payment(DATE_2);
        assertFalse(payment1.equals(payment2));
    }

    @Test
    public void equals_differentDateCount_returnsFalse() {
        Payment payment1 = new Payment(DATE_1);
        Payment payment2 = new Payment(DATE_2);
        assertFalse(payment1.equals(payment2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Payment payment = Payment.EMPTY;
        assertFalse(payment.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Payment payment = Payment.EMPTY;
        assertFalse(payment.equals("payment"));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Payment payment1 = new Payment(DATE_1);
        Payment payment2 = new Payment(DATE_1);
        assertEquals(payment1.hashCode(), payment2.hashCode());
    }

    @Test
    public void hashCode_differentDates_differentHashCode() {
        Payment payment1 = new Payment(DATE_1);
        Payment payment2 = new Payment(DATE_2);
        assertNotEquals(payment1.hashCode(), payment2.hashCode());
    }

    @Test
    public void toString_validPayment_returnsFormattedString() {
        Payment payment = new Payment(DATE_1);
        String result = payment.toString();
        assertTrue(result.contains("Payment"));
        assertTrue(result.contains(DATE_1.toString()));
    }

    @Test
    public void getPaidDates_returnsUnmodifiableSet() {
        Payment payment = new Payment(DATE_1);
        Set<LocalDate> retrievedDates = payment.getPaidDates();
        assertThrows(UnsupportedOperationException.class, () ->
                retrievedDates.add(DATE_2)); // Should not be able to modify
    }
}
