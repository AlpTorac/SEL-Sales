package view.repository.uifx;

import java.util.Collection;
import java.util.Iterator;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import view.repository.IUIComponent;
import view.repository.IIndexedLayout;

public interface IFXIndexedLayout extends IFXLayout, IIndexedLayout {
	default public int getComponentCount() {
		return ((Pane) this).getChildren().size();
	}
	
	default public int getUIComponentIndex(IUIComponent c) {
		Collection<Node> nodes = ((Pane) this).getChildren();
		Iterator<Node> it = nodes.iterator();
		Node component = (Node) c.getComponent();
		int index = 0;
		while (it.hasNext()) {
			if (component == it.next()) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	default public void setSpacing(double space) {
		((IFXIndexedLayout) this.getComponent()).setSpacing(space);
	}
	
	default public void addUIComponent(int index, IUIComponent c) {
		((Pane) this).getChildren().add(index, (Node) c.getComponent());
	}
	
	default public void addUIComponents(Iterable<IUIComponent> cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
	
	@Override
	default void addUIComponent(IUIComponent c) {
		((Pane) this).getChildren().add((Node) c.getComponent());
	}
}
