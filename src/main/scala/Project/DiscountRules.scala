package Project

import java.time.Month
import TransactionHandler.Transaction

object DiscountRules {

  // Expiry discount: 1% per day under 30 days remaining
  def isExpiryDiscount(product: Transaction): Boolean =
    product.expiryDate.toEpochDay - product.transactionDate.toEpochDay < 30
  def expiryDiscount(product: Transaction): Double = {
    (30 - product.expiryDate.toEpochDay - product.transactionDate.toEpochDay)
      .toDouble / 100
  }

  // Cheese (10%) and Wine (5%) discount
  def isCheeseDiscount(product: Transaction): Boolean =
    Set("Wine", "Cheese").contains(product.productName)
  def cheeseDiscount(product: Transaction): Double =
    product.productName match {
      case "Cheese" => .10
      case "Wine"   => .05
      case _        => .00
    }

  // 23rd March special discount 50%
  def isMarch23Discount(product: Transaction): Boolean =
    product.transactionDate.getMonth == Month.MARCH &&
      product.transactionDate.getDayOfMonth == 23
  def march23Discount(product: Transaction): Double = .5

  // Quantity tier discount
  def isQuantityDiscount(product: Transaction): Boolean = product.quantity > 5
  def quantityDiscount(product: Transaction): Double =
    product.quantity match {
      case 6|7|8|9        => .05
      case 10|11|12|13|14 => .07
      case x if (x > 14)  => .10
      case _              => .00
    }

  // App usage discount: quantity rounded up to nearest 5
  def isAppDiscount(product: Transaction): Boolean = product.channel.equalsIgnoreCase("App")
  def appDiscount(product: Transaction): Double =
    product.quantity match {
      case x if x > 15 => .15
      case _ => (((product.quantity - 1) / 5) + 1) * 0.05
    }

  // Visa payment discount
  def isVisaDiscount(product: Transaction): Boolean =
    product.paymentMethod.equalsIgnoreCase("Visa")
  def visaDiscount(product: Transaction): Double = .05

  val allRules: Vector[(Transaction => Boolean, Transaction => Double)] =
    Vector(
      (isExpiryDiscount   , expiryDiscount  ),
      (isCheeseDiscount   , cheeseDiscount  ),
      (isMarch23Discount  , march23Discount ),
      (isQuantityDiscount , quantityDiscount),
      (isAppDiscount      , appDiscount     ),
      (isVisaDiscount     , visaDiscount    )
    )
}
