package optimize

import model.Pokemon

case class AttackWithPokemon(myPokemon: String, niveauDef: Int = 50, myNiveau: Int = 50, puissance: Int = 100, displayFirsts: Int = 20) {
  val optimize = new AttackCommon

  def pokemons = optimize.pokemons

  var strongAtt = optimize.strongAtt
  var strongDef = optimize.strongDef
  var strongAvg = optimize.strongAvg

  def getStrongAttackAgainst(): Seq[(Pokemon, Double)] = {
    val poke = pokemons.find(_.name == myPokemon).get
    var strong = Map[Pokemon, Double]()

    pokemons.foreach(
      enemy => {
        strong += (enemy -> Fight.attack(enemy, poke, myNiveau, puissance).max)
      })
    strong.toSeq.sortBy(_._2).reverse
  }

  def getStrongDefenseAgainst(): Seq[(Pokemon, Double)] = {
    val poke = pokemons.find(_.name == myPokemon).get
    var strong = Map[Pokemon, Double]()
    pokemons.foreach(
      enemy => {
        strong += (enemy -> Fight.attack(poke, enemy, niveauDef, puissance).max)
      })
    strong.toSeq.sortBy(_._2)
  }

  def computeAllAgainst(): Unit = {
    strongAtt = getStrongAttackAgainst()
    strongDef = getStrongDefenseAgainst()
    var strong = Map[Pokemon, Double]()
    pokemons.foreach(name => strong += (name -> (strongAtt.find(_._1 == name).get._2 + 200 - strongDef.find(_._1 == name).get._2) / 2))
    strongAvg = strong.toSeq.sortBy(_._2).reverse
  }

  def displayAll() = {
    computeAllAgainst()
    println
    println(s"My Pokemon ${myPokemon} has Strong Attack against:")
    strongAtt.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"My Pokemon ${myPokemon} has Strong Defense against:")
    strongDef.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"My Pokemon ${myPokemon} has average Attack + Defense against:")
    strongAvg.take(displayFirsts).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))
  }

  def displayMyPokemonAgainst(pokemonToBeat: String): Unit = {
    computeAllAgainst()
    println
    println(s"My Pokemon ${myPokemon} has Strong Attack against:")
    strongAtt.filter(_._1.name == pokemonToBeat).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"My Pokemon ${myPokemon} has Strong Defense against:")
    strongDef.filter(_._1.name == pokemonToBeat).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))

    println
    println(s"My Pokemon ${myPokemon} has average Attack + Defense against:")
    strongAvg.filter(_._1.name == pokemonToBeat).foreach(it => println(s"${Math.round(it._2).toInt} -> " + it._1.simplifiedToString()))
  }

}

case object AttackWithPokemon {

  def main(args: Array[String]): Unit = {
    val myPokemon = "Nénupiot"
    val pokemon = AttackWithPokemon(myPokemon, myNiveau = 20)
    pokemon.displayAll()
    pokemon.displayMyPokemonAgainst("Zamazenta")
  }

}
