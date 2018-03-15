package com.demo.service.kafka.base.impl;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class DefaultPartitioner implements Partitioner {

    public DefaultPartitioner(VerifiableProperties props) {
    }

    public int partition(Object key, int numPartitions) {

        return Integer.parseInt((String) key);

    }
}