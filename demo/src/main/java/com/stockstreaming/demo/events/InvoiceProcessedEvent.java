package com.stockstreaming.demo.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Setter
public class InvoiceProcessedEvent {
    private final String invoiceNumber;
    private final boolean success;
    private final String message;


}
