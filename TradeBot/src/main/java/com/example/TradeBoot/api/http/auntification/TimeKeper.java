package com.example.TradeBoot.api.http.auntification;

import java.util.Optional;


public interface TimeKeper
{
    long currentTimeMillis();
    long getLastTimeMillis();

    class Base implements TimeKeper {
        private Optional<Long> lastTime;
        public long currentTimeMillis() {
            long time = System.currentTimeMillis();
            this.lastTime = Optional.of(time);
            return time;
        }

        public long getLastTimeMillis() {
            if (lastTime.isEmpty())
                return currentTimeMillis();
            else
                return lastTime.get();
        }
    }

    class Mock implements TimeKeper
    {
        private final long time;

        public Mock(long time) {
            this.time = time;
        }

        @Override
        public long currentTimeMillis() {
            return time;
        }

        @Override
        public long getLastTimeMillis() {
            return time;
        }
    }
}

