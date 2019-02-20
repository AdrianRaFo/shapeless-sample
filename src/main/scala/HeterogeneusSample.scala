import shapeless.{HMap, HNil}

trait HSample {
  sealed abstract class User(val name: String)
  case class Admin(override val name: String, email: String) extends User(name)
  case class NormalUser(override val name: String, age: Int) extends User(name)

  val admin      = Admin("Ignacio", "ignacio@rocks.com")
  val normalUser = NormalUser("Santiago", 30)
}

//noinspection TypeAnnotation
object HListSample extends HSample {

  val userList: List[User] = List(admin, normalUser)
  userList.head.name
  //userList.head.email //not works
  //userList.last.age   //not works

  val userHList = admin :: normalUser :: HNil

  userHList.head.name
  userHList.head.email //works
  userHList.last.age   //works
}

//noinspection TypeAnnotation
object HMapSample extends HSample {

  val intUserMap = Map(1     -> admin, 2              -> normalUser)
  val strUserMap = Map(admin -> "coolGuy", normalUser -> "theOtherGuy")

  val userFromIndex = intUserMap.get(1)
  userFromIndex.flatMap(strUserMap.get)
  userFromIndex.map(_.name)
  //userFromIndex.map(_.email) //not works

  class BiUserMap[K, V]
  implicit val intToAdmin        = new BiUserMap[Int, Admin]
  implicit val intToNormalUser   = new BiUserMap[Int, NormalUser]
  implicit val adminToLabel      = new BiUserMap[Admin, String]
  implicit val normalUserToLabel = new BiUserMap[NormalUser, String]

  val hm: HMap[BiUserMap] = HMap[BiUserMap](1 -> admin, admin -> "coolGuy", 2 -> normalUser, normalUser -> "theOtherGuy")

  val adminFromIndex: Option[Admin] = hm.get[Int, Admin](1)

  adminFromIndex.map(_.name)
  adminFromIndex.map(_.email) //works
  adminFromIndex.flatMap(hm.get(_))
}
