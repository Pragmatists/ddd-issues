package ddd.infrastructure;

import java.util.Date;

import ddd.domain.Clock;

public class DateUtilClock implements Clock {
    @Override
    public Date time() {
        return new Date();
    }
}
