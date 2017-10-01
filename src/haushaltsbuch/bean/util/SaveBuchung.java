package haushaltsbuch.bean.util;

import java.util.Date;
import java.util.Vector;

public class SaveBuchung {
	
	private Boolean success = false;
	private Vector<Date> monthEnds = new Vector<Date>();
	private Vector<Date> yearEnds = new Vector<Date>();
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Vector<Date> getMonthEnds() {
		return monthEnds;
	}
	public void addMonthEnd(Date monthEnd) {
		this.monthEnds.add(monthEnd);
	}
	public Vector<Date> getYearEnds() {
		return yearEnds;
	}
	public void addYearEnd(Date yearEnd) {
		this.yearEnds.add(yearEnd);
	}

}