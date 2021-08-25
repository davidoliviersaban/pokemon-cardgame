package model

object ElementType {
  val values: Seq[String] = Seq("Acier", "Combat", "Dragon", "Eau", "Electrik", "Fee", "Feu", "Glace", "Insecte", "Normal", "Plante", "Poison", "Psy", "Roche", "Sol", "Spectre", "Tenebres", "Vol")
  //val Acier,Combat,Dragon,Eau,Electrik,Fee,Feu,Glace,Insecte,Normal,Plante,Poison,Psy,Roche,Sol,Spectre,Tenebres,Vol = "Acier","Combat","Dragon","Eau","Electrik","Fee","Feu","Glace","Insecte","Normal","Plante","Poison","Psy","Roche","Sol","Spectre","Tenebres","Vol"

  def get(name: String): Option[String] = {
    var result: Option[String] = None
    if (name.length >= 3)
      result = values.find(_.toLowerCase == name.replaceAll("É", "E").replaceAll("é|è","e").toLowerCase)
    //if (result.isEmpty) null else result.get
    result
  }

  def id(name: String): Int = {
    val actual_name = get(name)
    if (actual_name.isEmpty) -1
    else values.indexOf(actual_name.get)
  }

  def id(name: Option[String]): Int = {
    if (name.isEmpty) -1
    else values.indexOf(name.get)
  }
}
