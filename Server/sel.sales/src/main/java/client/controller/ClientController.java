package client.controller;

import client.controller.handler.AddPendingPaymentOrderHandler;
import client.controller.handler.AvailableTableNumbersReceivedHandler;
import client.controller.handler.EditOrderHandler;
import client.controller.handler.MenuReceivedHandler;
import client.controller.handler.OrderSentHandler;
import client.controller.handler.SendOrderHandler;
import client.model.IClientModel;
import controller.Controller;
import controller.manager.IApplicationEventManager;

public abstract class ClientController extends Controller implements IClientController {

	public ClientController(IClientModel model) {
		super(model);
	}

	@Override
	public IClientModel getModel() {
		return (IClientModel) super.getModel();
	}

	@Override
	protected IApplicationEventManager initEventManager() {
		IApplicationEventManager bem = super.initEventManager();
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.ADD_PENDING_PAYMENT_ORDER, new AddPendingPaymentOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.SEND_ORDER, new SendOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.EDIT_ORDER, new EditOrderHandler(this));
		
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.ORDER_SENT, new OrderSentHandler(this));
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.MENU_RECEIVED, new MenuReceivedHandler(this));
		bem.addApplicationEventToHandlerMapping(ClientSpecificEvent.AVAILABLE_TABLE_NUMBERS_RECEIVED, new AvailableTableNumbersReceivedHandler(this));
		return bem;
	}
}
