package model


class FightingPokemon(pokemon: Pokemon, niveau : Int, puissance: Int, puissanceSpe: Int) {
  var dmg = 0
  def PV() = {pokemon.stats(Stats.PV)/2+niveau/2 - dmg}
  def Vitesse() = {pokemon.stats(Stats.Vitesse)/4+niveau/4}
  def Attaque() = {pokemon.stats(Stats.Attaque)/10+niveau/10}
  def Defense() = {pokemon.stats(Stats.Defense)/10+niveau/10}
  def AttSpe() = {pokemon.stats(Stats.AttSpe)/10+niveau/10}
  def DefSpe() = {pokemon.stats(Stats.DefSpe)/10+niveau/10}
  def puissance(): Int = {puissance}
  def puissanceSpe(): Int = {puissanceSpe}
  def type1: String = {ElementType.get(pokemon.type1).get}
  def type2: String = {ElementType.get(pokemon.type2).get}
  def name() : String = {pokemon.name()}
  def niveau(): Int = {niveau}
  def pokemon() : Pokemon = { pokemon}
  def hit(value: Double) = { dmg += Math.round(value).toInt}
  def hit(values: Seq[Double]) = { dmg += Math.round(values.max).toInt}
  def capture() = {
    Math.round(pokemon.capture/50)
  }

  override def toString: String = {
    println (name)
    s"${name},$type1,$type2,$PV,$Vitesse,$Attaque,$Defense,$AttSpe,$DefSpe,$capture"
  }
 def competencesToString: String = {
   val comp = Seq(new Competence, new Competence)
   comp(0).elementType(type1)
   comp(1).elementType(type2)
   s"${pokemon.competences.take(2).map{_.simplifiedToString}.mkString(",")},${comp.mkString(",")}"
 }
}
