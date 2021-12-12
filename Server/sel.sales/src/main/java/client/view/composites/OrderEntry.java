package client.view.composites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import view.repository.IChoiceBox;
import view.repository.IHBoxLayout;
import view.repository.IIndexedLayout;
import view.repository.ILabel;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class OrderEntry extends UIHBoxLayout implements PriceUpdateTarget<MenuItemEntry>, Cloneable {
	private static final boolean DEFAULT_IS_CASH = false;
	private static final boolean DEFAULT_IS_HERE = false;
	
	private IController controller;
	private UIComponentFactory fac;
	private PriceUpdateTarget<OrderEntry> notifyTarget;
	
	private OrderData activeData;
	private DishMenuData activeMenu;
	
	private List<MenuItemEntry> menuItemEntries;
	
	private IIndexedLayout bottomPart;
	private ILabel priceDisplay;
	
	private String priceDisplayAffix = "Price: ";
	private String tableNumberLabelCaption = "Table Number: ";
	
	private IIndexedLayout orderItemArea;
	
	private IHBoxLayout tableNumberArea;
	private IChoiceBox<Integer> tableNumberCB;
	private ILabel tableNumberLabel;
	
	private ITextBox noteBox;
	
	public OrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget) {
		super(fac.createHBoxLayout().getComponent());
		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		this.controller = controller;
		this.fac = fac;
		this.notifyTarget = notifyTarget;
		this.orderItemArea = this.fac.createVBoxLayout().getComponent();
		this.orderItemArea.setSpacing(10);
		this.setMarigins(10, 0, 0, 0);
		this.setSpacing(10);
		this.addUIComponent(this.orderItemArea);
		this.addComponentsVertically(this.priceDisplay = this.initPriceDisplay());
		this.addTableNumberArea();
		this.addBottomPart();
		this.addUIComponent(this.noteBox = this.initNoteBox());
		this.noteBoxSetupExtras();
	}
	
	protected void noteBoxSetupExtras() {
		this.noteBox.setEnabled(false);
	}
	
	protected ITextBox initNoteBox() {
		ITextBox tb = this.getUIFactory().createTextBox();
		tb.setWrapText(true);
		tb.setPlaceholderText("Order Note");
		tb.setPrefWidth(200);
		return tb;
	}

	public OrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget, OrderData data) {
		this(controller, fac, notifyTarget);
		this.activeData = data;
		this.displayData(this.activeData);
	}
	
	protected IHBoxLayout initTableNumberArea() {
		IHBoxLayout tna = this.getUIFactory().createHBoxLayout();
		
		tna.addUIComponents(new IUIComponent[] {
				this.tableNumberLabel = this.initTableNumberLabel(),
			this.tableNumberCB = this.initTableNumberChoiceBox()
		});
		
		this.tableNumberChoiceBoxSetup();
		
		return tna;
	}
	
	protected ILabel initTableNumberLabel() {
		ILabel lbl = this.getUIFactory().createLabel();
		lbl.setCaption(this.tableNumberLabelCaption);
		return lbl;
	}

	protected IChoiceBox<Integer> initTableNumberChoiceBox() {
		IChoiceBox<Integer> cb = this.getUIFactory().createChoiceBox();
		Collection<Integer> col = this.getController().getModel().getTableNumbers();
		if (col != null) {
			cb.addItems(col.toArray(Integer[]::new));
		}
		return cb;
	}
	
	public void orderSentToNextTab() {
		this.resetUserInput();
	}
	
	protected void tableNumberChoiceBoxSetup() {
		this.tableNumberCB.setOpacity(1);
		this.tableNumberCB.setEnabled(false);
	}
	
	protected void addTableNumberArea() {
		this.addComponentsVertically(this.tableNumberArea = this.initTableNumberArea());
	}
	
	protected void refreshTableNumbers(Collection<Integer> tableNumbers) {
		if (tableNumbers != null) {
			this.tableNumberCB.clear();
			this.tableNumberCB.addItemsIfNotPresent(tableNumbers.toArray(Integer[]::new));
		}
	}
	
	protected IIndexedLayout getVBoxLayout() {
		return this.orderItemArea;
	}
	
	protected void addComponentsVertically(IUIComponent... components) {
		this.getVBoxLayout().addUIComponents(components);
	}
	
	public List<MenuItemEntry> getEntries() {
		return this.menuItemEntries;
	}
	
	public MenuItemEntry getEntry(int pos) {
		return this.getEntries().get(pos);
	}
	
	protected void setActiveMenu(DishMenuData menu) {
		this.activeMenu = menu;
	}
	
	public DishMenuData getActiveMenu() {
		return this.activeMenu;
	}
	
	public OrderData getActiveData() {
		return this.activeData;
	}
	
	protected void setActiveData(OrderData data) {
		this.activeData = data;
	}
	
	public void displayData(OrderData data) {
		if (data != null) {
			this.resetUserInput();
			this.noteBox.setCaption(data.getNote());
			this.tableNumberCB.artificiallySelectItem(data.getTableNumber());
			this.setActiveData(data);
			this.initMenuItemEntries(this.getActiveData());
		}
	}
	
	protected void addBottomPart() {
		this.addComponentsVertically(this.bottomPart = this.initBottomPart());
	}
	
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = this.fac.createHBoxLayout();		
		return bottom;
	}

	protected ILabel initPriceDisplay() {
		ILabel lbl = this.fac.createLabel();
		lbl.setCaption(this.priceDisplayAffix);
		return lbl;
	}

	protected void initMenuItemEntries(OrderData data) {
		for (AccumulatingAggregateEntry<DishMenuItemData> itemData : data.getOrderedItems()) {
			this.addMenuItemEntry(itemData);
		}
	}
	
	protected void addMenuItemEntry(AccumulatingAggregateEntry<DishMenuItemData> data) {
		if (data.getAmount().doubleValue() > 0) {
			this.addMenuItemEntry(this.createItemEntry(data));
		};
	}
	
	protected void addMenuItemEntry(MenuItemEntry entry) {
		this.getEntries().add(entry);
		this.refreshMenu(this.getActiveMenu());
		this.addMenuItemEntryToUI(entry);
		this.refreshPriceDisplay();
	}
	
	protected void addMenuItemEntryToUI(MenuItemEntry entry) {
		this.getVBoxLayout().addUIComponentBefore(entry, this.bottomPart);
	}
	
	protected MenuItemEntry createItemEntry() {
		return new MenuItemEntry(fac, this);
	}
	
	protected MenuItemEntry createItemEntry(AccumulatingAggregateEntry<DishMenuItemData> data) {
		MenuItemEntry e  = this.createItemEntry();
		e.displayData(data);
		return e;
	}
	
	@Override
	public void refreshPrice() {
		this.refreshPriceDisplay();
		this.notifyTarget.refreshPrice();
	}

	public void removeFromParent() {
		this.notifyTarget.remove(this);
		this.dettach();
	}
	
	@Override
	public void remove(MenuItemEntry referenceOfCaller) {
		this.getEntries().remove(referenceOfCaller);
		if (this.getEntries().isEmpty()) {
			this.noEntryAction();
		}
	}
	
	protected void noEntryAction() {
		
	}
	
	public void refreshMenu(DishMenuData menuData) {
		if (menuData != null) {
			this.setActiveMenu(menuData);
			this.getEntries().forEach(mie -> mie.refreshMenu(this.getActiveMenu()));
			this.getNotifyTarget().refreshPrice();
		}
	}
	
	protected void refreshPriceDisplay() {
		this.priceDisplay.setCaption(this.priceDisplayAffix + this.getNetPriceForDisplay());
	}
	
	protected String getNetPriceForDisplay() {
		return this.getNetPrice().toPlainString();
	}
	
	public BigDecimal getNetPrice() {
		return this.getEntries().stream().map(mie -> mie.getPrice()).reduce(BigDecimal.ZERO, (p1,p2)-> p1.add(p2));
	}
	
	public String getSerialisedOrderID() {
		return this.getActiveData().getID().toString();
	}
	
	protected boolean isCash() {
		return DEFAULT_IS_CASH;
	}
	
	protected boolean isHere() {
		return DEFAULT_IS_HERE;
	}
	
	public OrderData getCurrentOrder() {
		OrderData data = this.getController().getModel().getOrderFactory()
				.constructData(
						this.getSerialisedOrderID(),
						LocalDateTime.now(),
						this.isCash(),
						this.isHere());
		
		this.getEntries().stream()
		.filter(mie -> mie.getSelectedMenuItem() != null && mie.getAmount().compareTo(BigDecimal.ZERO) != 0)
		.forEach(mie -> data.addOrderItem(mie.getSelectedMenuItem(), mie.getAmount()));
		
		data.setAttributeValue(OrderAttribute.TABLE_NUMBER, this.getTableNumberSelection());
		data.setAttributeValue(OrderAttribute.NOTE, this.getCurrentOrderNote());
		
		return data;
	}
	
	public void resetUserInput() {
		for (MenuItemEntry mie : this.getEntries().toArray(MenuItemEntry[]::new)) {
			mie.removeFromParent();
		}
		this.refreshPrice();
		this.noteBox.clearText();
		this.activeData = null;
	}
	
	public Collection<MenuItemEntry> cloneMenuItemEntries() {
		Collection<MenuItemEntry> col = new CopyOnWriteArrayList<MenuItemEntry>();
		this.getEntries().forEach(mie -> {col.add(mie.clone());});
		return col;
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	protected PriceUpdateTarget<OrderEntry> getNotifyTarget() {
		return this.notifyTarget;
	}
	
	protected OrderEntry constructClone() {
		return new OrderEntry(this.getController(), this.getUIFactory(), this.getNotifyTarget());
	}
	
	@Override
	public OrderEntry clone() {
		OrderEntry clone = this.constructClone();
		clone.refreshMenu(this.getActiveMenu());
		clone.displayData(this.getActiveData());
		return clone;
	}
	
	public Integer getTableNumberSelection() {
		Integer element = this.tableNumberCB.getSelectedElement();
		if (element != null) {
			return element;
		}
		return null;
	}
	
	public String getCurrentOrderNote() {
		return this.noteBox.getText();
	}
	
	protected IChoiceBox<Integer> getTableNumberChoiceBox() {
		return this.tableNumberCB;
	}
	
	protected ITextBox getOrderNoteBox() {
		return this.noteBox;
	}
	
	@Override
	public String toString() {
		if (this.getActiveData() != null) {
			return this.getActiveData().toString();
		} else {
			return super.toString();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof OrderEntry)) {
			return false;
		}
		OrderEntry castedO = (OrderEntry) o;
		OrderData adOD = castedO.getActiveData();
		OrderData thisOD = this.getActiveData();
		if (adOD == null ^ thisOD == null) {
			return false;
		}
		return thisOD.equals(adOD);
	}
}
