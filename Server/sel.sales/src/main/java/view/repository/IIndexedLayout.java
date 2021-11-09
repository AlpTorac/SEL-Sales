package view.repository;

public interface IIndexedLayout extends ILayout {
	default public int getComponentCount() {
		return ((IIndexedLayout) this.getComponent()).getComponentCount();
	}
	
	/**
	 * @param componentToAdd component that will be added
	 * @param reference component, after which componentToAdd will be added
	 */
	default public void addUIComponentAfter(IUIComponent componentToAdd, IUIComponent reference) {
		int index = this.getUIComponentIndex(reference) + 1;
		int size = this.getComponentCount();
		
		if (index > size) {
			this.addUIComponent(componentToAdd);
		} else {
			this.addUIComponent(index, componentToAdd);
		}
	}
	
	/**
	 * @param componentToAdd component that will be added
	 * @param reference component, before which componentToAdd will be added
	 */
	default public void addUIComponentBefore(IUIComponent componentToAdd, IUIComponent reference) {
		int index = this.getUIComponentIndex(reference) - 1;
		
		if (index < 0) {
			this.addUIComponent(0, componentToAdd);
		} else {
			this.addUIComponent(index, componentToAdd);
		}
	}
	
	default public int getUIComponentIndex(IUIComponent c) {
		return ((IIndexedLayout) this.getComponent()).getUIComponentIndex(c);
	}
	
	default public void setSpacing(double space) {
		((IIndexedLayout) this.getComponent()).setSpacing(space);
	}
	
	default public void addUIComponent(int index, IUIComponent c) {
		((IIndexedLayout) this.getComponent()).addUIComponent(index, c);
	}
	
	default public void addUIComponent(IUIComponent c) {
		((IIndexedLayout) this.getComponent()).addUIComponent(c);
	}
	
	default public void addUIComponents(IUIComponent[] cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
}
