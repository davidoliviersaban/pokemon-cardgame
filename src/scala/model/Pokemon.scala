package model

import parser.DownloadResistance

import scala.io.Source

class Pokemon(number: String, id: String, name: String) {
  def number(): String = {
    if (override_number != null) {
      s"#$override_number"
    }
    else if (override_id != null) {
      s"#$override_id"
    }
    else number
  }

  def id(string : String) = {
    override_id = string
  }
  def number(string : String) = {
    override_number = string
  }
  def id() : String = {
    if (override_id == null) id else override_id
  }

  def name(): String = {
    if (elementType.size == 0) {
      name match {
        case "Darumacho" => {
          elementType(Seq("Feu","Feu"))
        }
        case "Darumacho_Transe" => {
          elementType(Seq("Feu","Psy"))
          image = s"$name.png"
        }
        case "Darumacho_de_Galar" => {
          elementType(Seq("Glace","Glace"))
        }
        case "Darumacho_de_Galar_Transe" => {
          elementType(Seq("Glace","Feu"))
          image = s"$name.png"
        }
        case "Motisma" => elementType(Seq("Electrik","Spectre"))
        case "Motisma_Chaleur" => {
          elementType(Seq("Electrik","Feu"))
          image = s"$name.png"
        }
        case "Motisma_Froid" => {
          elementType(Seq("Electrik","Glace"))
          image = s"$name.png"
        }
        case "Motisma_Helice" => {
          elementType(Seq("Electrik","Vol"))
          image = s"$name.png"
        }
        case "Motisma_Tonte" => {
          elementType(Seq("Electrik","Plante"))
          image = s"$name.png"
        }
        case "Motisma_Lavage" => {
          elementType(Seq("Electrik","Eau"))
          image = s"$name.png"
        }
        case "Zacian" => {
          elementType(Seq("Acier","Fee"))
        }
        case "Zamazenta" => {
          elementType(Seq("Acier","Combat"))
        }
        case _ =>
      }
    }
    Pokemon.fixName(name)
  }

  def elementType(seq :Seq[String]): Seq[String] = {
    elementType = seq.map(it => {ElementType.get(it).get}).sorted
    elementType
  }

  def type1 = {
    elementType(0)
  }

  def type2 = {
    elementType(1)
  }

  //  def id(): String = {id}
  //  def talents(): String = {Pokemon.fixTalentLoc(talents)}
  //  def localisations(): String = {Pokemon.fixTalentLoc(localisations)}
  var override_id : String = null
  var override_number : String = null
  var weight: Double = -1
  var size: Double = -1
  var capture: Int = -1
  var talents = Seq("","","")
  private var elementType: Seq[String] = Seq()
  var stats: Seq[Int] = Seq()
  var image: String = ""
  var competences: Seq[Competence] = Seq()

  def competences(comp : Seq[Competence]): Seq[Competence] = {
    comp.foreach(c => if (competences.find(a => a.name==c.name).isEmpty) competences :+= c)
    competences
  }

  def talents(list : Seq[String]): Unit = {
    list match {
      case Seq(a,b,c,d) => talents = Seq(a,b,c)
      case Seq(a,b,c) => talents = Seq(a,b,c)
      case Seq(a,b) => talents = Seq(a,b,"")
      case Seq(a) => talents = Seq(a,"","")
      case _ => talents = Seq("", "", "")
    }
  }

  override def toString: String = {
    s"${number},${id},${name()},${elementType.mkString(" ")},${elementType.mkString(",")},${stats.take(Stats.values.size).mkString(",")},${capture}"
  }

  def simplifiedToString(): String = {
    s"${name} ${elementType.mkString(" ")} ${stats.mkString(",")}"
  }

  def competencesToString(): String = {
    s"${name},${competences.take(2).map(_.name).mkString(",")},${elementType.mkString(",")}"
  }

  def talentsToString() : String = {
    s"${talents.take(3).mkString(",")}"
  }

  def infoToString(): String = {
    s"$name,$image,$size,$weight"
  }

  def getResistances : Seq[Double] = {
    println (toString())
    // elementType = elementType.map(ElementType.get(_).get).sorted
    DownloadResistance.resistance.find(res => ElementType.get(res.defense1).get == type1 && ElementType.get(res.defense2).get == type2).get.values
  }

}

case object Pokemon {
  def allHeaders() = {
    s"Number,Id,Types,Name,Type1,Type2,PV,Vitesse,Attaque,Defense,Att_Spe,Def_Spe,Capture,Competence1,CType1,Category1,Power1,Accuracy1,Competence2,CType2,Category2,Power2,Accuracy2,Competence3,CType3,Category3,Power3,Accuracy3,Competence4,CType4,Category4,Power4,Accuracy4,Image,${ElementType.values.mkString(",")}"
  }

  def allSimplifiedPokemonString(pokemon : Pokemon): String = {
    pokemon.elementType(pokemon.elementType)
    val pok : FightingPokemon =  new FightingPokemon(pokemon,0,0,0)
    s"${pokemon.number},${pokemon.id},${pokemon.elementType.mkString(" ")},${pok.toString},${pok.competencesToString},${pokemon.image},${pokemon.getResistances.mkString(",")}"
  }

  def simplifiedHeader(): String = {
    s"Name,Type1,Type2,PV,Vitesse,Attaque,Defense,Att_Spe,Def_Spe,Capture"
  }

  def header(): String = {
    s"Number,Id,Name,Types,Type1,Type2,${Stats.header()},Capture"
  }

  def talentsHeader() = {
    "Name,Talent1,Talent2,Talent3"
  }

  def competencesHeader: String = {
    "Name,Competence1,Competence2,Competence3,Competence4"
  }

  def infoHeader(): String = {
    "Name,Image,Size,Weigh"
  }

  def fixName(name: String): String = {
    name.replaceAll("Ï", "ï").replaceAll("Achéomire", "Archéomire").replaceAll(" ", "_")
  }

  def fixTalentLoc(name: String) = {
    name.replaceAll("<[^>]*>", ";").replaceAll(",+", ";").replaceAll(";+", ";")
  }

  def simplifiedPokemonString(pokemon : Pokemon): String = {
    new FightingPokemon(pokemon,0,0,0).toString
  }


  def parsePokemonStats(filename: String = s"generatedResources/pokemon8eGene.csv") = {
    var pokemons = collection.mutable.Seq[Pokemon]()
    Source.fromFile(filename).getLines().drop(1).foreach { line => {
      val values = line.split(",")
      val pokemon = new Pokemon(values(0), values(1), values(2))
      pokemon.elementType(Seq(values(4), values(5)))
      pokemon.stats = Seq(values(6).toInt, values(7).toInt, values(8).toInt, values(9).toInt, values(10).toInt, values(11).toInt)
      pokemon.capture = values(12).toInt
      pokemons :+= pokemon
    }
    }
    pokemons
  }
}
