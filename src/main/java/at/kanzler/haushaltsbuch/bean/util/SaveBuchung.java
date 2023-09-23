package at.kanzler.haushaltsbuch.bean.util;

import java.time.LocalDate;
import java.util.Vector;

public class SaveBuchung {

    private Boolean success = false;
    private Vector<LocalDate> monthEnds = new Vector<LocalDate>();
    private Vector<LocalDate> yearEnds = new Vector<LocalDate>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Vector<LocalDate> getMonthEnds() {
        return monthEnds;
    }

    public void addMonthEnd(LocalDate monthEnd) {
        this.monthEnds.add(monthEnd);
    }

    public Vector<LocalDate> getYearEnds() {
        return yearEnds;
    }

    public void addYearEnd(LocalDate yearEnd) {
        this.yearEnds.add(yearEnd);
    }

}