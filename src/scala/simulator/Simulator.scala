import model.{ElementType, FightingPokemon, Pokemon, Resistance}
import optimize.Fight

case object FightingPokemon {
  var resistances: Seq[Resistance] = Resistance.parseResistances()
  def simplifiedAttack(defending: FightingPokemon, attacking: FightingPokemon): Seq[Double] = {
    val att = attacking.Attaque - defending.Defense + attacking.puissance/10
    val attSpe = attacking.AttSpe - defending.DefSpe + attacking.puissanceSpe/10
    val resist = resistances.find(
      res => {
        ElementType.get(res.defense1).get == defending.type1 && ElementType.get(res.defense2).get == defending.type2
      }).get

    Seq(
      att * resist.values(ElementType.id(attacking.type1)),
      att * resist.values(ElementType.id(attacking.type2)),
      attSpe * resist.values(ElementType.id(attacking.type1)),
      attSpe * resist.values(ElementType.id(attacking.type2))
    )
  }

}

case object Simulator {

  val pokemons : Seq[Pokemon] = Pokemon.parsePokemonStats("generatedResources/pokemon1eGene.csv")

  def simplified(pok1: FightingPokemon, pok2 : FightingPokemon) = {
//    println("-- Simplified --")
    var round = 0
    while (pok1.PV > 0 && pok2.PV > 0) {
      round += 1
      val attacking = if (pok1.Vitesse >= pok2.Vitesse) pok1 else pok2
      val defending = if (pok1.Vitesse <  pok2.Vitesse) pok1 else pok2
      defending.hit(FightingPokemon.simplifiedAttack(defending,attacking))
      if (defending.PV > 0) {
        attacking.hit(FightingPokemon.simplifiedAttack(attacking,defending))
      }
//      println(s"${pok1.name}: ${pok1.PV}    vs     ${pok2.name}: ${pok2.PV}")
    }
    //println(s"-- Simple -- Finished in ${round} rounds ${pok1.name}: ${pok1.PV}    vs     ${pok2.name}: ${pok2.PV}")
    Seq(round, pok1.PV, pok2.PV)
  }

  def normal(pok1: FightingPokemon, pok2 : FightingPokemon) = {
//    println("-- Normal --")
    var round = 0
    while (pok1.PV > 0 && pok2.PV > 0) {
      round+=1
      val attacking = if (pok1.Vitesse >= pok2.Vitesse) pok1 else pok2
      val defending = if (pok1.Vitesse <  pok2.Vitesse) pok1 else pok2
      defending.hit(Fight.attack(defending.pokemon,attacking.pokemon, attacking.niveau(), attacking.puissance()))
      if (defending.PV > 0) {
        attacking.hit(Fight.attack(attacking.pokemon,defending.pokemon, defending.niveau(), defending.puissance()))
      }
//      println(s"${pok1.name}: ${pok1.PV}    vs     ${pok2.name}: ${pok2.PV}")
    }
    //println(s"-- Normal -- Finished in ${round} rounds ${pok1.name}: ${pok1.PV}    vs     ${pok2.name}: ${pok2.PV}")
    Seq(round, pok1.PV, pok2.PV)
  }

  def get(name: String) = {
    Simulator.pokemons.find(_.name.contains(name)).get
  }

  def compare(fightingPokemon1: FightingPokemon, fightingPokemon2: FightingPokemon) = {
    fightingPokemon1.dmg = 0
    fightingPokemon2.dmg = 0
    val stat1 = simplified(fightingPokemon1,fightingPokemon2)
    fightingPokemon1.dmg = 0
    fightingPokemon2.dmg = 0
    val stat2 = normal(fightingPokemon1,fightingPokemon2)
    fightingPokemon1.dmg = 0
    fightingPokemon2.dmg = 0
    Seq(stat1,stat2)
  }

  def main(args : Array[String]): Unit = {
    val starter = Seq("Éthernatos", "Carapuce", "Bulbizarre")

    val Boss = Map(
      ("Boss1_Pierre" -> Seq(
        new FightingPokemon(get("Racaillou"),10,40,40),
        new FightingPokemon(get("Onix"),20,40,40)
      )),
        ("Boss2_Ondine" -> Seq(
          new FightingPokemon(get("Stari"),20,40,40),
          new FightingPokemon(get("Staross"),20,40,60)
        )),
      ( "Boss3_Major" -> Seq(
        new FightingPokemon(get("Voltorbe"),20,40,20),
        new FightingPokemon(get("Pikachu"),20,0,40),
        new FightingPokemon(get("Raichu"),20,0,100)
      )),
        ("Boss4_Erika" -> Seq(
          new FightingPokemon(get("Empiflor"),30,60,20),
          new FightingPokemon(get("Saquedeneu"),20,20,40),
          new FightingPokemon(get("Rafflesia"),30,0,120)
        )),
      ("Boss5_Koga" -> Seq(
        new FightingPokemon(get("Smogo"),40,40,60),
        new FightingPokemon(get("Smogo"),40,40,60),
        new FightingPokemon(get("Smogogo"),40,40,200),
        new FightingPokemon(get("Grotadmorv"),40,20,60)
      ))
    )

    for (name <- starter) {
      for (niveau <- Seq(1, 10, 20, 30, 40, 50)) {
        println(s"Niveau: ${niveau}")
        for (boss <- Boss) {
          println(s"Boss : ${boss._1} ")
          for (bossesPokemon <- boss._2) println(s"${name},${bossesPokemon.name}," + compare(new FightingPokemon(get(name), niveau, 40, 40), bossesPokemon).flatten.mkString(","))
        }
      }
      println()
    }

  }

}