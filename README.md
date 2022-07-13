# currency-conversion

    Find the best currency conversion.

## Requirements

1. Java - 11

2. gradle - 7.4

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/pawelLarionov/currency-conversion.git
```

**2. Build and run the app using gradle**

```bash
cd currency-conversion
./gradlew bootRun

Also after './gradlew build' you can run:  'java -jar ./build/libs/currency-conversion-0.0.1-SNAPSHOT.jar' ) 

As a result of execution will be created CSV file ./output.csv that contains optimal conversions.
```

### Configuration and log files
    Result CSV file   : ./output.csv
    Log file          : ./logs/application.log
    Configuration file: ./src/main/resources/application.yaml

## Information about task

    Find the best currency conversion possible for our customers. 
    However, we don’t have direct Canadian Dollar conversions to all currencies so we have to trade currencies for other currencies.
    It is possible that we can go from one currency to another, and that a currency could show up multiple times.

### Example

    Convert CAD to EUR
    CAD -> GBP -> EUR

    There are no cycles
    CAD -> EUR -> GBP -> EUR
    Utilizing the API data, return the best possible conversion rate for every currency we can get, assuming we start with $100 CAD. 
    The best possible conversion rate should yield the    highest possible amount of currency that you are converting to. 
    Given the following example, the longer route yields the higher amount (95.00) and is therefore the best conversation rate.

    CAD -> HKD = 90.00
    CAD -> GBP -> DOGE -> HKD = 95.000

### API Endpoint URL  

`https://api-coding-challenge.neofinancial.com/currency-conversion?seed=65015`

### Requirements

    Use one of the following languages: Javascript, Typescript, Java, C#, Python or Ruby
    Ensure you comment your code
    Use a REST call to get the data, do not hardcode it into your source code.
    Generate a CSV file as an output that contains optimal conversions from CAD to all currencies. The file should have the following format:
    Currency Code (ie. AUD, USD, BTC)
    Country (ie. Australia, USA, Bitcoin)
    Amount of currency, given we started with $100 CAD (ie. 1500.43)
    Path for the best conversion rate, pipe delimited (ie. CAD | GBP | EUR)
