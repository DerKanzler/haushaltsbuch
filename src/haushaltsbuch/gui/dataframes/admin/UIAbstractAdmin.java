package haushaltsbuch.gui.dataframes.admin;

import org.eclipse.swt.widgets.Composite;

public abstract class UIAbstractAdmin extends Composite {

	public UIAbstractAdmin(Composite parent, Integer style) {
		super(parent, style);
	}
	
	public abstract void lock();
	
	public abstract void unlock();
	
	public abstract void remove(Composite aac);
	
	public abstract void resize();
	
	public abstract void update();
	
}