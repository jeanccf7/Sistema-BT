package com.batterytrade.app.payment;

/**
 * Factory sencillo para obtener la estrategia de pago según el método.
 */
public class PaymentStrategyFactory {

    public static PaymentStrategy getStrategy(String metodoPago) {
        if (metodoPago == null) return new EfectivoStrategy();
        switch (metodoPago.toUpperCase()) {
            case "YAPE":
                return new YapeStrategy();
            case "PLIN":
            case "TRANSFERENCIA":
                return new EfectivoStrategy();
            default:
                return new EfectivoStrategy();
        }
    }
}
