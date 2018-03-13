package com.demo.service.rabbitmq;

public interface ClientFactory<T> {

    public T createClient();
}
