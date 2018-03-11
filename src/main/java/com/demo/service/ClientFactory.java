package com.demo.service;

public interface ClientFactory<T> {

    public T createClient();
}
