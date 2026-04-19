package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState state;
    private Stack<OrderState> history;

    public Order(OrderState initialState) {
        this.state = initialState;
        this.history = new Stack<>();
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (this.state == OrderState.DELIVERED || this.state == OrderState.CANCELED) {
            throw new OrderIsAlreadyFinalException();
        }

        this.history.push(this.state);

        switch (this.state) {
            case PLACED -> this.state = OrderState.PROCESSED;
            case PROCESSED -> this.state = OrderState.SHIPPED;
            case SHIPPED -> this.state = OrderState.DELIVERED;
        }

        System.out.println("Order state updated to: " + this.state);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (this.state == OrderState.DELIVERED || this.state == OrderState.CANCELED) {
            throw new CannotCancelFinalOrderException();
        }

        this.history.push(this.state);
        this.state = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (this.history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("No");
        }

        this.state = this.history.pop();
        System.out.println("Order state reverted to: " + this.state);
    }
}