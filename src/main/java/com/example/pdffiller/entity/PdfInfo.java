package com.example.pdffiller.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfInfo {
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String pan;
    private String dpAccountDetails;
    private String bankAccountNumber;
    private String bankNameAndBranch;
    private BigDecimal bidPrice;
    private BigDecimal discount;
    private BigDecimal numberOfEquityShare;
}
