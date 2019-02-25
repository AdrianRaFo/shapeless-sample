object TuplesSample extends App{
  //shapeless allows standard Scala tuples to be manipulated in exactly the same ways as HLists

  import shapeless.syntax.std.tuple._

  (23, "foo", true).head
  (23, "foo", true).tail
  (23, "foo", true).drop(2)
  (23, "foo", true).take(2)
  (23, "foo", true).split(1)
  23 +: ("foo", true)
  (23, "foo") :+ true

  import shapeless.poly._
  import shapeless.Id

  object option extends (Id ~> Option) {
    def apply[T](t: T) = Option(t)
  }
  (23, "foo", true) map option

  (23, "foo", true).productElements
}
