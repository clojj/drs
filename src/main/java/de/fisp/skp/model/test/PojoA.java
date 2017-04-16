package de.fisp.skp.model.test;

import java.math.BigDecimal;
import java.util.Date;

public class PojoA {
    private String someId;
    private BigDecimal someMoney;
    private Date someDate;
    private boolean someFlag;

    public String getSomeId() {
        return someId;
    }

    public void setSomeId(String someId) {
        this.someId = someId;
    }

    public BigDecimal getSomeMoney() {
        return someMoney;
    }

    public void setSomeMoney(BigDecimal someMoney) {
        this.someMoney = someMoney;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public boolean isSomeFlag() {
        return someFlag;
    }

    public void setSomeFlag(boolean someFlag) {
        this.someFlag = someFlag;
    }
}
