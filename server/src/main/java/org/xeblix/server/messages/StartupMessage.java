package org.xeblix.server.messages;

import org.xeblix.server.util.MessagesEnum;

public class StartupMessage implements Message {

	private static final long serialVersionUID = -5581247578151916756L;

	public MessagesEnum getType() {
		return MessagesEnum.STARTUP;
	}

}
