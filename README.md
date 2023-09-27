# wex-purchase-transaction-api


## Introduction
This is the WEX TAG and Gateway coding assignment submission by Rafael Uy.

[Go to Product Brief section](#Product Brief) 

###Assumptions
1. All purchase transactions stored is in USD
2. I am free to create the endpoint specifications (ie. naming, parameters, etc)

###Future improvements
Unfortunately I have to limit the time I spend on this as I have other applications to tend to. If I had more time,
I would work on the following:

1. Security
2. API Documentation, maybe through swagger
3. Additional tests, especially negative scenarios
4. Improve logging

###Thanks
To the people reviewing my code,


Thank you for taking time to review my work. It's been an interesting challenge
and I enjoyed working on it, despite my busy schedule. I am looking forward to 
hearing back and I am keen to receive constructive feedback on my work.  



----
##Quickstart 
###Build
```
./gradlew clean build

docker build -t purchase-transaction-api:1.0.0 .
```

###Run
```
docker run -p 8080:8080 purchase-transaction-api:1.0.0
```

----
##Usage

### Create Purchase Transaction
Method: POST 

Endpoint: /api/v1/purchasetransaction

Request Body: PurchaseTransaction json object. eg.
```
{
    "description": "dummy",
    "transactionDate": "2020-03-20",
    "amount": "12.50"
}
```

description - max 50 character description of the transaction

transactionDate - date when the transaction occurred in ISO-8601 format

amount - transaction amount in USD 


### Get Purchase Transaction by ID 
Method: GET 

Endpoint: /api/v1/purchasetransaction/{id}

Example: `/api/v1/purchasetransaction/1`

Response: 

```
{
    "id": 1
    "description": "dummy",
    "transactionDate": "2020-03-20",
    "amount": "12.50"
}
```

Path parameter:

id - ID of the purchase transaction  

### Get Transaction in Converted Currency

Method: GET

Endpoint: /api/v1/purchasetransaction/{id}/convertCurrency?country={country full name}

Example: `/api/v1/purchasetransaction/1111/convertCurrency?country=Australia`

Response:
```
{
    "id": 1111,
    "description": "test",
    "transactionDate": "2020-03-22",
    "amount": 20.20,
    "convertedAmount": 28.79,
    "exchangeRate": 1.425
}
```


---
#Product Brief

##Summary
Your task is to build an application that supports the requirements outlined below. Outside of the requirements outlined,
as well as any language limitations specified by the technical implementation notes below and/or by the hiring
manager(s), the application is your own design from a technical perspective.

This is your opportunity to show us what you know! Have fun, explore new ideas, and as noted in the Questions section
below, please let us know if you have any questions regarding the requirements!

##Requirements
###Requirement #1: Store a Purchase Transaction
Your application must be able to accept and store (i.e., persist) a purchase transaction with a description, transaction
date, and a purchase amount in United States dollars. When the transaction is stored, it will be assigned a unique
identifier.

Field requirements

- Description: must not exceed 50 characters
- Transaction date: must be a valid date
- Purchase amount: must be a valid amount rounded to the nearest cent
- Unique identifier: must uniquely identify the purchase

## Requirement #2: Retrieve a Purchase Transaction in a Specified Country’s Currency
Based upon purchase transactions previously submitted and stored, your application must provide a way to retrieve the
stored purchase transactions converted to currencies supported by the Treasury Reporting Rates of Exchange API based
upon the exchange rate active for the date of the purchase.

https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange

The retrieved purchase should include the identifier, the description, the transaction date, the original US dollar purchase
amount, the exchange rate used, and the converted amount based upon the specified currency’s exchange rate for the
date of the purchase.

Currency conversion requirements
- When converting between currencies, you do not need an exact date match, but must use a currency conversion
rate less than or equal to the purchase date from within the last 6 months.
- If no currency conversion rate is available within 6 months equal to or before the purchase date, an error should
be returned stating the purchase cannot be converted to the target currency.
- The converted purchase amount to the target currency should be rounded to two decimal places (i.e., cent).
Technical Implementation
The technical implementation, including frameworks, libraries, etc. is your own design except for the language. That is, if
you are applying for Gateways (written in Java), you should implement the solution in Java. If you are applying for TAG
(written in GoLang), you may choose to implement the solution in GoLang or Java.
You should build this application as if you are building an application to be deployed in a Production environment. This
should be interpreted to mean that all functional automated testing you would include for a Production application should
be expected. Please note that non-functional test (e.g., performance testing) automation is not needed.
Your application repository should be runnable without installing additional software stack components, such as
databases, web servers, or servlet containers (e.g., Jetty, Tomcat, etc).