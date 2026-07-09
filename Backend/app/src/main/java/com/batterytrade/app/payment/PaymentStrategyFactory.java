package com.batterytrade.app.payment;

/**
 * Factory sencilla para obtener la estrategia de pago según el método.
 */
public class PaymentStrategyFactory {

    public static PaymentStrategy getStrategy(String metodoPago) {
        if (metodoPago == null) {
            return new EfectivoStrategy();
        }

        switch (metodoPago.toUpperCase()) {
            case "YAPE":
                return new YapeStrategy();
            case "PLIN":
                return new PlinStrategy();
            case "TRANSFERENCIA":
                return new TransferenciaStrategy();
            case "EFECTIVO":
            default:
                return new EfectivoStrategy();
        }
    }
}
