package Project

import scala.io.Source
import TransactionHandler._
import java.util.logging.Level
import scala.util.{Try, Failure, Success}

/**
 * The Discount45 object processes transactions by reading them from a CSV file,
 * applying discount rules, and calculating the final price after discount.
 */
object Discount45 extends App {
  val logger = log.setup()
  logger.info("Starting transaction processing.")

  // Reads all transaction lines from the CSV file, skipping the header.
  val lines: List[String] = Try(Source.fromFile("src/main/resources/TRX1000.csv").getLines().toList.tail) match {
    case Success(l) =>
      logger.info(s"Read ${l.size} transaction lines from CSV.")
      l
    case Failure(e) =>
      logger.log(Level.SEVERE, "Failed to read CSV file.", e)
      sys.exit(1)
  }

  // Converts each CSV line to a Transaction, applies discount rules, and collects the processed transactions.
  val processed_transactions = lines.map(toTransaction).map(addDiscount)

  logger.info(s"Processed ${processed_transactions.size} transactions.")

  // Writes each processed transaction to the Oracle database.
  processed_transactions.foreach { tx =>
    Try(OracleDB.write(tx)) match {
      case Success(_) => logger.fine(s"Wrote transaction: ${tx.productName +" - "+tx.productDescription} with discount ${tx.discount*100}% and final price ${tx.finalPriceAfterDiscount} ")
      case Failure(e) => logger.log(Level.WARNING, s"Failed to write transaction: ${tx.productName+" - "+tx.productDescription} with discount ${tx.discount*100}% and final price ${tx.finalPriceAfterDiscount}", e)
    }
  }

  // Closes the Oracle database connection after all transactions are written.
  Try(OracleDB.conn.close()) match {
    case Success(_) => logger.info("Database connection closed.")
    case Failure(e) => logger.log(Level.WARNING, "Failed to close database connection.", e)
  }

  /**
   * Applies all discount rules to a transaction, selects the top two discounts,
   * averages them, and returns a new Transaction with the calculated discount and final price.
   *
   * @param product The transaction to process.
   * @return A new Transaction with updated discount and final price.
   */
  def addDiscount(product: Transaction): Transaction = {
    // Get all discounts that apply to the transaction, sort them descending, and take the top two
    val top2Discount = DiscountRules.allRules.filter(_._1(product)).map(_._2(product)).sortBy(-_).take(2)
    // If there are any discounts, average the top two; otherwise, use the existing discount
    val discount = if (top2Discount.nonEmpty) top2Discount.sum / top2Discount.length else product.discount
    Transaction(
      product.transactionDate,
      product.transactionTime,
      product.productName,
      product.productDescription,
      product.expiryDate,
      product.quantity,
      product.unitPrice,
      product.channel,
      product.paymentMethod,
      discount,
      finalPriceAfterDiscount = product.unitPrice * product.quantity * (1 - discount)
    )
  }
}