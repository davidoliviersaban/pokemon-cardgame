package model

object Stats {
  val values: Seq[String] = Seq("PV", "Attack", "Defense", "Att_Spe", "Def_Spe", "Vitesse")

  def header(): String = {
    values.mkString(",")
  }

  def id(name: String) = {
    values.indexOf(name)
  }

  val PV = 0
  val Attaque = 1
  val Defense = 2
  val AttSpe = 3
  val DefSpe = 4
  val Vitesse = 5
}
