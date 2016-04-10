package com.example.androidstudio.kalkulaator_ii;

import java.math.BigDecimal;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class DoubleStatistic {
    String op;
    double val1;
    double val2;
    double result;

    public DoubleStatistic(String op, double val1, double val2) {
        this.op = op;
        this.val1 = val1;
        this.val2 = val2;
        result = operation();
    }

    public double getResult() {
        return result;
    }

    public double operation() {
        if (Double.isInfinite(val1) || Double.isInfinite(val2)) {
            if (op.equals("+")) {
                return val1 + val2;
            }
            if (op.equals(":")) {
                return val1 / val2;
            }
            if (op.equals("*")) {
                return val1 * val2;
            }
        }
        BigDecimal bVal1 = BigDecimal.valueOf(val1);
        BigDecimal bVal2 = BigDecimal.valueOf(val2);
        if (op.equals("+")) {
            return bVal1.add(bVal2).doubleValue();
        }
        if (op.equals(":")) {
            if (val2 == 0) {
                if (val1 > 0) {
                    return Double.POSITIVE_INFINITY;
                } else {
                    return Double.NEGATIVE_INFINITY;
                }
            } else {
                try {
                    return bVal1.divide(bVal2).doubleValue();
                } catch (Exception e) {
                    return val1 / val2;
                }
            }
        }
        if (op.equals("*")) {
            return bVal1.multiply(bVal2).doubleValue();
        }
        return 0;
    }
}
