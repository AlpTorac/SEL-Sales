package controller.handler;

import controller.IController;

public class BlockKnownClientHandler extends StatusEventHandler {

	public BlockKnownClientHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().blockKnownClient((String) args[0]);
	}

}
