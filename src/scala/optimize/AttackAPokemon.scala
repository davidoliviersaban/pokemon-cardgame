package optimize

import model._

class AttackAPokemon(pokemonToBeat: String, niveauPokemonToBeat: Int = 50, myNiveau: Int = 50, puissance: Int = 100, displayFirsts: Int = 20) {
  val optimize = new AttackCommon()

  def pokemons = optimize.pokemons
  var strongAtt = optimize.strongAtt
  var strongDef = optimize.strongDef
  var strongAvg = optimize.strongAvg

  def getStrongAttackAgainst(): Seq[(Pokemon, Double)] = {
    val enemy = pokemons.find(_.name.contains(pokemonToBeat)).get
    var strong = Map[Pokemon, Double]()
    pokemons.foreach(
      poke => {
        strong += (poke -> Fight.attack(enemy, poke, myNiveau, puissance).max)
      })
    strong.toSeq.sortBy(_._2).reverse
  }

  def getStrongDefenseAgainst(): Seq[(Pokemon, Double)] = {
    val enemy = pokemons.find(_.name.contains(pokemonToBeat)).get
    var strong = Map[Pokemon, Double]()
    pokemons.foreach(
      poke => {
        strong += (poke -> Fight.attack(poke, enemy, niveauPokemonToBeat, puissance).max)
      })
    strong.toSeq.sortBy(_._2)
  }

  def computeAllAgainst(): Unit = {
    strongAtt = getStrongAttackAgainst()
    strongDef = getStrongDefenseAgainst()
    var strong = Map[Pokemon, Double]()
    pokemons.foreach(name => strong += (name -> (strongAtt.find(_._1 == name).get._2 - strongDef.find(_._1 == name).get._2)))
    strongAvg = strong.toSeq.sortBy(_._2).reverse
  }

  def displayAll() = {
    computeAllAgainst()
    println
    println(s"Strong Attack against ${pokemonToBeat}:")
    strongAtt.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"Strong Defense against ${pokemonToBeat}:")
    strongDef.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"Average Def + Att against ${pokemonToBeat}")
    strongAvg.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))
  }

  def displayMyPokemonAgainst(myPokemon: String): Unit = {
    computeAllAgainst()
    println
    println(s"Strong Attack against ${pokemonToBeat}:")
    strongAtt.filter(_._1.name == myPokemon).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"Strong Defense against ${pokemonToBeat}:")
    strongDef.filter(_._1.name == myPokemon).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"Average Def + Att against ${pokemonToBeat}")
    strongAvg.filter(_._1.name == myPokemon).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))
  }

}

case object AttackAPokemon {

  def main(args: Array[String]): Unit = {
    val pokemonToBeat = "tamorph"
    val attackAPokemon = new AttackAPokemon(pokemonToBeat)
    attackAPokemon.displayAll()
    attackAPokemon.displayMyPokemonAgainst("Rhinocorne")
  }
}