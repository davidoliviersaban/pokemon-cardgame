package optimize

import model._

import scala.io.Source

class AttackCommon() {
  var pokemons: Seq[Pokemon] = Pokemon.parsePokemonStats()

  var strongAtt: Seq[(Pokemon, Double)] = Seq()
  var strongDef: Seq[(Pokemon, Double)] = Seq()
  var strongAvg: Seq[(Pokemon, Double)] = Seq()

}

case object Fight {
  var resistances: Seq[Resistance] = Resistance.parseResistances()

  def attack(defending: Pokemon, attacking: Pokemon, attackingLevel: Int, puissance: Int): Seq[Double] = {
    val att1 = ((attackingLevel * 0.4 + 2) * attacking.stats(Stats.Attaque) * puissance / defending.stats(Stats.Defense) / 50 + 2)
    val att2 = ((attackingLevel * 0.4 + 2) * attacking.stats(Stats.AttSpe) * puissance / defending.stats(Stats.DefSpe) / 50 + 2)
    val resist = resistances.find(
      res => {
        ElementType.get(res.defense1) == ElementType.get(defending.type1) && ElementType.get(res.defense2) == ElementType.get(defending.type2)
      }).get

    Seq(
      att1 * resist.values(ElementType.id(attacking.type1)),
      att1 * resist.values(ElementType.id(attacking.type2)),
      att2 * resist.values(ElementType.id(attacking.type1)),
      att2 * resist.values(ElementType.id(attacking.type2))
    )
  }

  /*
  def defense(enemy: Pokemon, myPokemon: Pokemon, niveau: Int, puissance: Int): Seq[Double] = {
    val att1 = ((niveau * 0.4 + 2) * enemy.stats(Stats.Attaque) * puissance / myPokemon.stats(Stats.Defense) / 50 + 2)
    val att2 = ((niveau * 0.4 + 2) * enemy.stats(Stats.AttSpe) * puissance / myPokemon.stats(Stats.DefSpe) / 50 + 2)
    val resist = resistances.find(
      res => {
        res.defense1 == myPokemon.type1 && res.defense2 == myPokemon.type2
      }).get

    Seq(
      att1 * resist.values(ElementType.id(enemy.type1)),
      att1 * resist.values(ElementType.id(enemy.type2)),
      att2 * resist.values(ElementType.id(enemy.type1)),
      att2 * resist.values(ElementType.id(enemy.type2))
    )
  }*/

}
