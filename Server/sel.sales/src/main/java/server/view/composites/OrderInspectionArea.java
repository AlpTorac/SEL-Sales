package server.view.composites;

import java.time.LocalDateTime;

import model.IDateSettings;
import model.order.IOrderData;
import model.order.IOrderItemData;
import view.repository.IButton;
import view.repository.IGridLayout;
import view.repository.IHBoxLayout;
import view.repository.ILabel;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderInspectionArea extends UIVBoxLayout {
	private IDateSettings ds;

	private ILabel orderIDLabel;
	private ILabel orderTimeInDayLabel;
	private ILabel orderDateLabel;
	
	private ITable<IOrderItemData> orderDetailsDisplay;
	
	private IRadioButton cashRadioButton;
	private IRadioButton cardRadioButton;
	private IToggleGroup isCash;
	
	private IRadioButton toGoRadioButton;
	private IRadioButton hereRadioButton;
	private IToggleGroup isHere;
	
	private ILabel grossSumDisplay;
	private ILabel discountDisplay;
	private ILabel netSumDisplay;
	
	private IButton addConfirmButton;
	private IButton removeButton;
	private IButton editButton;
	private IButton createNewOrderButton;
	private IButton confirmAllButton;
	
	private UIComponentFactory fac;
	
	public OrderInspectionArea(UIComponentFactory fac, IDateSettings ds) {
		super(fac.createVBoxLayout().getComponent());
		this.ds = ds;
		this.fac = fac;
		this.init();
	}

	public LocalDateTime getDisplayedDate()  {
		return this.ds.parseDate(this.getOrderDateLabel().getText()+this.getOrderTimeInDayLabel().getText());
	}
	
	public void clearOrderDisplay() {
		this.getOrderIDLabel().clearText();
		this.getOrderTimeInDayLabel().clearText();
		this.getOrderDateLabel().clearText();
		this.getOrderDetailsDisplay().clear();
		this.getIsCash().clearSelections();
		this.getIsHere().clearSelections();
		this.getGrossSumDisplay().clearText();
		this.getNetSumDisplay().clearText();
		this.getDiscountDisplay().clearText();
		this.getAddConfirmButton().setEnabled(false);
		this.getRemoveButton().setEnabled(false);
		this.getEditButton().setEnabled(false);
	}
	
	public void displayOrder(IOrderData data) {
		this.displayOrderID(data);
		this.displayOrderDate(data);
		this.displayOrderedItems(data);
		this.displayIsCash(data);
		this.displayIsHere(data);
		this.displayOrderPriceDetails(data);
		this.getAddConfirmButton().setEnabled(true);
		this.getRemoveButton().setEnabled(true);
		this.getEditButton().setEnabled(true);
	}
	
	private void displayOrderID(IOrderData data) {
		this.getOrderIDLabel().setCaption(data.getID().toString());
	}
	
	private void displayOrderPriceDetails(IOrderData data) {
		this.getGrossSumDisplay().setCaption(data.getGrossSum().toPlainString());
		this.getDiscountDisplay().setCaption(data.getOrderDiscount().toPlainString());
		this.getNetSumDisplay().setCaption(data.getNetSum().toPlainString());
	}
	
	private void displayOrderedItems(IOrderData data) {
		this.getOrderDetailsDisplay().clear();
		this.getOrderDetailsDisplay().addItems(data.getOrderedItems());
	}
	
	private void displayOrderDate(IOrderData data) {
		LocalDateTime orderDate = data.getDate();
		this.getOrderTimeInDayLabel().setCaption(this.ds.getTimeInDay(orderDate));
		this.getOrderDateLabel().setCaption(this.ds.getDateInYear(orderDate));
	}
	
	private void displayIsCash(IOrderData data) {
		boolean isCash = data.getIsCash();
		
		this.getCashRadioButton().setToggled(isCash);
		this.getCardRadioButton().setToggled(!isCash);
	}
	
	private void displayIsHere(IOrderData data) {
		boolean isHere = data.getIsHere();
		
		this.getHereRadioButton().setToggled(isHere);
		this.getToGoRadioButton().setToggled(!isHere);
	}
	
	private void init() {
		this.addUIComponents(new IUIComponent[] {
				this.initDisplayLayout(),
				this.initOrderOperationsLayout()
		});
	}
	
	protected IVBoxLayout initDisplayLayout() {
		IVBoxLayout layout = this.fac.createVBoxLayout();
		
		this.orderIDLabel = this.initOrderIDDisplay();
		this.orderTimeInDayLabel = this.initOrderTimeInDayDisplay();
		this.orderDateLabel = this.initOrderDateDisplay();
		this.orderDetailsDisplay = this.initOrderDetailsTable();
		
//		layout.addUIComponent(this.getOrderIDLabel(), 			0, 1, 2, 1);
//		layout.addUIComponent(this.getOrderTimeInDayLabel(), 	2, 0, 3, 1);
//		layout.addUIComponent(this.getOrderDateLabel(), 			2, 1, 3, 1);
//		layout.addUIComponent(this.getOrderDetailsDisplay(), 		0, 2, 7, 1);
//		layout.addUIComponent(this.initPaymentMethodSelection(), 	0, 3, 3, 3);
//		layout.addUIComponent(this.initPlaceSelection(), 			0, 6, 3, 3);
//		layout.addUIComponent(this.initGrossDiscNetSum(), 			3, 3, 3, 5);
		
		IVBoxLayout orderDateInfoLayout = fac.createVBoxLayout();
		orderDateInfoLayout.addUIComponents(new IUIComponent[] {
				this.getOrderTimeInDayLabel(),
				this.getOrderDateLabel()
		});
		
		IHBoxLayout orderInfoLayout = fac.createHBoxLayout();
		orderInfoLayout.setSpacing(30);
		orderInfoLayout.addUIComponents(new IUIComponent[] {
				this.getOrderIDLabel(),
				orderDateInfoLayout
		});
		
		IVBoxLayout orderPaymentAndPlaceLayout = fac.createVBoxLayout();
		orderPaymentAndPlaceLayout.addUIComponents(new IUIComponent[] {
				this.initPaymentMethodSelection(),
				this.initPlaceSelection()
		});
		
		IHBoxLayout orderSummaryLayout = fac.createHBoxLayout();
		orderSummaryLayout.setSpacing(10);
		orderSummaryLayout.addUIComponents(new IUIComponent[] {
				orderPaymentAndPlaceLayout,
				this.initGrossDiscNetSum()
		});
		
		layout.addUIComponents(new IUIComponent[] {
				orderInfoLayout,
				this.getOrderDetailsDisplay(),
				orderSummaryLayout
		});
		
		return layout;
	}
	
	protected ILabel initOrderIDDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("Order N - ID");
		return label;
	}
	
	protected ILabel initOrderTimeInDayDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("hh:mm:ss");
		return label;
	}
	
	protected ILabel initOrderDateDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("dd/mm/yyyy");
		return label;
	}
	
	protected ITable<IOrderItemData> initOrderDetailsTable() {
		ITable<IOrderItemData> table = this.fac.createTable();
		table.addColumn("Menu Item", "ItemData");
		table.addColumn("Amount", "Amount");
		table.addColumn("Gross Price", "GrossPrice");
		return table;
	}
	
	protected IHBoxLayout initPaymentMethodSelection() {
		IHBoxLayout optionArea = this.fac.createHBoxLayout();
		
		this.cashRadioButton = this.fac.createRadioButton();
		this.cashRadioButton.setCaption("Cash");
		this.cashRadioButton.setEnabled(false);
		this.cashRadioButton.setOpacity(1);
		
		this.cardRadioButton = this.fac.createRadioButton();
		this.cardRadioButton.setCaption("Card");
		this.cardRadioButton.setEnabled(false);
		this.cardRadioButton.setOpacity(1);
		
		IRadioButton[] choices = new IRadioButton[] {this.getCardRadioButton(), this.getCashRadioButton()};
		
		this.isCash = this.fac.createToggleGroup();
		this.isCash.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IHBoxLayout initPlaceSelection() {
		IHBoxLayout optionArea = this.fac.createHBoxLayout();
		
		this.toGoRadioButton = this.fac.createRadioButton();
		this.toGoRadioButton.setCaption("To-Go");
		this.toGoRadioButton.setEnabled(false);
		this.toGoRadioButton.setOpacity(1);
		
		this.hereRadioButton = this.fac.createRadioButton();
		this.hereRadioButton.setCaption("Here");
		this.hereRadioButton.setEnabled(false);
		this.hereRadioButton.setOpacity(1);
		
		IRadioButton[] choices = new IRadioButton[] {this.getToGoRadioButton(), this.getHereRadioButton()};
		
		this.isHere = this.fac.createToggleGroup();
		this.isHere.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IVBoxLayout initGrossDiscNetSum() {
		IVBoxLayout sumArea = this.fac.createVBoxLayout();
		
		sumArea.addUIComponents(new IUIComponent[] {
				this.initGrossSumDisplay(),
				this.initDiscDisplay(),
				this.initNetSumDisplay()
		});
		
		return sumArea;
	}
	
	protected IHBoxLayout initDiscDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Discount:");
		
		this.discountDisplay = this.fac.createLabel();
		this.discountDisplay.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				this.getDiscountDisplay()
		});
		
		return area;
	}
	protected IHBoxLayout initNetSumDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Net Sum:");
		
		this.netSumDisplay = this.fac.createLabel();
		this.netSumDisplay.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				this.getNetSumDisplay()
		});
		
		return area;
	}
	protected IHBoxLayout initGrossSumDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Gross Sum:");
		
		this.grossSumDisplay = this.fac.createLabel();
		this.grossSumDisplay.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				this.getGrossSumDisplay()
		});
		
		return area;
	}
	
	protected IGridLayout initOrderOperationsLayout() {
		IGridLayout layout = this.fac.createGridLayout();
		layout.setMarigins(10, 0, 0, 0);
		layout.setHSpacing(50);
		layout.setVSpacing(10);
		
		this.addConfirmButton = this.initOrderAddConfirmButton();
		this.removeButton = this.initOrderRemoveButton();
		this.editButton = this.initOrderEditButton();
		this.createNewOrderButton = this.initOrderNewButton();
		this.confirmAllButton = this.initOrderConfirmAllButton();
		
		layout.addUIComponent(this.getAddConfirmButton(),			0, 0, 1, 1);
		layout.addUIComponent(this.getRemoveButton(), 				3, 0, 1, 1);
		layout.addUIComponent(this.getEditButton(), 				0, 1, 1, 1);
		layout.addUIComponent(this.getCreateNewOrderButton(), 		3, 1, 1, 1);
		layout.addUIComponent(this.getConfirmAllButton(),			0, 2, 3, 1);
		
		return layout;
	}
	
	protected IButton initOrderAddConfirmButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Add / Confirm");
		button.setEnabled(false);
		return button;
	}
	protected IButton initOrderRemoveButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Remove");
		button.setEnabled(false);
		return button;
	}
	protected IButton initOrderEditButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Edit");
		button.setEnabled(false);
		return button;
	}
	protected IButton initOrderNewButton() {
		IButton button = this.fac.createButton();
		button.setCaption("New Order");
		return button;
	}
	protected IButton initOrderConfirmAllButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Confirm All");
		return button;
	}

	public ILabel getOrderIDLabel() {
		return orderIDLabel;
	}

	public ILabel getOrderTimeInDayLabel() {
		return orderTimeInDayLabel;
	}

	public ILabel getOrderDateLabel() {
		return orderDateLabel;
	}

	public ITable<IOrderItemData> getOrderDetailsDisplay() {
		return orderDetailsDisplay;
	}

	public IRadioButton getCashRadioButton() {
		return cashRadioButton;
	}

	public IRadioButton getCardRadioButton() {
		return cardRadioButton;
	}

	public IToggleGroup getIsCash() {
		return isCash;
	}

	public IRadioButton getToGoRadioButton() {
		return toGoRadioButton;
	}

	public IRadioButton getHereRadioButton() {
		return hereRadioButton;
	}

	public IToggleGroup getIsHere() {
		return isHere;
	}

	public ILabel getGrossSumDisplay() {
		return grossSumDisplay;
	}

	public ILabel getDiscountDisplay() {
		return discountDisplay;
	}

	public ILabel getNetSumDisplay() {
		return netSumDisplay;
	}

	public IButton getAddConfirmButton() {
		return addConfirmButton;
	}

	public IButton getRemoveButton() {
		return removeButton;
	}

	public IButton getEditButton() {
		return editButton;
	}

	public IButton getCreateNewOrderButton() {
		return createNewOrderButton;
	}

	public IButton getConfirmAllButton() {
		return confirmAllButton;
	}
}
