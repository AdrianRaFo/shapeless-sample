import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

object RecordSample {

  //Std

  val strFieldsBook = Map("author" -> "Benjamin Pierce", "title" -> "Types and Programming Languages")

  val numFieldsBook = Map(("id" -> 262162091, "price" -> 44.11))

  val bookSection = Map(101 -> "History")

  //Shapeless

  val book = ("author" ->> "Benjamin Pierce") :: (101 ->> "History") ::
    ("id" ->> 262162091) :: ("price" ->> 44.11) :: HNil

  //Get values
  book("author") //Benjamin Pierce

  //Add values
  book + ("inPrint" ->> true)

  //Delete values
  book - "id"
}
