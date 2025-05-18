package Project

import java.time.LocalDate

object TransactionHandler {

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
                          discount: Double
                        )

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
      discount = 0
    )
  }

}
