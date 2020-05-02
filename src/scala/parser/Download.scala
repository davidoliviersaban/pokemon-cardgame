package parser

import java.io.{BufferedWriter, File, FileWriter}

import model._

import scala.io.Source
import scala.sys.process._

trait WriteFileGetFile {
  val pokemonFolder = "pokemons"
  val pokemonImagesFolder = "pokemons/images"
  val generatedDataFolder = "generatedResources"

  def getFile(url: String, file: String): Unit = {
    s"wget -nc ${url} -nv -O ${file}" !!
  }

  def writeFile(filename: String, header: String, list: Iterable[Any]): Unit = {
    val bw = new BufferedWriter(new FileWriter(new File(filename)))
    bw.write(header + "\n")
    for (item <- list) {
      bw.write(item.toString + "\n")
    }
    bw.close()
  }

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).filter(!_.isHidden).toList
    } else {
      List[File]()
    }
  }
}

abstract class Download extends WriteFileGetFile {
  var inoutfilename = ""

  def pokeListInputFile() = s"${generatedDataFolder}/${inoutfilename}.html"
  def outputFile() = s"${generatedDataFolder}/${inoutfilename}.csv"

  def getPokemonList: Seq[Pokemon] = { Seq[Pokemon]() }
  def writeFile(): Unit = {
    var pokemons = getPokemonList
    Pokepedia.downloadPokemons(pokemons)
    //writeFile("pokemonList.csv", Pokemon.header, pokemons)
    pokemons = pokemons.map(poke => Pokepedia.parsePokemonStats(poke))
    writeFile(s"${outputFile}", Pokemon.header, pokemons)
    writeFile(s"${generatedDataFolder}/${inoutfilename}.infos.csv", Pokemon.infoHeader(), pokemons.map(pok => pok.infoToString()))
    writeFile(s"${generatedDataFolder}/${inoutfilename}.competences.csv", "Name,Competence1,Competence2,Competence3,Competence4",pokemons.map(_.competencesToString()))
    writeFile(s"${generatedDataFolder}/${inoutfilename}.AllCompetences.csv", Competence.header,Pokepedia.competencesMap.toList.map(comp => comp._2))
    writeFile(s"${generatedDataFolder}/${inoutfilename}.simplified.csv", Pokemon.simplifiedHeader(),pokemons.map(Pokemon.simplifiedPokemonString(_)))
    writeFile(s"${generatedDataFolder}/${inoutfilename}.csv", Pokemon.allHeaders(),pokemons.sortBy(p => p.id).map(Pokemon.allSimplifiedPokemonString(_)))
  }
}

case object DownloadResistance extends Download {
  val resitances = "https://www.pokepedia.fr/Table_des_types#R.C3.A9sistance"
  val pokeResitanceInputFile = "resistances.html"
  val resistance = getResistanceList()

  def getResitances = {
    getFile(resitances, pokeResitanceInputFile)
  }

  def getResistanceList(): Seq[Resistance] = {
    if (!new File(pokeResitanceInputFile).exists) getResitances
    if (resistance != null) return resistance

    def lines = s"./src/scripts/parsePokeResistance.sh" !!

    val bw = new BufferedWriter(new FileWriter(new File("resistances.csv")))
    bw.write(lines.replaceFirst("^\n", ""))
    bw.close()

    ParseResistances().apply().toSeq
  }

  override def writeFile(): Unit = {
    writeFile("pokeResistances.csv", "Defense type," + ElementType.values.mkString(" "), getResistanceList())
  }

}

case object DownloadPokemon8thGeneration extends Download {
  val pokeList = "https://www.pokebip.com/page/jeuxvideo/pokemon-epee-bouclier/pokedex-galar"
  inoutfilename = "pokemon8eGene"

  def getPokeList = {
    getFile(pokeList, pokeListInputFile)
  }

  override def getPokemonList(): Seq[Pokemon] = {
    var pokemonList = collection.mutable.Set[Pokemon]()
    if (!new File(pokeListInputFile).exists) getPokeList
    val lines = ("./src/scripts/parsePokeFile.sh") !!
    val pattern1 = """<td >(\d+).*mon (.\d+).*<strong>(.+)</strong>.*Type ([a-z]+).8G.*</td><td >.*</td>""".r
    val pattern2 = "<td >(\\d+).*mon (.\\d+).*<strong>(.+)</strong>.*Type ([a-z]+).8G.*Type ([a-z]+).8G.*</td>.*</td>".r

    for (line <- lines.split("\n")) {
      line match {
        case pattern2(number, id, name, type1, type2) => val pok = new Pokemon(number, id, name); pok.elementType(Seq(ElementType.get(type1).get, ElementType.get(type2).get)); pokemonList += pok //println (s"${number} , ${id}, ${name}, $type1 $type2")
        case pattern1(number, id, name, type1) => val pok = new Pokemon(number, id, name); pok.elementType(Seq(ElementType.get(type1).get, ElementType.get(type1).get)); pokemonList += pok //println (s"${number} , ${id}, ${name}, $type1")
        case _ => println(s"Didn't match")
      }
    }
    pokemonList.toSeq.sortBy(p => p.number)
  }

}

case object DownloadPokemon1stGeneration extends Download {
  val pokeList = "https://www.pokepedia.fr/Liste_des_Pok%C3%A9mon_de_la_premi%C3%A8re_g%C3%A9n%C3%A9ration"
  inoutfilename = "pokemon1eGene"

  def getPokeList = {
    getFile(pokeList, pokeListInputFile)
  }

  override def getPokemonList(): Seq[Pokemon] = {
    var pokemonList = collection.mutable.Set[Pokemon]()
    if (!new File(pokeListInputFile).exists) getPokeList

    var allLines = ""
    val pattern1 = """.*<tr><td>(\d+)</td>.*<a[^>]*">([^>]+)</a>.*""".r

    Source.fromFile(pokeListInputFile).getLines().foreach { line => allLines += line }
    allLines.replaceAll("\r|\n", "").split("</tr>").map {
      line =>
        line match {
          case pattern1(id, name) => pokemonList += new Pokemon(id, "#" + id, name) //println (s"${number} , ${id}, ${name}, $type1")
          case _ =>  println(s"Didn't match: ")
        }
    }
    pokemonList.toSeq.sortBy(p => p.number)
  }

}

case object DownloadPokemon7thGeneration extends Download {
  val pokeList = "https://www.pokepedia.fr/Liste_des_Pok%C3%A9mon_de_la_septi%C3%A8me_g%C3%A9n%C3%A9ration"
  inoutfilename = "pokemon7eGene"

  def getPokeList = {
    getFile(pokeList, pokeListInputFile)
  }

  override def getPokemonList(): Seq[Pokemon] = {
    var pokemonList = collection.mutable.Set[Pokemon]()
    if (!new File(pokeListInputFile).exists) getPokeList

    var allLines = ""
    val pattern1 = """.*<tr><td>(\d+)</td>.*<a[^>]*">([^>]+)</a>.*""".r
    val patternIdAndNext = """.*<tr><td>([0-9]{1,4})</td>(.*)""".r
    val patternNameAndNext = """<a[^>]*">([^>]+)</a>(.*)""".r

    Source.fromFile(pokeListInputFile).getLines().foreach { line => allLines += line }
    allLines.replaceAll("\r|\n", "").split("</tr>").map {
      line =>
        line match {
//          case pattern1(id, name) => pokemonList += new Pokemon(id, "#" + id, name) //println (s"${number} , ${id}, ${name}, $type1")
          case patternIdAndNext(id, next) =>
            print(s"Id found: $id")
            next match {
              case patternNameAndNext(name,next2) => pokemonList += new Pokemon(id, "#" + id, name)
              case _ => println(s"$id cannot extract name from: $next")
            }
          case _ =>  println(s"Cannot extract id from line: ${line.substring(0,20)}")
        }
    }
    pokemonList.toSeq.sortBy(p => p.number)
  }

}


case object DownloadPokemonAnyGeneration extends Download {
  inoutfilename = "anyGeneration"

  override def getPokemonList(): Seq[Pokemon] = {
    automaticPokemonList()
    // manualPokemonList(Seq("Riolu","Morpeko","Darumacho","Darumacho_de_Galar"))
  }

  def automaticPokemonList() = {
    var pokemonList = collection.mutable.Set[Pokemon]()

    val allLines = getListOfFiles(s"${pokemonFolder}")
    allLines.foreach {
      line => pokemonList += new Pokemon(null,null,line.getName.replaceAll(".html",""))
    }
    pokemonList.toSeq.sortBy(p => p.name)
  }
/*
  def manualPokemonList(): Seq[Pokemon] = {
    var list : Seq[Pokemon] = Seq[Pokemon]()
    list :+= new  Pokemon("266","618", "Limonde_de_Galar")
    list :+= new  Pokemon("251","110", "Smogogo_de_Galar")
    list :+= new  Pokemon("079","344", "Galopa_de_Galar")
    //    list :+= new  Pokemon("??","080", "Flagadoss_de_Galar")
    list :+= new  Pokemon("031","263", "Zigzaton_de_Galar")
    list :+= new  Pokemon("367","554", "Darumarond_de_Galar")
    //    list += new  Pokemon("??","618", "Roigada_de_Galar")
    //    list += new  Pokemon("??","264", "Linéon_de_Galar")
    list :+= new  Pokemon("368","555", "Darumacho_de_Galar")
    //    list += new  Pokemon("??","145", "Électhor_de_Galar")
    list :+= new  Pokemon("333","077", "Ponyta_de_Galar")
    //    list :+= new  Pokemon("??","079", "Ramoloss_de_Galar")
    list :+= new  Pokemon("192","052", "Miaouss_de_Galar")
    list :+= new  Pokemon("183","863", "Berserkatt")
    //    list :+= new  Pokemon("??","146", "Sulfura_de_Galar")
    list
  }
*/
  def manualPokemonList(names : Seq[String]): Seq[Pokemon] = {
    names.map(name => new  Pokemon(null,null, name))
  }

}


object DoDownload {

  def main(args: Array[String]) {
    DownloadPokemon7thGeneration.writeFile()
    /*
    DownloadResistance.writeFile()
    DownloadPokemon8thGeneration.writeFile()
    DownloadPokemon1stGeneration.writeFile()
     */
    //DownloadPokemonAnyGeneration.writeFile()
  }

}

