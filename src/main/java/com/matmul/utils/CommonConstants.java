package com.matmul.utils;

import com.matmul.multiplier.Multiplier;
import com.matmul.multiplier.NaiveConcurrentMultiplier;
import com.matmul.multiplier.NaiveMultiplier;
import com.matmul.multiplier.StrassensConcurrentMultiplier;
import com.matmul.multiplier.StrassensMultiplier;

public enum CommonConstants {

    NAIVE("Naive", new NaiveMultiplier()),
    NAIVE_CONCURRENT("NaiveConcurrent", new NaiveConcurrentMultiplier()),
    STRASSEN("Strassen", new StrassensMultiplier()),
    STRASSEN_CONCURRENT("StrassenConcurrent", new StrassensConcurrentMultiplier());

    public final String label;
    public final Multiplier multiplier;

    CommonConstants(String label, Multiplier multiplier) {
        this.label = label;
        this.multiplier = multiplier;
    }
}
