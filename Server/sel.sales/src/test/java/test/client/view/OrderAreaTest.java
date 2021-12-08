package test.client.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.IClientView;
import model.order.OrderData;
import model.settings.SettingsField;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;

class OrderAreaTest extends FXTestTemplate {
//	private String serialisedTableNumbers = "1-2,4,5,1-10,90,11";
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
	
	private String o4id;
	private String o5id;
	
	private OrderData od1;
	private OrderData od2;
	private OrderData od3;
	private OrderData od4;
	private OrderData od5;
	private OrderData[] ods;
	
	@BeforeEach
	void prep() {
		this.cleanTestFolder();
		clientModel = this.initClientModel();
		clientController = this.initClientController(clientModel);
		
		runFXAction(()->{
			clientView = this.initClientView(clientModel, clientController);
			clientView.startUp();
			
			this.setClientOpHelper(clientModel, clientController, clientView);
		});
		
		this.initDishMenuItems(clientModel);
		this.initDishMenu();
		clientModel.setDishMenu(menu.toData());
		
		o4id = "order4";
		o5id = "order5";
		
		od1 = clientModel.getOrderFactory().constructData(o1id, clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809112233343",clientModel)), false, false);
		od1.addOrderItem(iData1, o1a1);
		
		od2 = clientModel.getOrderFactory().constructData(o2id, clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111",clientModel)), true, false);
		od2.addOrderItem(iData1, o2a1);
		od2.addOrderItem(iData2, o2a2);
		
		od3 = clientModel.getOrderFactory().constructData(o3id, clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222",clientModel)), true, true);
		od3.addOrderItem(iData3, o3a3);
		
		od4 = clientModel.getOrderFactory().constructData(o4id, clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111",clientModel)), true, false);
		od4.addOrderItem(iData1, o2a1);
		od4.addOrderItem(iData3, o3a3);
		
		od5 = clientModel.getOrderFactory().constructData(o5id, clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222",clientModel)), true, true);
		od5.addOrderItem(iData3, o3a3);
		od5.addOrderItem(iData2, o2a2);
		od5.addOrderItem(iData1, o1a1);
		
		ods = new OrderData[] {od1, od2, od3, od4, od5};
		
		clientModel.addSetting(SettingsField.DISH_MENU_FOLDER, this.testFolderAddress);
		clientModel.addSetting(SettingsField.ORDER_FOLDER, this.testFolderAddress);
		clientModel.addSetting(SettingsField.TABLE_NUMBERS, serialisedTableNumbers);
	}
	
	@AfterEach
	void cleanUp() {
//		clientView.close();
		this.closeModel(clientModel);
	}
	
	private boolean orderDatasEqual(OrderData od1, OrderData od2) {
		return GeneralTestUtilityClass.arrayContentEquals(od1.getOrderedItems(), od2.getOrderedItems());
//		return od1.equals(od2);
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
				if (removeOrderArr[indx]) {
					Assertions.assertNull(clientModel.getOrder(ods[indx].getID().toString()));
				} else {
					switch (statusArr[indx]) {
					case 1:
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getCookingOrder(ods[indx].getID().toString()), ods[indx]));
						break;
					case 2:
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingPaymentOrder(ods[indx].getID().toString()), ods[indx]));
						break;
					case 3:
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingSendOrder(ods[indx].getID().toString()), ods[indx]));
						break;
					case 4:
						Assertions.assertTrue(this.orderDatasEqual(clientModel.getSentOrder(ods[indx].getID().toString()), ods[indx]));
						break;
					}
					
					if (statusArr[indx] > 2) {
						Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsCash(), isCashArr[indx]);
						Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getIsHere(), isHereArr[indx]);
					}
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getNote(), orderNotes[indx]);
					Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getTableNumber(), selectedTNS[indx]);
				}
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
				
				Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getNote(), orderNotes[indx]);
				Assertions.assertEquals(clientModel.getOrder(ods[indx].getID().toString()).getTableNumber(), selectedTNS[indx]);
			}
		}
	}

	private void displayOrder(Integer[] tableNumbers, String orderNote, int index, int tnIndex) {
		runFXAction(()->{
			clientOpHelper.orderTakingAreaDisplayOrder(ods[index]);
			clientOpHelper.selectTableNumber(tableNumbers[tnIndex]);
			clientOpHelper.setOrderNote(orderNote);
		});
		
		OrderData d = clientOpHelper.getOrderTakingAreaCurrentOrder();
		Assertions.assertTrue(this.orderDatasEqual(ods[index], d));
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
	}
	
	private void makeCookingOrder(Integer[] tableNumbers, String orderNote, int index, int tnIndex) {
		runFXAction(()->{clientOpHelper.addCookingOrder(ods[index].getID().toString());});
		
		Assertions.assertTrue(this.orderDatasEqual(clientModel.getCookingOrder(ods[index].getID().toString()), ods[index]));
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getNote(), orderNote);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getTableNumber(), tableNumbers[tnIndex]);
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
	}
	
	private void makePendingPaymentOrder(Integer[] tableNumbers, String orderNote, int index, int tnIndex) {
		runFXAction(()->{clientOpHelper.addPendingPaymentOrder(ods[index].getID().toString());});
		
		Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingPaymentOrder(ods[index].getID().toString()), ods[index]));
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getNote(), orderNote);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getTableNumber(), tableNumbers[tnIndex]);
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
	}
	
	private void makePendingSendOrder(Integer[] tableNumbers, String orderNote, int index, int tnIndex, boolean isCash, boolean isHere) {
		runFXAction(()->{clientOpHelper.addPendingSendOrder(ods[index].getID().toString(), isCash, isHere);});
		
		Assertions.assertTrue(this.orderDatasEqual(clientModel.getPendingSendOrder(ods[index].getID().toString()), ods[index]));
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getIsCash(), isCash);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getIsHere(), isHere);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getNote(), orderNote);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getTableNumber(), tableNumbers[tnIndex]);
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
	}
	
	private void makeSentOrder(Integer[] tableNumbers, String orderNote, int index, int tnIndex, boolean isCash, boolean isHere) {
		clientModel.orderSentByID(ods[index].getID().toString());
		
		Assertions.assertTrue(this.orderDatasEqual(clientModel.getSentOrder(ods[index].getID().toString()), ods[index]));
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getIsCash(), isCash);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getIsHere(), isHere);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getNote(), orderNote);
		Assertions.assertEquals(clientModel.getOrder(ods[index].getID().toString()).getTableNumber(), tableNumbers[tnIndex]);
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index+1);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
	}
	
	private void editCookingOrder(int index) {
		runFXAction(()->{clientOpHelper.editCookingOrder(ods[index].getID().toString());});
		
		OrderData d = clientOpHelper.getOrderTakingAreaCurrentOrder();
		Assertions.assertTrue(this.orderDatasEqual(ods[index], d));
		Assertions.assertTrue(this.orderDatasEqual(clientModel.getEditTarget(), d));
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
	}
	
	private void editPendingPaymentOrder(int index) {
		runFXAction(()->{clientOpHelper.editPendingPaymentOrder(ods[index].getID().toString());});
		
		OrderData d = clientOpHelper.getOrderTakingAreaCurrentOrder();
		Assertions.assertTrue(this.orderDatasEqual(ods[index], d));
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, index);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
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
			
			this.displayOrder(tableNumbers, orderNote, index, tnIndex);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index);
			
			this.makeCookingOrder(tableNumbers, orderNote, index, tnIndex);
			this.makePendingPaymentOrder(tableNumbers, orderNote, index, tnIndex);
			this.makePendingSendOrder(tableNumbers, orderNote, index, tnIndex, isCash, isHere);
			this.makeSentOrder(tableNumbers, orderNote, index, tnIndex, isCash, isHere);
		}
	}
	
	@Test
	void orderCycleWithEditTest() {
		Integer[] tableNumbers = clientModel.getTableNumbers().toArray(Integer[]::new);
		
		int index;
		boolean isCash;
		boolean isHere;
		int tnIndex;
		String orderNote;
		
		for (int i = 0; i < ods.length; i++) {
			index = i;
			isCash = GeneralTestUtilityClass.generateRandomBoolean();
			isHere = GeneralTestUtilityClass.generateRandomBoolean();
			tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			
			this.displayOrder(tableNumbers, orderNote, index, tnIndex);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index);
			
			this.makeCookingOrder(tableNumbers, orderNote, index, tnIndex);
			this.editCookingOrder(index);
			
			tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			
			this.displayOrder(tableNumbers, orderNote, index, tnIndex);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
			
			this.makeCookingOrder(tableNumbers, orderNote, index, tnIndex);
			this.makePendingPaymentOrder(tableNumbers, orderNote, index, tnIndex);
			this.editPendingPaymentOrder(index);
			
			tnIndex = GeneralTestUtilityClass.generateRandomNumber(0, tableNumbers.length - 1);
			orderNote = GeneralTestUtilityClass.generateRandomWord(5);
			
			this.displayOrder(tableNumbers, orderNote, index, tnIndex);
			Assertions.assertEquals(clientModel.getAllWrittenOrders().length, index+1);
			
			this.makeCookingOrder(tableNumbers, orderNote, index, tnIndex);
			this.makePendingPaymentOrder(tableNumbers, orderNote, index, tnIndex);
			this.makePendingSendOrder(tableNumbers, orderNote, index, tnIndex, isCash, isHere);
			this.makeSentOrder(tableNumbers, orderNote, index, tnIndex, isCash, isHere);
		}
	}
}
