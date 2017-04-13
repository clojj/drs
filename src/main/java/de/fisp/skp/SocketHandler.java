package de.fisp.skp;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fisp.skp.model.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	private ObjectMapper mapper;

	public SocketHandler() {
		mapper = new ObjectMapper();
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
		for (WebSocketSession webSocketSession : sessions) {
			String payload = message.getPayload();
			Reservation reservation = mapper.readValue(payload, Reservation.class);

			String result = mapper.writeValueAsString(reservation);
			webSocketSession.sendMessage(new TextMessage(result));
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
		sessions.remove(session);
	}
}