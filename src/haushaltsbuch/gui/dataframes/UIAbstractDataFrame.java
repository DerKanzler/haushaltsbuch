package haushaltsbuch.gui.dataframes;

import org.eclipse.swt.widgets.Composite;

public abstract class UIAbstractDataFrame extends Composite {

	public UIAbstractDataFrame(Composite parent, int style) {
		super(parent, style);
	}
	
	private Boolean toBeUpdated = false;
	
	public abstract void activate();
	
	public abstract void deactivate();
	
	public void setToBeUpdated(Boolean b) {
		toBeUpdated = b;
	}
	
	public Boolean hasToBeUpdated() {
		return toBeUpdated;
	}
	
	public abstract void update();

}