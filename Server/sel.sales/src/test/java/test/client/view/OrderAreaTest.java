package test.client.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.model.ClientModel;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.StandardClientView;
import javafx.application.Platform;
import model.dish.DishMenu;
import model.dish.DishMenuItemData;
import model.order.OrderData;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnectionUtility;
import test.external.dummy.DummyStandardClient;
import test.external.dummy.DummyStandardInteraction;
import test.external.dummy.DummyStandardServer;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

class OrderAreaTest extends ApplicationTest {
	private DishMenuItemData item1;
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private DishMenuItemData item2;
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private DishMenuItemData item3;
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serialisedTableNumbers = "1-2,4,5,1-10,90,11";
	
	private StandardClientViewOperationsUtilityClass clientOpHelper;
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
	
	private DishMenu menu;
	
	private volatile boolean actionFinished = false;
	
	private String o1id;
	private String o2id;
	private String o3id;
	private String o4id;
	private String o5id;
	
	private String so1;
	private String so2;
	private String so3;
	private String so4;
	private String so5;
	
	private OrderData od1;
	private OrderData od2;
	private OrderData od3;
	private OrderData od4;
	private OrderData od5;
	private OrderData[] ods;
	
	private void waitForAction() {
		while (!actionFinished) {
			
		}
		actionFinished = false;
	}
	
	private void runFXAction(Runnable run) {
		Platform.runLater(() -> {
			run.run();
			actionFinished = true;
		});
		waitForAction();
	}
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		clientModel = new ClientModel(this.testFolderAddress);
		clientController = new StandardClientController(clientModel);
		
		runFXAction(()->{
			clientView = new StandardClientView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), clientController, clientModel);
			clientView.startUp();
			
			clientOpHelper = new StandardClientViewOperationsUtilityClass(
					clientView, clientController, clientModel);
		});
		
		menu = clientModel.getDishMenuHelper().createDishMenu();
		menu.addMenuItem(clientModel.getDishMenuHelper().createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1id));
		menu.addMenuItem(clientModel.getDishMenuHelper().createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2id));
		menu.addMenuItem(clientModel.getDishMenuHelper().createDishMenuItem(i3Name, i3PorSize, i3ProCost, i3Price, i3id));
		
		clientModel.setDishMenu(clientModel.getDishMenuHelper().dishMenuToData(menu));
		
		item1 = clientModel.getMenuItem(i1id);
		item2 = clientModel.getMenuItem(i2id);
		item3 = clientModel.getMenuItem(i3id);
		
		o1id = "order1";
		o2id = "order2";
		o3id = "order3";
		o4id = "order4";
		o5id = "order5";
		
		so1 = o1id + "#20200809235959890#0#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1;";
		so2 = o2id + "#20200809235959890#0#0:item2,3;item3,5;item1,7;item2,0;item3,1;";
		so3 = o3id + "#20200809235959890#0#0:item3,5;item1,7;item2,0;item3,1;";
		so4 = o4id + "#20200809235959890#0#0:item1,7;item2,0;item3,1;";
		so5 = o5id + "#20200809235959890#0#0:item1,2;item2,3;item3,5;";
		
		od1 = clientModel.getOrderHelper().deserialiseOrderData(so1);
		od2 = clientModel.getOrderHelper().deserialiseOrderData(so2);
		od3 = clientModel.getOrderHelper().deserialiseOrderData(so3);
		od4 = clientModel.getOrderHelper().deserialiseOrderData(so4);
		od5 = clientModel.getOrderHelper().deserialiseOrderData(so5);
		ods = new OrderData[] {od1, od2, od3, od4, od5};
		
		clientModel.addSetting(SettingsField.DISH_MENU_FOLDER, this.testFolderAddress);
		clientModel.addSetting(SettingsField.ORDER_FOLDER, this.testFolderAddress);
		clientModel.addSetting(SettingsField.TABLE_NUMBERS, serialisedTableNumbers);
	}
	
	@AfterEach
	void cleanUp() {
//		clientView.close();
		clientModel.close();
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
	}
	
	private boolean orderDatasEqual(OrderData od1, OrderData od2) {
		od1.flatten();
		od2.flatten();
		return od1.itemsEqual(od2);
	}
	
	@Test
	void orderPersistenceWithRemoveTest() {
		int size = ods.length;
		Integer[] tableNumbers = clientModel.getTableNumbers().toArray(Integer[]::new);
		
		boolean[] isCashArr = new boolean[size];
		boolean[] isHereArr = new boolean[size];
		int[] statusArr = new int[size]; // 1 for cooking, 2 for pending payment, 3 for pending send, 4 for sent
		Integer[] selectedTNS = new Integer[size];
		String[] orderNotes = new String[size];
		boolean[] removeOrderArr = new boolean[size];
		
		int status = 0;
		boolean removeOrder = false;
		
		for (int i = 0; i < size; i++) {
			final int index = i;
			final boolean isCash = GeneralTestUtilityClass.generateRandomBoolean();
			final boolean isHere = GeneralTestUtilityClass.generateRandomBoolean();
			final int tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			final String orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			final OrderData currentData = ods[index];
			final String currentID = currentData.getID().toString();
			
			removeOrder = GeneralTestUtilityClass.generateRandomBoolean();
			status = GeneralTestUtilityClass.generateRandomNumber(1, 4);
			
			runFXAction(()->{
				clientOpHelper.orderTakingAreaDisplayOrder(currentData);
				clientOpHelper.selectTableNumber(tableNumbers[tnIndex]);
				clientOpHelper.setOrderNote(orderNote);
			});
			runFXAction(()->{clientOpHelper.addCookingOrder(currentID);});
			
			switch (status) {
			case 1:
				if (removeOrder) {
					runFXAction(()->{clientOpHelper.cookingOrderAreaRemove(currentID);});
				}
				break;
			case 2:
				runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
				if (removeOrder) {
					runFXAction(()->{clientOpHelper.pendingPaymentOrderAreaRemove(currentID);});
				}
				break;
			case 3:
				runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
				runFXAction(()->{clientOpHelper.addPendingSendOrder(currentID, isCash, isHere);});
				if (removeOrder) {
					clientModel.removeOrder(currentID);
				}
				break;
			case 4:
				runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
				runFXAction(()->{clientOpHelper.addPendingSendOrder(currentID, isCash, isHere);});
				runFXAction(()->{clientModel.makeSentOrder(currentID);});
				if (removeOrder) {
					clientModel.removeOrder(currentID);
				}
				break;
			}
			
			isCashArr[index] = isCash;
			isHereArr[index] = isHere;
			statusArr[index] = status;
			selectedTNS[index] = tableNumbers[tnIndex];
			orderNotes[index] = orderNote;
			removeOrderArr[index] = removeOrder;
			
			clientModel.clearAllOrders();
			clientModel.loadSaved();
			
			for (int indx = 0; indx < index+1; indx++) {
				Assertions.assertTrue(this.orderDatasEqual(clientModel.getOrder(ods[indx].getID().toString()), ods[indx]));
				
				switch (statusArr[indx]) {
				case 1:
					if (removeOrderArr[indx]) {
						Assertions.assertFalse(clientModel.isOrderValid(ods[indx].getID().toString()));
					} else {
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getCookingOrder(ods[indx].getID().toString()), ods[indx]));
					}
					break;
				case 2:
					if (removeOrderArr[indx]) {
						Assertions.assertFalse(clientModel.isOrderValid(ods[indx].getID().toString()));
					} else {
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingPaymentOrder(ods[indx].getID().toString()), ods[indx]));
					}
					break;
				case 3:
					if (removeOrderArr[indx]) {
						Assertions.assertFalse(clientModel.isOrderValid(ods[indx].getID().toString()));
					} else {
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingSendOrder(ods[indx].getID().toString()), ods[indx]));
					}
//					System.out.println("expected isCash: " + isCashArr[indx] + " ,actual isCash: " + clientModel.getOrder(ods[indx].getID().toString()).getIsCash());
//					System.out.println("expected isHere: " + isHereArr[indx] + " ,actual isHere: " + clientModel.getOrder(ods[indx].getID().toString()).getIsHere());
					break;
				case 4:
					if (removeOrderArr[indx]) {
						Assertions.assertFalse(clientModel.isOrderValid(ods[indx].getID().toString()));
					} else {
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getSentOrder(ods[indx].getID().toString()), ods[indx]));
					}
//					System.out.println("expected isCash: " + isCashArr[indx] + " ,actual isCash: " + clientModel.getOrder(ods[indx].getID().toString()).getIsCash());
//					System.out.println("expected isHere: " + isHereArr[indx] + " ,actual isHere: " + clientModel.getOrder(ods[indx].getID().toString()).getIsHere());
					break;
				}
				
				if (statusArr[indx] > 2) {
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsCash(), isCashArr[indx]);
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsHere(), isHereArr[indx]);
				}
				
				Assertions.assertEquals(clientModel.getOrderNote(ods[indx].getID().toString()), orderNotes[indx]);
				Assertions.assertEquals(clientModel.getOrderTableNumber(ods[indx].getID().toString()), selectedTNS[indx]);
			}
		}
	}
	
	@Test
	void orderPersistenceTest() {
		int size = ods.length;
		Integer[] tableNumbers = clientModel.getTableNumbers().toArray(Integer[]::new);
		
		boolean[] isCashArr = new boolean[size];
		boolean[] isHereArr = new boolean[size];
		int[] statusArr = new int[size]; // 1 for cooking, 2 for pending payment, 3 for pending send, 4 for sent
		Integer[] selectedTNS = new Integer[size];
		String[] orderNotes = new String[size];
		
		int status = 0;
		
		for (int i = 0; i < size; i++) {
			final int index = i;
			final boolean isCash = GeneralTestUtilityClass.generateRandomBoolean();
			final boolean isHere = GeneralTestUtilityClass.generateRandomBoolean();
			final int tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			final String orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			final OrderData currentData = ods[index];
			final String currentID = currentData.getID().toString();
			
			status = GeneralTestUtilityClass.generateRandomNumber(1, 4);
			
			runFXAction(()->{
				clientOpHelper.orderTakingAreaDisplayOrder(currentData);
				clientOpHelper.selectTableNumber(tableNumbers[tnIndex]);
				clientOpHelper.setOrderNote(orderNote);
			});
			runFXAction(()->{clientOpHelper.addCookingOrder(currentID);});
			
			switch (status) {
				case 1:
					break;
				case 2:
					runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
					break;
				case 3:
					runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
					runFXAction(()->{clientOpHelper.addPendingSendOrder(currentID, isCash, isHere);});
					break;
				case 4:
					runFXAction(()->{clientOpHelper.addPendingPaymentOrder(currentID);});
					runFXAction(()->{clientOpHelper.addPendingSendOrder(currentID, isCash, isHere);});
					runFXAction(()->{clientModel.makeSentOrder(currentID);});
					break;
			}

			isCashArr[index] = isCash;
			isHereArr[index] = isHere;
			statusArr[index] = status;
			selectedTNS[index] = tableNumbers[tnIndex];
			orderNotes[index] = orderNote;
			
			clientModel.clearAllOrders();
			clientModel.loadSaved();
			
			for (int indx = 0; indx < index+1; indx++) {
				Assertions.assertTrue(this.orderDatasEqual(clientModel.getOrder(ods[indx].getID().toString()), ods[indx]));
				
				switch (statusArr[indx]) {
				case 1:
					Assertions.assertTrue(this.orderDatasEqual(clientModel.getCookingOrder(ods[indx].getID().toString()), ods[indx]));
					break;
				case 2:
					Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingPaymentOrder(ods[indx].getID().toString()), ods[indx]));
					break;
				case 3:
					Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingSendOrder(ods[indx].getID().toString()), ods[indx]));
//					System.out.println("expected isCash: " + isCashArr[indx] + " ,actual isCash: " + clientModel.getOrder(ods[indx].getID().toString()).getIsCash());
//					System.out.println("expected isHere: " + isHereArr[indx] + " ,actual isHere: " + clientModel.getOrder(ods[indx].getID().toString()).getIsHere());
					break;
				case 4:
					Assertions.assertTrue(this.orderDatasEqual(clientModel.getSentOrder(ods[indx].getID().toString()), ods[indx]));
//					System.out.println("expected isCash: " + isCashArr[indx] + " ,actual isCash: " + clientModel.getOrder(ods[indx].getID().toString()).getIsCash());
//					System.out.println("expected isHere: " + isHereArr[indx] + " ,actual isHere: " + clientModel.getOrder(ods[indx].getID().toString()).getIsHere());
					break;
				}
				
				if (statusArr[indx] > 2) {
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsCash(), isCashArr[indx]);
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsHere(), isHereArr[indx]);
				}
				
				Assertions.assertEquals(clientModel.getOrderNote(ods[indx].getID().toString()), orderNotes[indx]);
				Assertions.assertEquals(clientModel.getOrderTableNumber(ods[indx].getID().toString()), selectedTNS[indx]);
			}
		}
	}

	@Test
	void orderCycleTest() {
		Integer[] tableNumbers = clientModel.getTableNumbers().toArray(Integer[]::new);
		
		for (int i = 0; i < ods.length; i++) {
			final int index = i;
			final boolean isCash = GeneralTestUtilityClass.generateRandomBoolean();
			final boolean isHere = GeneralTestUtilityClass.generateRandomBoolean();
			final int tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			final String orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			
			runFXAction(()->{
				clientOpHelper.orderTakingAreaDisplayOrder(ods[index]);
				clientOpHelper.selectTableNumber(tableNumbers[tnIndex]);
				clientOpHelper.setOrderNote(orderNote);
			});
//			GeneralTestUtilityClass.performWait(100);
			OrderData d = clientOpHelper.getOrderTakingAreaCurrentOrder();
			d.flatten();
			Assertions.assertTrue(this.orderDatasEqual(ods[index], d));
			
			Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addCookingOrder(ods[index].getID().toString());});
			
//			while (clientOpHelper.getCookingOrders().size() < index + 1) {
//				
//			}
//			Assertions.assertTrue(clientOpHelper.getCookingOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
//			GeneralTestUtilityClass.performWait(100);
			Assertions.assertTrue(this.orderDatasEqual(clientModel.getCookingOrder(ods[index].getID().toString()), ods[index]));
			Assertions.assertEquals(clientModel.getOrderNote(ods[index].getID().toString()), orderNote);
			Assertions.assertEquals(clientModel.getOrderTableNumber(ods[index].getID().toString()), tableNumbers[tnIndex]);
			
			while (!clientModel.isOrderWritten(ods[index].getID().toString())) {
				
			}
			
			Assertions.assertEquals(clientModel.getAllCookingOrders().length, 1);
			Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, i+1);
			
			runFXAction(()->{clientOpHelper.addPendingPaymentOrder(ods[index].getID().toString());});
			
//			while (clientOpHelper.getPendingPaymentOrders().size() < index + 1) {
//				
//			}
//			Assertions.assertTrue(clientOpHelper.getPendingPaymentOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
//			GeneralTestUtilityClass.performWait(100);
			Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingPaymentOrder(ods[index].getID().toString()), ods[index]));
			Assertions.assertEquals(clientModel.getOrderNote(ods[index].getID().toString()), orderNote);
			Assertions.assertEquals(clientModel.getOrderTableNumber(ods[index].getID().toString()), tableNumbers[tnIndex]);
			
			Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 1);
			Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, i+1);
			
			runFXAction(()->{clientOpHelper.addPendingSendOrder(ods[index].getID().toString(), isCash, isHere);});
			
//			while (clientOpHelper.getPendingSendOrders().size() < index + 1) {
//				
//			}
//			Assertions.assertTrue(clientOpHelper.getPendingSendOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
//			GeneralTestUtilityClass.performWait(100);
			Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingSendOrder(ods[index].getID().toString()), ods[index]));
			Assertions.assertEquals(clientModel.getOrderNote(ods[index].getID().toString()), orderNote);
			Assertions.assertEquals(clientModel.getOrderTableNumber(ods[index].getID().toString()), tableNumbers[tnIndex]);
			
			Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 1);
			Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, i+1);
			
			clientModel.orderSent(ods[index].getID().toString());
			
			Assertions.assertTrue(this.orderDatasEqual(clientModel.getSentOrder(ods[index].getID().toString()), ods[index]));
			Assertions.assertEquals(clientModel.getOrderNote(ods[index].getID().toString()), orderNote);
			Assertions.assertEquals(clientModel.getOrderTableNumber(ods[index].getID().toString()), tableNumbers[tnIndex]);
			
			Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
			Assertions.assertEquals(clientModel.getAllSentOrders().length, index+1);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, i+1);
		}
	}
}
