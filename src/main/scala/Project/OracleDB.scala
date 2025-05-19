package Project

import java.sql.{Connection, DriverManager, PreparedStatement, Statement, Date}

object OracleDB {

  val url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
  val user = "scalaDiscount"
  val password = "123"
  val conn: Connection = DriverManager.getConnection(url, user, password)

  def write(tx: TransactionHandler.Transaction){
    val sql =
      """INSERT INTO transactions
        |("timestamp", product_name, expiry_date, quantity, unit_price, channel, payment_method, discount, final_price_after_discount)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""".stripMargin
    val pstmt: PreparedStatement = conn.prepareStatement(sql)
    pstmt.setString(1, tx.transactionDate.toString + 'T' + tx.transactionTime)
    pstmt.setString(2, tx.productName + " - " + tx.productDescription)
    pstmt.setString(3, tx.expiryDate.toString)
    pstmt.setInt(4, tx.quantity)
    pstmt.setDouble(5, tx.unitPrice)
    pstmt.setString(6, tx.channel)
    pstmt.setString(7, tx.paymentMethod)
    pstmt.setDouble(8, tx.discount)
    pstmt.setDouble(9, tx.finalPriceAfterDiscount)
    pstmt.executeUpdate()
    pstmt.close()
  }
}

