package Project

import java.time.LocalDate

/**
 * Handles transaction data and parsing for the discount system.
 */
object TransactionHandler {

  /**
   * Represents a single transaction for a product.
   *
   * @param transactionDate          The date of the transaction (yyyy-MM-dd).
   * @param transactionTime          The time of the transaction (HH:mm:ss).
   * @param productName              The name of the product.
   * @param productDescription       The description of the product.
   * @param expiryDate               The expiry date of the product.
   * @param quantity                 The quantity of the product purchased.
   * @param unitPrice                The price per unit of the product.
   * @param channel                  The sales channel (e.g., App, Web, Store).
   * @param paymentMethod            The payment method used (e.g., Visa, Cash).
   * @param discount                 The discount applied to the transaction (percentage).
   * @param finalPriceAfterDiscount  The final Price After Discount.
   */
  case class Transaction(
                          transactionDate: LocalDate,
                          transactionTime: String,
                          productName: String,
                          productDescription: String,
                          expiryDate: LocalDate,
                          quantity: Int,
                          unitPrice: Double,
                          channel: String,
                          paymentMethod: String,
                          discount: Double,
                          finalPriceAfterDiscount: Double
                        )

  /**
   * Parses a CSV line into a Transaction object.
   * Expects the line to be in the following format:
   *   timestamp,productName,expiryDate,quantity,unitPrice,channel,paymentMethod
   * Where:
   *   - timestamp is in ISO format (yyyy-MM-ddTHH:mm:ss)
   *   - product is in the format "Name - Description"
   *
   * @param line The CSV line representing a transaction.
   * @return     The parsed Transaction object.
   */
  def toTransaction(line: String): Transaction = {
    val l = line.split(',')
    val transactionTimestamp = l(0).split('T')
    val productName = l(1).split(" - ")
    Transaction(
      transactionDate = LocalDate.parse(transactionTimestamp.head),
      transactionTime = transactionTimestamp.last,
      productName = productName.head,
      productDescription = productName.last,
      expiryDate = LocalDate.parse(l(2)),
      quantity = l(3).toInt,
      unitPrice = l(4).toDouble,
      channel = l(5),
      paymentMethod = l(6),
      discount = 0,
      finalPriceAfterDiscount = l(3).toInt * l(4).toDouble
    )
  }

}