import shapeless.Lazy
object LazySample extends App {

  case class Commit(name: String, sha: String)
  case class Branch(subBranch: Option[Branch], commits: List[Commit])

  val commit1 = Commit("a", "1a2b")
  val commit2 = Commit("b", "3c4d")

  val develop = Branch(None, List(commit1, commit2))
  val master  = Branch(Some(develop), Nil)

  trait Show[T] {
    def apply(t: T): String
  }

  def show[T](t: T)(implicit s: Show[T]): String = s(t)

  implicit val showString: Show[String] = (t: String) => t

  implicit def showList[T](implicit s: Show[T]): Show[List[T]] =
    (t: List[T]) => t.map(show[T]).mkString("(", ", ", ")")

  implicit def showCommit(implicit s: Show[String]): Show[Commit] =
    (t: Commit) => s"Commit(name = ${show(t.name)(s)}, sha = ${show(t.sha)(s)})"

  show(commit1) //Commit(name = a, sha = 1a2b)
  show(commit2) //Commit(name = b, sha = 3c4d)

  object nonLazy {

    implicit def showOption[T](implicit s: Show[T]): Show[Option[T]] =
      (t: Option[T]) => t.map(show[T]).getOrElse("\"\"")

    implicit def showBranch(implicit sb: Show[Option[Branch]], sc: Show[List[Commit]]): Show[Branch] =
      (t: Branch) => s"Branch(subBranch = ${show(t.subBranch)(sb)}, commits = ${show(t.commits)(sc)})"

  }

  //import nonLazy._

  //show(develop) //diverging implicit expansion for type Show[Option[Branch]]
  //show(master) //diverging implicit expansion for type Show[Option[Branch]]

  object withLazy {

    implicit def showLazyOption[T](implicit s: Lazy[Show[T]]): Show[Option[T]] =
      (t: Option[T]) => t.map(show[T](_)(s.value)).getOrElse("\"\"")

    implicit def showLazyBranch(implicit sb: Lazy[Show[Option[Branch]]], sc: Show[List[Commit]]): Show[Branch] =
      (t: Branch) => s"Branch(subBranch = ${show(t.subBranch)(sb.value)}, commits = ${show(t.commits)(sc)})"

  }

  import withLazy._

  println(show(develop)) //Branch(subBranch = "", commits = (Commit(name = a, sha = 1a2b), Commit(name = b, sha = 3c4d)))
  println(show(master))  //Branch(subBranch = Branch(subBranch = "", commits = (Commit(name = a, sha = 1a2b), Commit(name = b, sha = 3c4d))), commits = ())

}
