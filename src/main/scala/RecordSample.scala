import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

object RecordSample {

  val book =
    ("author" ->> "Benjamin Pierce") ::
      ("title" ->> "Types and Programming Languages") ::
      ("id" ->> 262162091) ::
      ("price" ->> 44.11) ::
      HNil

  //Get values
  book("author")
  //Benjamin Pierce

  //Add values
  book + ("inPrint" ->> true)

  //Delete values
  book - "id"
}
