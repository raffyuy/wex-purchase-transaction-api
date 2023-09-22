# wex-purchase-transaction-api


## Intro
This is the WEX TAG and Gateway coding assignment submission by Rafael Uy

###Assumptions
1. All purchase transactions stored is in USD
2. I am free to create the endpoint specifications (ie. naming, parameters, etc)

###Future improvements
Unfortunately I have to limit the time I spend on this as I have other applications to tend to. If I had more time,
I would work on the following:

1. Security
2. API Documentation, maybe through swagger
3. Additional tests, especially negative scenarios

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



