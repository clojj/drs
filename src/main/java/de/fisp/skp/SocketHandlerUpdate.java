package de.fisp.skp;

import de.fisp.skp.jpa.StoreRepository;
import de.fisp.skp.model.Store;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@EnableScheduling
public class SocketHandlerUpdate extends TextWebSocketHandler {

    @Resource
    private StoreRepository storeRepository;

    // todo ConcurrentHashMap ?
    private List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Scheduled(fixedDelay = 2000)
    public void scheduleUpdate() throws IOException {
        System.out.println("update...");
        List<Store> list = storeRepository.findByDarlehen("123");
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(list.toString()));
        }
    }

}