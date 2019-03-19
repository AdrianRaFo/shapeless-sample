import shapeless._

object CoproductSample {

  case class Lead(name: String, alias: String, area: String)
  case class Senior(name: String, mastery: List[String], years: Int)
  case class Engineer(name: String, haskellian: Boolean)

  //Std

  sealed trait InheritanceDeveloper

  type optDeveloper = (Option[Lead], Option[Senior], Option[Engineer])

  type eitherDeveloper = Either[Lead, Either[Senior, Engineer]]

  //Shapeless

  type Developer = Lead :+: Senior :+: Engineer :+: CNil

  val lead   = Lead("Juan Pedro", "who is worth it", "Everything")
  val senior = Senior("pepegar", List("Alien", "FP"), 7)
  val engineer = Engineer("yawolf", true)

  val leadDev   = Coproduct[Developer](lead)
  val seniorDev = Coproduct[Developer](senior)
  val engineerDev = Coproduct[Developer](engineer)

  type PlainDeveloper = Senior :+: Engineer :+: CNil

  leadDev.eliminate(lead => Coproduct[PlainDeveloper](Senior(lead.name, List("FP"), 14)), identity) // Plain Developer

  leadDev.select[Lead]   //Some
  leadDev.select[Senior] // None

  leadDev.reverse // Junior :+: Senior :+: Lead :+: CNil
  leadDev.rotateLeft(1)  // Senior :+: Junior :+: Lead :+: CNil
  leadDev.rotateRight(1) // Junior :+: Lead :+: Senior :+: CNil

  val devList: List[Developer] = List(leadDev, seniorDev, engineerDev)

  object calculateDevPoints extends Poly1 {
    implicit def caseLead: Case.Aux[Lead, Int]     = at[Lead](_ => 5)
    implicit def caseSenior: Case.Aux[Senior, Int] = at[Senior](_.mastery.size + 1)
    implicit def caseJunior: Case.Aux[Engineer, Int] = at[Engineer](jn => if (jn.haskellian) 1 else 2)
  }

  devList.map(_.map(calculateDevPoints).unify).sum
}
