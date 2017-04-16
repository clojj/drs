package de.fisp.skp;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fisp.skp.model.test.PojoA;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PojoToDatomsTest {

    private static final Date DATE = asDate(LocalDate.of(2017, 1, 1));

    @Test
    public void convert_PojoA_Jackson() throws Exception {

        PojoA pojoA = createPojoA();

        ObjectMapper objectMapper = new ObjectMapper();

        @SuppressWarnings("unchecked")
        Map<String, Object> objectAsMap = objectMapper.convertValue(pojoA, Map.class);

        assertPojoA(objectAsMap);
    }

    private void assertPojoA(Map<String, Object> objectAsMap) {
        assertThat(objectAsMap).containsKeys("someDate", "someFlag", "someMoney", "someId");
        assertThat(objectAsMap).containsValues(Boolean.TRUE, DATE, "id1", BigDecimal.TEN);
    }

    @Test
    public void convert_PojoA_BeanUtils() throws Exception {

        PojoA pojoA = createPojoA();

        Map<Object, Object> objectAsMap = new org.apache.commons.beanutils.BeanMap(pojoA);

        assertThat(objectAsMap).containsKeys("someDate", "someFlag", "someMoney", "someId");
        assertThat(objectAsMap).containsValues(Boolean.TRUE, DATE, "id1", BigDecimal.TEN);
    }

    private PojoA createPojoA() {
        PojoA pojoA = new PojoA();
        pojoA.setSomeDate(DATE);
        pojoA.setSomeFlag(true);
        pojoA.setSomeId("id1");
        pojoA.setSomeMoney(BigDecimal.TEN);
        return pojoA;
    }

    private static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
