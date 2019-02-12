import shapeless.tag
import shapeless.tag.@@

trait TagSample {
  case class User(name: String, email: String, address: String, country: String)

  User("Ignacio", "ignacio@rocks.com", "Avda Alfonso XIII", "Spain") //compile
  User("ignacio@rocks.com", "Ignacio", "Spain", "Avda Alfonso XIII") //compile
}

object TagSample1 extends TagSample {

  type Email   = String @@ EmailT
  type Country = String @@ CountryT

  //Approach 1

  sealed trait EmailT

  sealed trait CountryT

  case class UserWithTags(name: String, email: Email, address: String, country: Country)

  //UserWithTags("ignacio@rocks.com", "Ignacio", "Spain", "Avda Alfonso XIII") //do not compile
  UserWithTags("Ignacio", tag[EmailT][String]("ignacio@rocks.com"), "Avda Alfonso XIII", tag[CountryT][String]("Spain")) //compile
  User("Ignacio", tag[EmailT][String]("ignacio@rocks.com"), "Avda Alfonso XIII", tag[CountryT][String]("Spain"))         //compile

}

object TagSample2 extends TagSample {

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
  User("Ignacio", Email("ignacio@rocks.com"), "Avda Alfonso XIII", Country("Spain"))

}
