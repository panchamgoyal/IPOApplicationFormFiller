package com.example.pdffiller.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfInfo {
    private String address;
    private String email;
    private String name;
    private String panOfSole;
    private String telNumber;
    private String bidderDepositoryAccountDetails;
    private String bankAccountNumber;
    private String bankNameAndBranch;
    private String ammountInWords;
//    @Column(name = "upi_id")
//    private String upiId;
    private Boolean nsdl;
    private String bidPrice;
    private String discount;
    private String numberOfEquityShare;

    public String getAmmountBlocked() {
        return Long.toString(Long.parseLong(this.getNumberOfEquityShare()) * Long.parseLong(this.getNetPrice()));
    }

    public String getNetPrice() {
        if(this.getDiscount() == null || this.getDiscount().equals("")) {
            return this.getBidPrice();
        }
        return Long.toString(Long.parseLong(this.getBidPrice()) - Long.parseLong(this.getDiscount()));
    }
}
