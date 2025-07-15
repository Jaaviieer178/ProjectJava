package com.api.java.models;

/** Enumeración que define los posibles estados de una orden de compra. */
public enum OrderStatus {
    PENDING,    // La orden ha sido registrada pero aún no pagada
    PAID,       // La orden ya ha sido pagada
    CANCELLED   // La orden fue anulada antes de completarse
}
