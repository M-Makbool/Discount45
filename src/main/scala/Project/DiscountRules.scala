package Project

import java.time.Month
import TransactionHandler.Transaction

object DiscountRules {

  // Expiry discount: 1% per day under 30 days remaining
  def isExpiryDiscount(product: Transaction): Boolean =
    product.expiryDate.toEpochDay - product.transactionDate.toEpochDay < 30
  def expiryDiscount(product: Transaction): Option[(Double, String)] = {
    val daysRemaining = product.expiryDate.toEpochDay - product.transactionDate.toEpochDay
    val discount = (30 - daysRemaining).toDouble / 100
    Some(discount, s"Expiry discount: $daysRemaining days remaining -> $discount%")
  }

  // Cheese (10%) and Wine (5%) discount
  def isCheeseDiscount(product: Transaction): Boolean =
    Set("Wine", "Cheese").contains(product.productName)
  def cheeseDiscount(product: Transaction): Option[(Double, String)] =
    product.productName match {
      case "Cheese" => Some(.1, "Cheese discount: 10%")
      case "Wine" => Some(.05, "Wine discount: 5%")
      case _ => None
    }

  // 23rd March special discount 50%
  def isMarch23Discount(product: Transaction): Boolean =
    product.transactionDate.getMonth == Month.MARCH &&
      product.transactionDate.getDayOfMonth == 23
  def march23Discount(product: Transaction): Option[(Double, String)] =
    Some((.5, "23rd March special discount: 50%"))

  // Quantity tier discount
  def isQuantityDiscount(product: Transaction): Boolean = product.quantity > 5
  def quantityDiscount(product: Transaction): Option[(Double, String)] =
    product.quantity match {
      case 6|7|8|9        => Some((.05, "Quantity 6-9 units: 5%"))
      case 10|11|12|13|14 => Some((.07, "Quantity 10-14 units: 7%"))
      case x if (x > 14)  => Some((.1, "Quantity 15+ units: 10%"))
      case _              => None
    }

  // App usage discount: quantity rounded up to nearest 5
  def isAppDiscount(product: Transaction): Boolean = product.channel.equalsIgnoreCase("App")
  def appDiscount(product: Transaction): Option[(Double, String)] = {
    val discount = product.quantity match {
      case x if x > 15 => .15
      case _ => (((product.quantity - 1) / 5) + 1) * 0.05
    }
    Some(discount, s"App discount: ${product.quantity} units → ${(discount*100).toInt}%")
  }

  // Visa payment discount
  def isVisaDiscount(product: Transaction): Boolean = product.paymentMethod.equalsIgnoreCase("Visa")
  def visaDiscount(product: Transaction): Option[(Double, String)] = Some(.05, "Visa payment discount: 5%")

  private type qualifier = Transaction => Boolean
  private type calculation = Transaction => Option[(Double, String)]
  val allRules: Vector[(qualifier, calculation)] = Vector(
    (isExpiryDiscount   , expiryDiscount  ),
    (isCheeseDiscount   , cheeseDiscount  ),
    (isMarch23Discount  , march23Discount ),
    (isQuantityDiscount , quantityDiscount),
    (isAppDiscount      , appDiscount     ),
    (isVisaDiscount     , visaDiscount    )
  )
}
