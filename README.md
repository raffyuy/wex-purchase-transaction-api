# wex-purchase-transaction-api


## Intro
This is the WEX TAG and Gateway coding assignment submission by Rafael Uy

###Assumptions
1. All purchase transactions stored is in USD
2. I am free to create the endpoint specifications (ie. naming, parameters, etc)
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