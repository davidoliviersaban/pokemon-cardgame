package parser

import model.{FightingPokemon, Pokemon}

case object PokepediaTest {
  val bulbizarre = new Pokemon(null, null, "Bulbizarre")
  val morpeko = new Pokemon(null, null, "Morpeko")
  val riolu = new Pokemon(null, null, "Riolu")
  val darumacho = new Pokemon(null, null, "Darumacho")
  val darumacho_galar = new Pokemon(null, null, "Darumacho_de_Galar")
  val darumacho_galar_transe = new Pokemon(null, null, "Darumacho_de_Galar_Transe")
  val motisma = new Pokemon(null, null, "Motisma")
  val motisma_tonte = new Pokemon(null, null, "Motisma_Tonte")
  val zacian = new Pokemon(null, null, "Zacian")
  val zamazenta = new Pokemon(null, null, "Zamazenta")
  val ethernatos = new Pokemon(null, null, "Éthernatos")


  def testPokemonParser = {
    var parsed : Pokemon = null

    parsed = Pokepedia.parsePokemonStats(bulbizarre)
    assert(parsed.name == bulbizarre.name)
    assert(parsed.type1 == "Plante")
    assert(parsed.type2 == "Poison")
    assert(parsed.id() == "001")
    println(new FightingPokemon(parsed,0,0,0))

    parsed = Pokepedia.parsePokemonStats(morpeko)
    assert(parsed.name == morpeko.name)
    assert(parsed.type1 == "Electrik")
    assert(parsed.type2 == "Tenebres")
    assert(parsed.id() == "877")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(riolu)
    println (parsed)
    assert(parsed.name == riolu.name)
    assert(parsed.type1 == "Combat")
    assert(parsed.type2 == "Combat")
    assert(parsed.id() == "447")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(darumacho)
    assert(parsed.name == darumacho.name)
    assert(parsed.type1 == "Feu")
    assert(parsed.type2 == "Feu")
    assert(parsed.id() == "555")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(darumacho_galar)
    assert(parsed.name == darumacho_galar.name)
    assert(parsed.type1 == "Glace")
    assert(parsed.type2 == "Glace")
    assert(parsed.id() == "555")
    println(new FightingPokemon(parsed,0,0,0))

    parsed = Pokepedia.parsePokemonStats(darumacho_galar_transe)
    assert(parsed.name == darumacho_galar_transe.name)
    assert(parsed.type1 == "Feu")
    assert(parsed.type2 == "Glace")
    assert(parsed.id() == "555")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(motisma)
    assert(parsed.name == motisma.name)
    assert(parsed.type1 == "Electrik")
    assert(parsed.type2 == "Spectre")
    assert(parsed.id() == "479")
    println(new FightingPokemon(parsed,0,0,0))

    parsed = Pokepedia.parsePokemonStats(motisma_tonte)
    assert(parsed.name == motisma_tonte.name)
    assert(parsed.type1 == "Electrik")
    assert(parsed.type2 == "Plante")
    assert(parsed.id() == "479")
    println(new FightingPokemon(parsed,0,0,0))

    parsed = Pokepedia.parsePokemonStats(zacian)
    assert(parsed.name == zacian.name)
    assert(parsed.type1 == "Acier")
    assert(parsed.type2 == "Fee")
    assert(parsed.id() == "888")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(zamazenta)
    assert(parsed.name == zamazenta.name)
    assert(parsed.type1 == "Acier")
    assert(parsed.type2 == "Combat")
    assert(parsed.id() == "889")
    println(new FightingPokemon(parsed,0,0,0))


    parsed = Pokepedia.parsePokemonStats(ethernatos)
    assert(parsed.name == ethernatos.name)
    assert(parsed.type1 == "Dragon")
    assert(parsed.type2 == "Poison")
    assert(parsed.id() == "890")
    println(new FightingPokemon(parsed,0,0,0))
  }


  def main(args: Array[String]) = {
    testPokemonParser
  }

}
