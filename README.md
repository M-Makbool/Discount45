# Discount45

Discount45 is a Scala application that processes product transactions from a CSV file, applies discount rules, and writes the results to an Oracle database.

## Features

- Reads transactions from a CSV file (`src/main/resources/TRX1000.csv`)
- Applies multiple discount rules to each transaction
- Selects the top two applicable discounts, averages them, and calculates the final price
- Logs transaction processing steps and errors
- Writes processed transactions to an Oracle database

## Project Structure

- `src/main/scala/Project/Discount45.scala`: Main application logic
- `src/main/scala/Project/TransactionHandler.scala`: Transaction and discount rule definitions
- `src/main/resources/TRX1000.csv`: Input data file
- `discount45.log`: Log file with transaction and process details

## How It Works

1. Reads all transaction lines from the CSV file, skipping the header.
2. Converts each line to a `Transaction` object.
3. Applies all discount rules, selects the top two, averages them, and calculates the final price.
4. Writes each processed transaction to the Oracle database.
5. Closes the database connection and logs the process.

## Requirements

- Scala
- sbt
- Oracle JDBC driver (for database connectivity)

## Running the Application

1. Ensure the Oracle database is running and accessible.
2. Place the input CSV file at `src/main/resources/TRX1000.csv`.
3. Build and run the application using sbt.


4. Check `discount45.log` for processing details.

## Logging

- All processing steps and errors are logged to `discount45.log`.

## License

This project is for educational purposes.