import shapeless.tag
import shapeless.tag.@@

object NoTagSample {
  case class User(name: String, email: String, address: String, country: String)

  User("Ignacio", "ignacio@rocks.com", "Avda Alfonso XIII", "Spain") //compile
  User("ignacio@rocks.com", "Ignacio", "Spain", "Avda Alfonso XIII") //compile
}

object TagSample1 {

  type Email   = String @@ EmailT
  type Country = String @@ CountryT

  //Approach 1

  sealed trait EmailT

  sealed trait CountryT

  case class UserWithTags(name: String, email: Email, address: String, country: Country)

  //UserWithTags("ignacio@rocks.com", "Ignacio", "Spain", "Avda Alfonso XIII") //do not compile
  UserWithTags("Ignacio", tag[EmailT]("ignacio@rocks.com"), "Avda Alfonso XIII", tag[CountryT]("Spain")) //compile

}

object TagSample2 {

  trait TaggedTypeOps[T] {
    def apply(t: T): T @@ Tag = tag[Tag][T](t)

    sealed trait Tag
  }

  object Email extends TaggedTypeOps[String]

  object Country extends TaggedTypeOps[String]

  type Email   = String @@ Email.Tag
  type Country = String @@ Country.Tag

  case class UserWithTagsOps(name: String, email: Email, address: String, country: Country)

  UserWithTagsOps("Ignacio", Email("ignacio@rocks.com"), "Avda Alfonso XIII", Country("Spain"))

}
