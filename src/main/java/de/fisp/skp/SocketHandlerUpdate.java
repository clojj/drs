package de.fisp.skp;

import de.fisp.skp.jpa.DatomRepository;
import de.fisp.skp.model.Datom;
import de.fisp.skp.model.DatomId;
import de.fisp.skp.model.test.PojoA;
import javaslang.collection.Stream;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static javaslang.API.*;
import static javaslang.Predicates.instanceOf;
import static javaslang.control.Try.run;

@Component
@EnableScheduling
public class SocketHandlerUpdate extends TextWebSocketHandler {

    @Resource
    private DatomRepository datomRepository;

    // todo ConcurrentHashMap ?
    private List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    // TODO remove
    @PostConstruct
    private void initTest() {

        PojoA pojoA = new PojoA();
        pojoA.setSomeDate(asDate(LocalDate.of(2017, 1, 1)));
        pojoA.setSomeFlag(true);
        pojoA.setSomeId("id1");
        pojoA.setSomeMoney(BigDecimal.TEN);
        pojoA.setSomeInt(42);

        try {
            Stream.of(Introspector.getBeanInfo(pojoA.getClass(), Object.class).getPropertyDescriptors())
                    .filter(pd -> pd.getReadMethod() != null)
                    .forEach(pd -> {
                        try {
                            String name = pojoA.getClass().getName() + "." + pd.getName();
                            Object value = pd.getReadMethod().invoke(pojoA);

                            Datom datom = new Datom();
                            DatomId datomId = new DatomId();
                            datom.setDatomId(datomId);
                            datomId.setDarlehen("42424242");
                            datomId.setValidFrom(Timestamp.from(Instant.now()));
                            datomId.setFieldname(name);
                            Match(value).of(
                                    Case(instanceOf(String.class), o -> run(() -> datom.setValueString((String) value))),
                                    Case(instanceOf(Boolean.class), o -> run(() -> datom.setValueBoolean((Boolean) value))),
                                    Case(instanceOf(Date.class), o -> run(() -> datom.setValueDate((Date) value))),
                                    Case(instanceOf(Integer.class), o -> run(() -> datom.setValueInteger((Integer) value))),
                                    Case(instanceOf(BigDecimal.class), o -> run(() -> datom.setValueBigDecimal((BigDecimal) value))),
                                    Case($(), o -> run(() -> { throw new IllegalArgumentException(value.getClass().toString()); }))
                            );
                            datomRepository.save(datom);
                        } catch (Exception e) {
                            throw new IllegalStateException();
                        }
                    });
        } catch (IntrospectionException e) {
            throw new IllegalStateException();
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