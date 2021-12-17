
<img src="https://s2.coinmarketcap.com/static/cloud/img/coinmarketcap_1.svg?_=f029045" width="400">

# CoinMarketCap Discord Bot

This is a discord bot that uses coinmarketcaps api to get the quoted price converted to usd for a coin.

### Setup
Go to the root of the project and type:
```bash
./gradlew.bat build
```
If on windows dont use ./

Once it is built you can navigate to build/libs, and you will see CoinMarketCapBot-1.2.jar

### Config

When you first run the bot it will create a app.config file. If you want to skip it automatically creating it place a file where the jar file is called app.config with the following:

```bash
#This is the coinmarketcap api key that you get from here https://coinmarketcap.com/api/
COINMARKETCAP_API_KEY=
#This is the discord guild id. You can get it by turning on developer mode by going to settings in discord -> Advanced -> Developer Mode
GUILD_ID=
#This is the symbol that is uses. Example HEX
COIN_TOKEN=
#This is the discord token you generate from the discord bot after creating a application in https://discord.com/developers/
DISCORD_TOKEN=
#The amount of time it takes before doing an update of the bots nickname and activity
INTERVAL_MINUTES=15
#The presence message type is price percentage changed in 24 hours. Change it to any of the following below
PRESENCE_MESSAGE_TYPE=percent_change_24h
#This will determine the decimal placement on the price
PRICE_FORMAT=0.00
```

### PRESENCE_MESSAGE_TYPES

- price - <b>Price of the coin</b>
- volume_24h - <b>Volume of trades in the last 24 hours</b>
- volume_change_24h - <b>Volume percentage changed in the last 24 hours</b>
- percent_change_1h - <b>Price percentage changed in the last hour</b>
- percent_change_24h - <b>Price percentage changed in the last 24 hours</b>
- percent_change_7d - <b>Price percentage changed in the last 7 days</b>
- percent_change_30d - <b>Price percentage changed in the last 30 days</b>
- percent_change_60d - <b>Price percentage changed in the last 60 days</b>
- percent_change_90d - <b>Price percentage changed in the last 90 days</b>
- market_cap
- market_cap_dominance
- fully_diluted_market_cap

### Installing JDK 8

#### Centos

```bash
yum -y update
```

```bash
yum install java-1.8.0-openjdk
```

#### Ubuntu

```bash
sudo apt-get update
```

```bash
sudo apt-get install openjdk-8-jdk
```

#### Mac

```bash
brew install --cask java8
```

#### Windows

```bash
https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
```

### Running

```bash
java -jar CoinMarketCapBot-1.1.jar
```

### CoinMarketCap Api Setup
Go to https://coinmarketcap.com/api/ and click the button "GET YOUR API KEY NOW". It takes less than 5 min to setup.

It is also <b>FREE</b>.

CoinMarketCap offers 10,000 requests a month with 333 request per day.

So you can set the INTERVAL_MINUTES to 5 mintues and you will have enough free requests for a whole month.


### Discord Application Setup

Go to https://discord.com/developers/applications and click New Application. Once you have created a new application click into it if it does not navigate you to it. Then set the APP ICON. Next navigate to Bot and create a bot. Click the token reveal and copy it. You will be using this in the app.config.

Next navigate to the OAuth2

![img.png](examples/img.png)

Copy the link and navigate to it. It will ask you to authorize the bot into the server.
