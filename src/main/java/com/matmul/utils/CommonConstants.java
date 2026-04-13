package com.matmul.utils;

import com.matmul.multiplier.Multiplier;
import com.matmul.multiplier.NaiveConcurrentMultiplier;
import com.matmul.multiplier.NaiveMultiplier;
import com.matmul.multiplier.StrassensConcurrentMultiplier;
import com.matmul.multiplier.StrassensMultiplier;

public enum CommonConstants {

    NAIVE("Naive",                     new NaiveMultiplier(),                false),
    NAIVE_CONCURRENT("NaiveConcurrent", new NaiveConcurrentMultiplier(),     true),
    STRASSEN("Strassen",               new StrassensMultiplier(),             false),
    STRASSEN_CONCURRENT("StrassenConcurrent", new StrassensConcurrentMultiplier(), true);

    public final String label;
    public final Multiplier multiplier;
    public final boolean isConcurrent;

    CommonConstants(String label, Multiplier multiplier, boolean isConcurrent) {
        this.label = label;
        this.multiplier = multiplier;
        this.isConcurrent = isConcurrent;
    }
}
