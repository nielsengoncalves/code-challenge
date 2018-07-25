# Code Challenge

[![Build Status](https://travis-ci.org/nielsenmg/code-challenge.svg?branch=master)](https://travis-ci.org/nielsenmg/code-challenge)
[![codecov](https://codecov.io/gh/nielsenmg/n26-code-challenge/branch/master/graph/badge.svg)](https://codecov.io/gh/nielsenmg/n26-code-challenge)

The problem consists in expose two restful APIs, one for saving the transactions and the other to collect realtime statistic from the last 60 seconds.

There are some extra key requirements: 
- Endpoints is recommended to execute in constant time and memory (O(1))
- Solution must be thread safe 
- Built with in memory solution

## Prerequisites
- Java 8

## Build & Running
To build the project just run the following command in the root folder of the project:

 - Using the embedded maven wrapper: `./mvnw clean install`
 - Using your own maven: `mvn clean install`
 
To run the generated jar:

`java -jar web/target/n26-code-challenge.jar`

## Restful APIs
### 1 - Transactions API
The transactions API is the entry for collecting data about the transactions that happened in the last minute.

Only transactions that happened in the last minute are accept, otherwise a HTTP Status 204 will be returned.


#### 1.1 - Endpoint

`POST /transactions`

#### 1.2 - Request Fields

| Name      | Type   | Description                                          | Example       |
|-----------|--------|------------------------------------------------------|---------------| 
| amount    | Double | Transaction amount.                                  | 99.90         |
| timestamp | Long   | Transaction time in epoch in millis in UTC time zone | 1478192204000 |

#### 1.3 - Example Request

```bash
$ curl 'http://localhost:8080/transactions' -i -X POST \
    -H 'Content-Type: application/json' \
    -H 'Accept: application/json' \
    -d '{"amount": 99.90, "timestamp":1532283773093}'
```

#### 1.4 - Example Response

```bash
HTTP/1.1 201 
Content-Length: 0
Date: Sun, 22 Jul 2018 18:22:58 GMT
```

### 2 - Statistics API
The statistics API is used to get realtime statistics about the transactions of the last minute.

#### 2.1 - Endpoint

`GET /statistics`

#### 2.2 - Example Request

```bash
$ curl 'http://localhost:8080/statistics' -i -X GET \
    -H 'Accept: application/json'
```

#### 2.3 - Example Response

```bash
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 22 Jul 2018 18:26:25 GMT

{"sum":99.90,"max":99.90,"min":99.90,"count":1,"avg":99.90}
```

## Technical Details

### How thread safe was achieved?
This solution is thread safe because it relies on native ConcurrentHashMap implementation that locks at bucket level the record being modified. 
So two threads can't modify the same record concurrently.

### Why this solution is O(1)?
This solution is O(1) because it was implemented using a concurrentHashMap that holds only 60 records, 
one for each second termination. 

The operations don't rely on the number of transactions inputted, the execution time will always be the same. 

The ConcurrentHashMap is also a Singleton, so it's shared between the requests. 

### Why unit tests were made using groovy?
The spock framework simplify to write unit tests, it makes possible to do mocking, stubs and asserts in one expression and the tests are also cleaner and more human readable.

The groovy language is also less verbose than java, it has a lot of syntax sugar that helps to write tests faster. 

The last but not least is the fact that the spock follows the [AAA pattern](http://wiki.c2.com/?ArrangeActAssert) (Arrange, Act, Assert)

## Known Issues:
- Code coverage is not considering the integration tests. (The real code coverage is above 95%)
