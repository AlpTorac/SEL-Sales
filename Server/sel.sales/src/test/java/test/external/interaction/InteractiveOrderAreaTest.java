package test.external.interaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.view.IClientView;
import model.order.OrderData;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnectionUtility;
import test.external.dummy.DummyStandardClient;
import test.external.dummy.DummyStandardInteraction;
import test.external.dummy.DummyStandardServer;

//@Disabled("Takes too long to finish")
//@Execution(value = ExecutionMode.SAME_THREAD)
class InteractiveOrderAreaTest extends FXTestTemplate {
	private DummyStandardServer server;
	private DummyConnectionUtility connUtilServer;
	
	private DummyStandardClient client;
	private DummyConnectionUtility connUtilClient;
	
	private DummyStandardInteraction interaction;
	
	private IClientView clientView;
	
	private String o4id;
	private String o5id;
//	private String[] oids;
	
//	private String so1;
//	private String so2;
//	private String so3;
//	private String so4;
//	private String so5;
//	private String[] sos;
	
	private OrderData od1;
	private OrderData od2;
	private OrderData od3;
	private OrderData od4;
	private OrderData od5;
	private OrderData[] ods;
	
	@BeforeEach
	void prep() {
		this.cleanTestFolder();
		this.connUtilServer = new DummyConnectionUtility(this.serverAddress);
		this.connUtilClient = new DummyConnectionUtility(this.clientAddress);
		
		server = new DummyStandardServer(serverName, serverAddress, connUtilServer);
		client = new DummyStandardClient(clientName, clientAddress, connUtilClient);
		
		interaction = new DummyStandardInteraction(server, client);
		interaction.connectPartakers(server, client);
		
		this.initDishMenuItems(server.getModel());
		this.initDishMenu();
		
		this.getPrivateFieldsFromModel(client.getModel());
		
		interaction.broadcastMenu(menu);
		
		o4id = "order4";
		o5id = "order5";
		
		od1 = client.getModel().getOrderFactory().constructData(o1id, client.getModel().getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809112233343",client.getModel())), false, false);
		od1.addOrderItem(iData1, o1a1);
		
		od2 = client.getModel().getOrderFactory().constructData(o2id, client.getModel().getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111",client.getModel())), true, false);
		od2.addOrderItem(iData1, o2a1);
		od2.addOrderItem(iData2, o2a2);
		
		od3 = client.getModel().getOrderFactory().constructData(o3id, client.getModel().getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222",client.getModel())), true, true);
		od3.addOrderItem(iData3, o3a3);
		
		od4 = client.getModel().getOrderFactory().constructData(o4id, client.getModel().getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111",client.getModel())), true, false);
		od4.addOrderItem(iData1, o2a1);
		od4.addOrderItem(iData3, o3a3);
		
		od5 = client.getModel().getOrderFactory().constructData(o5id, client.getModel().getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222",client.getModel())), true, true);
		od5.addOrderItem(iData3, o3a3);
		od5.addOrderItem(iData2, o2a2);
		od5.addOrderItem(iData1, o1a1);
		
		ods = new OrderData[] {od1, od2, od3, od4, od5};
		
		runFXAction(()->{
			clientView = this.initClientView(client.getModel(), client.getController());
			clientView.startUp();
			
			this.setClientOpHelper(client.getModel(), client.getController(), clientView);
		});
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(interaction);
	}
	
	private boolean orderDatasEqual(OrderData od1, OrderData od2) {
		return GeneralTestUtilityClass.arrayContentEquals(od1.getOrderedItems(), od2.getOrderedItems())
				&& od1.getID().serialisedIDequals(od2.getID().toString());
	}
	
	@SuppressWarnings("resource")
	@Test
	void orderCycleTest() {
		for (int i = 0; i < ods.length; i++) {
			final int index = i;
			final boolean isCash = GeneralTestUtilityClass.generateRandomBoolean();
			final boolean isHere = GeneralTestUtilityClass.generateRandomBoolean();
			
			runFXAction(()->{clientOpHelper.orderTakingAreaDisplayOrder(ods[index]);});
			Assertions.assertTrue(this.orderDatasEqual(ods[i], clientOpHelper.getOrderTakingAreaCurrentOrder()));
			
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addCookingOrder(ods[index].getID().toString());});
			
//			Assertions.assertTrue(clientOpHelper.getCookingOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));

			Assertions.assertTrue(client.getModel().getCookingOrder(ods[i].getID().toString()) != null);
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addPendingPaymentOrder(ods[index].getID().toString());});
//			Assertions.assertTrue(clientOpHelper.getPendingPaymentOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
			
			Assertions.assertTrue(client.getModel().getPendingPaymentOrder(ods[i].getID().toString()) != null);
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addPendingSendOrder(ods[index].getID().toString(), isCash, isHere);});
//			Assertions.assertTrue(clientOpHelper.getPendingSendOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
			
			Assertions.assertTrue(client.getModel().getAllPendingSendOrders().length == 1
					|| client.getModel().getSentOrder(ods[i].getID().toString()) != null);
			
			while ((server.getModel().getOrder(ods[i].getID().toString()) == null ||
					client.getModel().getSentOrder(ods[i].getID().toString()) == null)
					&& !client.getExternal().getConnection(serverAddress).isClosed()) {
//				if (server.getModel().getOrder(ods[i].getID().toString()) == null) {
//					client.getModel().removeOrder(ods[i].getID().toString());
//				}
//				runFXAction(()->{clientOpHelper.orderTakingAreaDisplayOrder(ods[index]);});
//				runFXAction(()->{clientOpHelper.addCookingOrder(ods[index].getID().toString());});
//				runFXAction(()->{clientOpHelper.addPendingPaymentOrder(ods[index].getID().toString());});
//				runFXAction(()->{clientOpHelper.addPendingSendOrder(ods[index].getID().toString(), isCash, isHere);});
//				client.getExternal().refreshOrders();
			}
			
			this.orderDatasEqual(server.getModel().getOrder(ods[i].getID().toString()), ods[i]);
			
			System.out.println("------------------------------- order " + index + " sent -------------------------------");
		}
	}
}
