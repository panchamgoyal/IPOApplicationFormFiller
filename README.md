This application is used to fill PDF forms programatically for the purpose of applying for IPO applications through ASBA.

This application is useful for people who wish to apply for IPO on behalf of Minor, or any other entity for which they have to fill and sumbmit the IPO application form manually.

Steps to generate the filled application form.
1. Go to https://ipoforms.nseindia.com/issueforms and download blank application form for the company they wish to apply an IPO for.
2. Clone the repo and run it locally
3. Once the application is started, make a `POST` request to the endpoint `localhost:8081/pdf/fillPdf` with below data

```
[
    {
    "address": "#100, STREET NO 1, MAIN ROAD, CITY, 000000",
    "email": "EMAIL@GMAIL.COM",
    "name": "PANCHAM GOYAL",
    "pan": "ABCDE1234Z",
    "mobile": "9876543210",
    "dpAccountDetails": "IN00000000000000",
    "bankAccountNumber": "922010088319220",
    "bankNameAndBranch": "AXIS BANK, KIKAR BAZAAR BATHINDA",
    "bidPrice": "150",
    "discount": "0",
    "numberOfEquityShare": "100"
  }
]
```

In case  you wish to generate multiple applications for an IPO, you can download multiple Blank application forms in Step 1 above and add data for multiple applications in the above json beforing making `POST` request.
