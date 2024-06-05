package ru.prbank.test_task.seacombat.domain.enums;

public enum ShotResult {
    MISS{
        @Override
        public String toString() {
            return "Мимо";
        }
    },
    HIT{
        @Override
        public String toString() {
            return "Ранен";
        }
    },
    DESTROYED{
        @Override
        public String toString() {
            return "Убит";
        }
    },
    WIN{
        @Override
        public String toString() {
            return "Победа";
        }
    }
}
