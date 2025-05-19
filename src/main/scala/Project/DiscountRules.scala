package Project

import java.time.Month
import TransactionHandler.Transaction

/**
 * Contains discount rules for transactions.
 * Each rule consists of a qualifier and a discount calculation.
 */
object DiscountRules {

  /**
   * Checks if the expiry discount applies (less than 30 days to expiry).
   * @param product The transaction to check.
   * @return True if discount applies, false otherwise.
   */
  def isExpiryDiscount(product: Transaction): Boolean =
    product.expiryDate.toEpochDay - product.transactionDate.toEpochDay < 30

  /**
   * Calculates the expiry discount (1% per day under 30 days).
   * @param product The transaction.
   * @return Discount as a decimal (e.g., 0.05 for 5%).
   */
  def expiryDiscount(product: Transaction): Double = {
    (30 - product.expiryDate.toEpochDay - product.transactionDate.toEpochDay)
      .toDouble / 100
  }

  /**
   * Checks if the cheese or wine discount applies.
   * @param product The transaction.
   * @return True if product is Cheese or Wine.
   */
  def isCheeseDiscount(product: Transaction): Boolean =
    Set("Wine", "Cheese").contains(product.productName)

  /**
   * Returns the cheese or wine discount (10% for Cheese, 5% for Wine).
   * @param product The transaction.
   * @return Discount as a decimal.
   */
  def cheeseDiscount(product: Transaction): Double =
    product.productName match {
      case "Cheese" => .10
      case "Wine"   => .05
      case _        => .00
    }

  /**
   * Checks if the March 23rd special discount applies.
   * @param product The transaction.
   * @return True if transaction date is March 23rd.
   */
  def isMarch23Discount(product: Transaction): Boolean =
    product.transactionDate.getMonth == Month.MARCH &&
      product.transactionDate.getDayOfMonth == 23

  /**
   * Returns the March 23rd special discount (50%).
   * @param product The transaction.
   * @return Discount as a decimal.
   */
  def march23Discount(product: Transaction): Double = .5

  /**
   * Checks if the quantity tier discount applies (quantity > 5).
   * @param product The transaction.
   * @return True if discount applies.
   */
  def isQuantityDiscount(product: Transaction): Boolean = product.quantity > 5

  /**
   * Returns the quantity tier discount based on quantity.
   * @param product The transaction.
   * @return Discount as a decimal.
   */
  def quantityDiscount(product: Transaction): Double =
    product.quantity match {
      case 6|7|8|9        => .05
      case 10|11|12|13|14 => .07
      case x if (x > 14)  => .10
      case _              => .00
    }

  /**
   * Checks if the app usage discount applies (channel is App).
   * @param product The transaction.
   * @return True if discount applies.
   */
  def isAppDiscount(product: Transaction): Boolean = product.channel.equalsIgnoreCase("App")

  /**
   * Returns the app usage discount (quantity rounded up to nearest 5, max 15%).
   * @param product The transaction.
   * @return Discount as a decimal.
   */
  def appDiscount(product: Transaction): Double =
    product.quantity match {
      case x if x > 15 => .15
      case _ => (((product.quantity - 1) / 5) + 1) * 0.05
    }

  /**
   * Checks if the Visa payment discount applies.
   * @param product The transaction.
   * @return True if payment method is Visa.
   */
  def isVisaDiscount(product: Transaction): Boolean =
    product.paymentMethod.equalsIgnoreCase("Visa")

  /**
   * Returns the Visa payment discount (5%).
   * @param product The transaction.
   * @return Discount as a decimal.
   */
  def visaDiscount(product: Transaction): Double = .05

  /**
   * All discount rules as a vector of (qualifier, discount) pairs.
   */
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