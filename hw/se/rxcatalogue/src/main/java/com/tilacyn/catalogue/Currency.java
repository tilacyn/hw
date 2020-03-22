package com.tilacyn.catalogue;

public enum Currency {
    USD, EUR, RUB;

    private static final double rubToUsdCoefficient = 75;
    private static final double rubToEurCoefficient = 88;

    public static double convertTo(Currency currency, double rub) {
        switch (currency) {
            case EUR:
                return rub / rubToEurCoefficient;
            case USD:
                return rub/ rubToUsdCoefficient;
        }
        return rub;
    }

    public static Currency fromString(String s) {
        if (s.toUpperCase().equals(USD.toString())) {
            return USD;
        } else if (s.toUpperCase().equals(EUR.toString())) {
            return EUR;
        }
        return RUB;
    }
}
