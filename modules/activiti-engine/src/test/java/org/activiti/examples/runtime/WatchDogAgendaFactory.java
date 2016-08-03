package org.activiti.examples.runtime;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.agenda.ListAgenda;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.runtime.Agenda;
import org.springframework.beans.factory.FactoryBean;

/**
 * This class...
 */
public class WatchDogAgendaFactory implements FactoryBean<Agenda> {

    @Override
    public Agenda getObject() throws Exception {
        return new WatchDogAgenda(new ListAgenda());
    }

    @Override
    public Class<?> getObjectType() {
        return WatchDogAgenda.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private static class WatchDogAgenda implements Agenda {

        private static final int WATCH_DOG_LIMIT = 10;

        private final Agenda agenda;
        private int counter;

        private WatchDogAgenda(Agenda agenda) {
            this.agenda = agenda;
        }

        @Override
        public boolean isEmpty() {
            return agenda.isEmpty();
        }

        @Override
        public Runnable getNextOperation() {
            if (counter<WATCH_DOG_LIMIT) {
                counter++;
                return agenda.getNextOperation();
            }
            throw new ActivitiException("WatchDog limit exceeded.");
        }

        @Override
        public void planOperation(Runnable operation) {
            agenda.planOperation(operation);
        }

        @Override
        public void planOperation(Runnable operation, ExecutionEntity executionEntity) {
            agenda.planOperation(operation, executionEntity);
        }
    }
}
