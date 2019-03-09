import shapeless.Poly1

import scala.util.Try

object PolySample {

  object readDouble extends Poly1 {
    implicit def caseInt    = at[Int](_.toDouble)
    implicit def caseString = at[String](str => Try(str.replace(',', '.').toDouble).getOrElse(0))
  }

  readDouble(23) //23.0

  readDouble("123") //123.0

  readDouble("123.1") //123.1

  readDouble("123,1") //123.1

  readDouble("foo") //0

}
