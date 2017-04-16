package de.fisp.skp;

import de.fisp.skp.jpa.DatomRepository;
import de.fisp.skp.model.Datom;
import de.fisp.skp.model.DatomId;
import de.fisp.skp.model.test.PojoA;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@EnableScheduling
public class SocketHandlerUpdate extends TextWebSocketHandler {

    @Resource
    private DatomRepository datomRepository;

    // todo ConcurrentHashMap ?
    private List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    // TODO remove
    @PostConstruct
    private void init_Test() {

        PojoA pojoA = new PojoA();
        pojoA.setSomeDate(asDate(LocalDate.of(2017, 1, 1)));
        pojoA.setSomeFlag(true);
        pojoA.setSomeId("id1");
        pojoA.setSomeMoney(BigDecimal.TEN);

        Map<Object, Object> objectAsMap = new org.apache.commons.beanutils.BeanMap(pojoA);

        for (Map.Entry<Object, Object> entry : objectAsMap.entrySet()) {
            Datom datom = new Datom();
            DatomId datomId = new DatomId();
            datom.setDatomId(datomId);

            datomId.setDarlehen("42424242");
            datomId.setFieldname((String) entry.getKey());
            datomId.setValidFrom(Timestamp.from(Instant.now()));
            datom.setValue(entry.getValue().toString());

            datomRepository.save(datom);
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

    @Scheduled(fixedDelay = 2000)
    public void scheduleUpdate() throws IOException {
        System.out.println("update...");
        List<Datom> list = datomRepository.findByDarlehen("123");
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(list.toString()));
        }
    }

    private static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}