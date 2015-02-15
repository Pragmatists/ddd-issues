package ddd.infrastructure;

import ddd.domain.Clock;

import java.util.Date;

public class ClockImpl implements Clock {
    @Override
    public Date time() {
        return new Date();
    }
}
