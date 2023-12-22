package com.example.pdffiller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "pan_of_sole")
    private String panOfSole;
    @Column(name = "tel_number")
    private String telNumber;
    @Column(name = "bidder_depository_account_details")
    private String bidderDepositoryAccountDetails;
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    @Column(name = "bank_name_and_branch")
    private String bankNameAndBranch;
    @Column(name = "ammount_blocked")
    private String ammountBlocked;
    @Column(name = "ammount_in_words")
    private String ammountInWords;
    @Column(name = "upi_id")
    private String upiId;
    @Column(name = "nsdl")
    private Boolean nsdl;
    @Column(name = "filled")
    private Boolean filled;

}
