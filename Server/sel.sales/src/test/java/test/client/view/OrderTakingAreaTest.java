package test.client.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.IClientView;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;

//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderTakingAreaTest extends FXTestTemplate {
	private IServerModel serverModel;
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
//	private DummyClientExternal clientExternal;
	
	private OrderData data;
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			clientModel = this.initClientModel();
			clientController = this.initClientController(clientModel);
			clientView = this.initClientView(clientModel, clientController);
//			clientExternal = new DummyClientExternal(serviceID, serviceName, clientController, clientModel);
			
			serverModel = this.initServerModel();
			this.initDishMenuItems(serverModel);
			this.addDishMenuToServerModel(serverModel);
			this.getServerMenuIntoClient(serverModel, clientModel);
			clientView.refreshMenu();
			
			this.getPrivateFieldsFromModel(clientModel);
			
//			data = clientModel.getOrderHelper().deserialiseOrderData("order2#20200809235959866#1#0:item1,2;item1,2;item1,2;item1,2;item2,3;");
			data = clientModel.getOrderFactory().constructData(o2id, clientModel.getDateSettings()
					.parseDate(this.serialiseDate("20200809235959866")), true, false);
			data.addOrderItem(iData1, o1a1);
			data.addOrderItem(iData1, o1a1);
			data.addOrderItem(iData1, o1a1);
			data.addOrderItem(iData1, o1a1);
			data.addOrderItem(iData2, o2a2);
			
			this.setClientOpHelper(clientModel, clientController, clientView);
		});
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
	}

	@Test
	void orderInputTest() {
		Assertions.assertEquals(clientOpHelper.getOrderTakingAreaEntryCount(), 0);
		
		// 2x iData2
		runFXAction(()->{clientOpHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(clientOpHelper.getOrderTakingAreaEntryCount(), 1);
		runFXAction(()->{
			clientOpHelper.orderTakingAreaSetEntryItem(iData2, 0);
			clientOpHelper.orderTakingAreaIncEntryAmount(0);
		});
		
		// 2x iData2, 1x iData3
		runFXAction(()->{clientOpHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(clientOpHelper.getOrderTakingAreaEntryCount(), 2);
		runFXAction(()->{
			clientOpHelper.orderTakingAreaSetEntryItem(iData3, 1);
		});
		
		// 2x iData1, 2x iData2, 1x iData3
		runFXAction(()->{clientOpHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(clientOpHelper.getOrderTakingAreaEntryCount(), 3);
		runFXAction(()->{
			clientOpHelper.orderTakingAreaSetEntryItem(iData1, 2);
			clientOpHelper.orderTakingAreaIncEntryAmount(2);
		});
		
		// 2x iData1, 1x iData1, 1x iData3
		runFXAction(()->{
			clientOpHelper.orderTakingAreaSetEntryItem(iData1, 0);
			clientOpHelper.orderTakingAreaDecEntryAmount(0);
		});
		
		// 2x iData1, 1x iData1, 1x iData3
		runFXAction(()->{clientOpHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(clientOpHelper.getOrderTakingAreaEntryCount(), 4);
		runFXAction(()->{
			clientOpHelper.orderTakingAreaSetEntryItem(iData2, 3);
		});
		runFXAction(()->{clientOpHelper.orderTakingAreaRemoveEntry(3);});
		
		AccumulatingAggregateEntry<DishMenuItemData> d1 = new AccumulatingAggregateEntry<DishMenuItemData>(iData1, BigDecimal.valueOf(2));
		AccumulatingAggregateEntry<DishMenuItemData> d2 = new AccumulatingAggregateEntry<DishMenuItemData>(iData1, BigDecimal.valueOf(1));
		AccumulatingAggregateEntry<DishMenuItemData> d3 = new AccumulatingAggregateEntry<DishMenuItemData>(iData3, BigDecimal.valueOf(1));
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getOrderTakingAreaCurrentOrderItems(),
				new AccumulatingAggregateEntry<?>[] {d1,d2,d3});
		
		GeneralTestUtilityClass.arrayContentEquals(orderDAO.parseValueObject(clientOpHelper.getOrderTakingAreaSerialisedOrder()).getOrderedItems(),
				new AccumulatingAggregateEntry<?>[] {d1,d2,d3});
	}
	
	@Test
	void displayOrderTest() {
		runFXAction(()->{clientOpHelper.orderTakingAreaDisplayOrder(data);});
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getOrderTakingAreaCurrentOrderItems(),
				data.getOrderedItems());
		
		GeneralTestUtilityClass.arrayContentEquals(orderDAO.parseValueObject(clientOpHelper.getOrderTakingAreaSerialisedOrder()).getOrderedItems(),
				data.getOrderedItems());
	}
}
