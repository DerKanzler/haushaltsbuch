package at.kanzler.haushaltsbuch.widgets;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import at.kanzler.haushaltsbuch.bean.Buchung;

public class BookingsSorter extends ViewerSorter {

    /**
     * Constructor argument values that indicate to sort items by
     * buchdat, buchtext, buchetr.
     */

    public final static int BUCHDAT_UP = 1;
    public final static int BUCHDAT_DOWN = 2;
    public final static int BUCHTEXT_UP = 3;
    public final static int BUCHTEXT_DOWN = 4;
    public final static int BUCHBETR_UP = 5;
    public final static int BUCHBETR_DOWN = 6;
    public final static int KOART_UP = 7;
    public final static int KOART_DOWN = 8;
    public final static int KONTOVON_UP = 9;
    public final static int KONTOVON_DOWN = 10;
    public final static int KONTONACH_UP = 11;
    public final static int KONTONACH_DOWN = 12;
    public final static int EINDAT_DOWN = 13;

    // Criteria that the instance uses 
    private int criteria;

    /**
     * Creates a resource sorter that will use the given sort criteria.
     *
     * @param criteria the sort criterion to use: one of <code>NAME</code> or 
     *   <code>TYPE</code>
     */

    public BookingsSorter(int criteria) {
        super();
        this.criteria = criteria;
    }

    /* (non-Javadoc)
     * Method declared on ViewerSorter.
     */
    public int compare(Viewer viewer, Object o1, Object o2) {
        Buchung booking1 = (Buchung) o1;
        Buchung booking2 = (Buchung) o2;

        switch (criteria) {
            case BUCHDAT_UP:
                return compareBookDate(booking1, booking2);

            case BUCHDAT_DOWN:
                return 0 - compareBookDate(booking1, booking2);

            case BUCHTEXT_UP:
                return compareBookText(booking1, booking2);

            case BUCHTEXT_DOWN:
                return 0 - compareBookText(booking1, booking2);

            case BUCHBETR_UP:
                return compareAmount(booking1, booking2);

            case BUCHBETR_DOWN:
                return 0 - compareAmount(booking1, booking2);

            case KOART_UP:
                return compareKoart(booking1, booking2);

            case KOART_DOWN:
                return 0 - compareKoart(booking1, booking2);

            case KONTOVON_UP:
                return compareKontoVon(booking1, booking2);

            case KONTOVON_DOWN:
                return 0 - compareKontoVon(booking1, booking2);

            case KONTONACH_UP:
                return compareKontoNach(booking1, booking2);

            case KONTONACH_DOWN:
                return 0 - compareKontoNach(booking1, booking2);

            case EINDAT_DOWN:
                return 0 - compareInputDate(booking1, booking2);

            default:
                return 0;
        }

    }

    /**
     * Returns a number reflecting the collation order of the given bookings
     * based on the percent completed.
     *
     * @param booking1
     * @param booking2
     * @return a negative number if the first element is less  than the 
     *  second element; the value <code>0</code> if the first element is
     *  equal to the second element; and a positive number if the first
     *  element is greater than the second element
     */
    private int compareAmount(Buchung booking1, Buchung booking2) {
        return booking1.getBuchbetr().compareTo(booking2.getBuchbetr());
    }

    private int compareBookDate(Buchung booking1, Buchung booking2) {
        return booking1.getBuchdat().compareTo(booking2.getBuchdat());
    }

    private int compareBookText(Buchung booking1, Buchung booking2) {
        return booking1.getBuchtext().compareToIgnoreCase(booking2.getBuchtext());
    }

    private int compareKoart(Buchung booking1, Buchung booking2) {
        if (booking1.getKoart() == null && booking2.getKoart() == null) {
            return 0;
        } else if (booking1.getKoart() == null) {
            return -1;
        } else if (booking2.getKoart() == null) {
            return 1;
        } else
            return booking1.getKoart().getKoartkubez().compareToIgnoreCase(booking2.getKoart().getKoartkubez());
    }

    private int compareKontoVon(Buchung booking1, Buchung booking2) {
        if (booking1.getKontovon() == null && booking2.getKontovon() == null) {
            return 0;
        } else if (booking1.getKontovon() == null) {
            return -1;
        } else if (booking2.getKontovon() == null) {
            return 1;
        } else
            return booking1.getKontovon().getKuerzel().compareToIgnoreCase(booking2.getKontovon().getKuerzel());
    }

    private int compareKontoNach(Buchung booking1, Buchung booking2) {
        if (booking1.getKontonach() == null && booking2.getKontonach() == null) {
            return 0;
        } else if (booking1.getKontonach() == null) {
            return -1;
        } else if (booking2.getKontonach() == null) {
            return 1;
        } else
            return booking1.getKontonach().getKuerzel().compareToIgnoreCase(booking2.getKontonach().getKuerzel());
    }

    private int compareInputDate(Buchung booking1, Buchung booking2) {
        return booking1.getBuchung().compareTo(booking2.getBuchung());
    }

    /**
     * Returns the sort criteria of this this sorter.
     *
     * @return the sort criterion
     */
    public int getCriteria() {
        return criteria;
    }

}