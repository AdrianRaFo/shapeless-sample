import shapeless.syntax.typeable._

object SafeCast {

  val l: Any = List(Vector("foo", "bar", "baz"), Vector("wibble"))

  l.cast[List[Vector[String]]]
  //Some(List(Vector(foo, bar, baz), Vector(wibble)))

   l.cast[List[Vector[Int]]]
   //None

}
