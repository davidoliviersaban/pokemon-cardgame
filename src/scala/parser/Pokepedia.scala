package parser

import java.io.File

import model._
import optimize.AttackCommon

import scala.io.Source
import scala.sys.process._
import utils.StringUtils

case object Pokepedia extends Download {

  var competencesMap: Map[String,Competence] = Map()

  def downloadPokemons(list: Seq[Pokemon]) {
    list.foreach(pokemon => downloadPokemons(pokemon))
  }

  def downloadPokemons(pokemon: Pokemon): Unit = {
    if (!new File(s"pokemons/${pokemon.name}.html").exists()) getFile(s"https://www.pokepedia.fr/${pokemon.name}", s"pokemons/${pokemon.name}.html")
  }

  def parsePokemonStats(pokemon: Pokemon): Pokemon = {
    var allLines = ""
    downloadPokemons(pokemon)
    println(s"Parsing ${pokemon.name}")
    Source.fromFile(s"pokemons/${pokemon.name}.html").getLines().foreach { line => allLines += line }
    val regex = """.*</a></td><td>(\d+)</td>.*""".r
    val stats: Seq[Int] = allLines.replaceAll("\r|\n", "").split("</tr>").filter(line => line.contains("""/Statistique#""")).map {
      line =>
        line match {
          case regex(value) => value.toInt
          case _ => -1
        }
    }.filter(_ > 0).toSeq
    pokemon.stats = stats
/*    parseInfo(pokemon)
  }

  def parseInfo(pokemon: Pokemon): Pokemon = {
    var allLines = ""
    downloadPokemons(pokemon)
    //println(s"Parsing ${pokemon.name}")
    Source.fromFile(s"pokemons/${pokemon.name}.html").getLines().foreach { line => allLines += line }
 */
    val regexId = """.*title=".*Num.rotation nationale">[^\d]+(\d\d\d)<.*""".r
    val regexCapacite = """.*<a href="/.*" title="([^"]+)">[^<]+</a>.*""".r
    val regexCapacite_Comp = """.*<img alt="(Physique|Spécial|Statut)".*""".r
    val regexCapacite_Type = """.*<img alt="([ÉA-Z]*[^" (_0-9]+)".*""".r
    val regexCapacite_Power = """<td>(\d+|[^\d])$""".r
    val regexCapacite_Precision = """<td>(\d+.{0,6}%|[^\d])$""".r
    val regexTalents1 = """.*>Talents?</a></th><td colspan="3">.*title="[^"]+">([^<]+)</a>.*""".r
    val regexTalents2 = """.*>Talents?</a></th><td colspan="3">.*title="[^"]+">([^<]+)</a>.*title="[^"]+">([^<]+)</a>.*""".r
    val regexTalents3 = """.*>Talents?</a></th><td colspan="3">.*title="[^"]+">([^<]+)</a>.*title="[^"]+">([^<]+)</a><.*title="[^"]+">([^<]+)</a>.*""".r
    val regexTalents4 = """.*>Talents?</a></th><td colspan="3">.*title="[^"]+">([^<]+)</a>.*title="[^"]+">([^<]+)</a><.*title="[^"]+">([^<]+)</a>.*<.*title="[^"]+">([^<]+)</a>.*""".r
    val regexCapture = """.*Taux de capture</a></div><div class="smw-table-cell smwprops">(\d+).*""".r
    val regexTaille = """.*Taille</a></th><td colspan="3">([,\d]+) ?m.*""".r
    val regexPoids = """.*Poids</a></th><td colspan="3">([,\d]+) ?kg.*""".r
    val regexType1 = """.*<th><a href="/Type" title="Type">(Type|Types)</a></th><td colspan="3"><a href="/[^"]*" title="(.+) \(type\)"><img[^>]*></a>.*""".r
    val regexType2 = """.*<th><a href="/Type" title="Type">(Type|Types)</a></th><td colspan="3"><a href="/[^"]*" title="(.+) \(type\)"><img[^>]*></a>.*<a href="/[^"]*" title="(.+) \(type\)"><img[^>]*></a>.*""".r

    var competence: Competence = new Competence
    var competences: Seq[Competence] = Seq[Competence]()

    allLines.split("""<tr>|</tr>|</td>""")
      //      .filter(line => {
      //        true
      //      })
      .foreach {
        line =>
//          if (line.contains("rotation nationale")) {
//           println (line)
//          }
          line match {
            case regexId(id) => pokemon.id(id)
            case regexTalents4(talent1, talent2, talent3, talent4) => pokemon.talents(Seq(talent1, talent2, talent3, talent4).filter(!_.contains("Talent cach")))
            case regexTalents3(talent1, talent2, talent3) => pokemon.talents(Seq(talent1, talent2, talent3).filter(!_.contains("Talent cach")))
            case regexTalents2(talent1, talent2) => pokemon.talents(Seq(talent1, talent2).filter(!_.contains("Talent cach")))
            case regexTalents1(talent1) => pokemon.talents(Seq(talent1).filter(!_.contains("Talent cach")))
            case regexCapture(capture) => pokemon.capture = capture.toInt
            case regexTaille(taille) => pokemon.size = taille.replaceAll(",", ".").toDouble
            case regexPoids(poids) => pokemon.weight = poids.replaceAll(",", ".").toDouble
            case regexType2(skip,type1, type2) => pokemon.elementType(Seq(type1, type2))
            case regexType1(skip,type1) => pokemon.elementType(Seq(type1, type1))
            case regexCapacite(capacite) => {
              if (competence.completed()) {
                competences :+= competence
                competencesMap += (competence.name -> competence)
                competence = new Competence
              }
              competence.name = StringUtils.stripAccents(capacite).replaceAll(" ?\t?\\(capacite\\)","")
            }
            case regexCapacite_Comp(capacite_comp) => {
              if (competence.name != null && competence.name.length >  0) competence.physical(capacite_comp)
            }
            case regexCapacite_Type(capacite_type) => {
              if (competence.name != null && competence.name.length >  0 && !ElementType.get(capacite_type).isEmpty) competence.elementType(capacite_type)
            }
            case regexCapacite_Power(power) => {
              if (competence.completed() && competence.power < -1) {
                if (power.matches("\\d+")) competence.power = power.toInt else competence.power = -1
              }
            }
            case regexCapacite_Precision(precision) => {
              if (competence.completed()) {
//                println(precision)
                competence.accuracy = precision.replaceAll("&#160;%?","").toInt
                competences :+= competence
                competencesMap += (competence.name -> competence)
              }
//              println(s"precision : $precision, competence: $competence")
              competence = new Competence
            }
            case _ => //println(line)
          }
      }
    val file = s"ls pokemons/images/" !!
    val image = file.split("\n").filter(_.contains(pokemon.name))

    if (image.length > 0) pokemon.image = image(0)
    if (competences.size > 0) pokemon.competences(competences)
    pokemon
  }

  def main(args: Array[String]): Unit = {
    val pokemons = new AttackCommon().pokemons
    pokemons.filter(_.name.contains("Raichu")).foreach(poke => parsePokemonStats(poke))
    //pokemons.filter(_.name.contains("Chrysacier")).foreach(poke => parseInfo(poke))
  }

}
