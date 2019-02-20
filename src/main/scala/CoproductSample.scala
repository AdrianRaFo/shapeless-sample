import shapeless._

object CoproductSample {

  case class Lead(name: String, alias: String, mastery: String)
  case class Senior(name: String, mastery: List[String], years: Int)
  case class Junior(name: String, daysWithoutBreakMaster: Int)

  type Developer = Lead :+: Senior :+: Junior :+: CNil

  val lead   = Lead("Juan Pedro", "who is worth it", "Everything")
  val senior = Senior("pepegar", List("Alien", "FP"), 7)
  val junior = Junior("yawolf", 31)

  val leadDev = Coproduct[Developer](lead)
  val seniorDev = Coproduct[Developer](senior)
  val juniorDev = Coproduct[Developer](junior)

  val devList : List[Developer] = List(leadDev, seniorDev, juniorDev)

  object calculateDevPoints extends Poly1 {
    implicit def caseLead: Case.Aux[Lead, Int]   = at[Lead](_ => 5)
    implicit def caseSenior: Case.Aux[Senior, Int] = at[Senior](_.mastery.size + 1)
    implicit def caseJunior: Case.Aux[Junior, Int] = at[Junior](jn => if (jn.daysWithoutBreakMaster >= 31) 2 else 1)
  }

  devList.map(_.map(calculateDevPoints).unify).sum
}
