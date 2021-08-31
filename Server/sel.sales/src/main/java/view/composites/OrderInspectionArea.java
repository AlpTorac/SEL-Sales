package view.composites;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
	private static String dateInYearSeperator = "/";
	private static String timeInDaySeperator = ":";
	private static DateFormat dateInYearFormat = new SimpleDateFormat("dd"+dateInYearSeperator+"MM"+dateInYearSeperator+"yyyy");
	private static DateFormat timeInDayFormat = new SimpleDateFormat("HH"+timeInDaySeperator+"mm"+timeInDaySeperator+"ss");
	
	private ILabel orderIDLabel;
	private ILabel orderTimeInDayLabel;
	private ILabel orderDateLabel;
	
	private ITable<IOrderItemData> orderDetailsDisplay;
	
	private IRadioButton cashRadioButton;
	private IRadioButton cardRadioButton;
	private IToggleGroup cashOrCard;
	
	private IRadioButton toGoRadioButton;
	private IRadioButton hereRadioButton;
	private IToggleGroup hereOrToGo;
	
	private ILabel grossSumDisplay;
	private ILabel discountDisplay;
	private ILabel netSumDisplay;
	
	private IButton addConfirmButton;
	private IButton removeButton;
	private IButton editButton;
	private IButton createNewOrderButton;
	private IButton confirmAllButton;
	
	private UIComponentFactory fac;
	
	public OrderInspectionArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}

	public void clearOrderDisplay() {
		this.getOrderIDLabel().clearText();
		this.getOrderTimeInDayLabel().clearText();
		this.getOrderDateLabel().clearText();
		this.getOrderDetailsDisplay().clear();
		this.getCashOrCard().clearSelections();
		this.getHereOrToGo().clearSelections();
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
		this.displayCashOrCard(data);
		this.displayHereOrToGo(data);
		this.displayOrderPriceDetails(data);
	}
	
	private void displayOrderID(IOrderData data) {
		this.getOrderIDLabel().setCaption(data.getID().toString());
	}
	
	private void displayOrderPriceDetails(IOrderData data) {
		this.getGrossSumDisplay().setCaption(data.getGrossSum().toPlainString());
		this.getDiscountDisplay().setCaption(data.getTotalDiscount().toPlainString());
		this.getNetSumDisplay().setCaption(data.getNetSum().toPlainString());
	}
	
	private void displayOrderedItems(IOrderData data) {
		this.getOrderDetailsDisplay().clear();
		this.getOrderDetailsDisplay().addItems(data.getOrderedItems());
	}
	
	private void displayOrderDate(IOrderData data) {
		this.getOrderTimeInDayLabel().setCaption(timeInDayFormat.format(data.getDate().getTime()));
		this.getOrderDateLabel().setCaption(dateInYearFormat.format(data.getDate().getTime()));
	}
	
	private void displayCashOrCard(IOrderData data) {
		boolean cashOrCard = data.getCashOrCard();
		
		this.getCashRadioButton().setToggled(cashOrCard);
		this.getCardRadioButton().setToggled(!cashOrCard);
	}
	
	private void displayHereOrToGo(IOrderData data) {
		boolean hereOrToGo = data.getHereOrToGo();
		
		this.getHereRadioButton().setToggled(hereOrToGo);
		this.getToGoRadioButton().setToggled(!hereOrToGo);
	}
	
	private void init() {
		this.addUIComponents(new IUIComponent[] {
				this.initDisplayLayout(),
				this.initOrderOperationsLayout()
		});
	}
	
	protected IGridLayout initDisplayLayout() {
		IGridLayout layout = this.fac.createGridLayout();
		
		this.orderIDLabel = this.initOrderIDDisplay();
		this.orderTimeInDayLabel = this.initOrderTimeInDayDisplay();
		this.orderDateLabel = this.initOrderDateDisplay();
		this.orderDetailsDisplay = this.initOrderDetailsTable();
		
		layout.addUIComponent(this.getOrderIDLabel(), 			0, 1, 2, 1);
		layout.addUIComponent(this.getOrderTimeInDayLabel(), 	2, 0, 3, 1);
		layout.addUIComponent(this.getOrderDateLabel(), 			2, 1, 3, 1);
		layout.addUIComponent(this.getOrderDetailsDisplay(), 		0, 2, 7, 1);
		layout.addUIComponent(this.initPaymentMethodSelection(), 	0, 3, 3, 3);
		layout.addUIComponent(this.initPlaceSelection(), 			0, 6, 3, 3);
		layout.addUIComponent(this.initGrossDiscNetSum(), 			3, 3, 3, 5);
		
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
		table.addColumn("Discount", "TotalDiscount");
		table.addColumn("Net Price", "NetPrice");
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
		
		this.cashOrCard = this.fac.createToggleGroup();
		this.cashOrCard.addAllToToggleGroup(choices);
		
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
		
		this.hereOrToGo = this.fac.createToggleGroup();
		this.hereOrToGo.addAllToToggleGroup(choices);
		
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

	public IToggleGroup getCashOrCard() {
		return cashOrCard;
	}

	public IRadioButton getToGoRadioButton() {
		return toGoRadioButton;
	}

	public IRadioButton getHereRadioButton() {
		return hereRadioButton;
	}

	public IToggleGroup getHereOrToGo() {
		return hereOrToGo;
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

	public static String getDateInYearSeperator() {
		return dateInYearSeperator;
	}

	public static String getTimeInDaySeperator() {
		return timeInDaySeperator;
	}
}
