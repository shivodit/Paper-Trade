
---

# Paper Trade - Stock Simulation Java App

Paper Trade is a Java application that simulates stock trading, allowing users to practice trading strategies without risking real money. The application uses SQL and MySQL to store and manage stock data, user portfolios, and transaction history.

## Features
- User-friendly interface for buying and selling stocks
- Portfolio management to track stock holdings and performance
- Transaction history for reviewing past trades
- Simulation for testing strategies without financial risk

## Technologies Used
- Java
- SQL
- MySQL
- API for stock data retrieval (e.g., Alpha Vantage, Yahoo Finance)

## Setup
1. Clone the repository: `git clone https://github.com/shivodit/Paper-Trade.git`
2. Set up MySQL and create a database for the application called paper_trade 
3. Configure the database connection in the application (Edit the DatabaseConnection class in src/PaperTrade/db to have your correct credentials)
4. Run the application and start trading!
5. {
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch App",
            "request": "launch",
            "mainClass": "main.PaperTrade.Main",
            "vmArgs": "--module-path C:\\Users\\vishn\\Documents\\cc\\dbs_project\\PaperTrade\\lib --add-modules javafx.controls,javafx.fxml -Dprism.order=sw",
        }
    ]
}

use the above given vscode launch configuration to run the app with these dependencies 
replace module path with your own --module-path C:\\Users\\vishn\\Documents\\cc\\dbs_project\\PaperTrade\\lib
for ex: --module-path C:\\PaperTrade\\lib

or provide the appropriate vm args with your preferred choice of launch method

## Usage
1. Register an account or log in if you already have one
2. Browse available stocks and their current prices
3. Buy stocks using available funds in your account
4. Sell stocks to realize profits or cut losses
5. Monitor your portfolio and track performance over time
6. Analyze transaction history to improve your trading strategy
---
