package Project

import scala.io.Source
import TransactionHandler._

/**
 * The Discount45 object processes transactions by reading them from a CSV file,
 * applying discount rules, and calculating the final price after discount.
 */
object Discount45 extends App {

  // Reads all transaction lines from the CSV file, skipping the header.
  val lines: List[String] = Source.fromFile("src/main/resources/TRX1000.csv").getLines().toList.tail

    lines.map(toTransaction).map(addDiscount).foreach(OracleDB.write)

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