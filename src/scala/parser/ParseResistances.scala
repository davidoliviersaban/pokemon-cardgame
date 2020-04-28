package parser

import model.{ElementType, Resistance}

import scala.io.Source

case class TypeVsType(defense: Set[String], attack: String) {
  override def hashCode(): Int = defense.hashCode + attack.hashCode * 31

  override def toString(): String = s"${defense.toString} vs ${attack}"
}

case class ParseResistances() {
  var resistanceList = collection.mutable.Map[TypeVsType, Double]()

  def apply(): ParseResistances = {
    resistanceList = collection.mutable.Map[TypeVsType, Double]()

    val input = Source.fromFile("resistances.csv").getLines()
    // Drop first line and then drop the first column
    input.drop(1).takeWhile(line => line.split(",")(1).matches("""\d+\.*\d*""")).foreach(
      line => {
        val values: Seq[String] = line.split(",").toSeq
        values.drop(1).zipWithIndex.map {
          case (data, id) => resistanceList += (TypeVsType(Set(values(0), values(0)), ElementType.values(id)) -> data.toDouble)
        }
      }
    )
    ElementType.values.foreach(
      el1 => ElementType.values.foreach(
        el2 => ElementType.values.foreach(
          attack => {
            if (el1 != el2) resistanceList += (TypeVsType(Set(el1, el2), attack) -> get(el1, attack) * get(el2, attack))
          }
        )
      )
    )
    this
  }

  def get(defense1: String, defense2: String, attack: String): Double = {
    resistanceList.get(TypeVsType(Set(defense1, defense2), attack)).get
  }

  def get(defense: String, attack: String): Double = {
    get(defense, defense, attack)
  }

  def toSeq: Seq[Resistance] = {
    var sequence: collection.mutable.Seq[Resistance] = collection.mutable.Seq[Resistance]()
    ElementType.values.zipWithIndex.foreach {
      case (defense1, id) =>
        ElementType.values.drop(id).foreach {
          defense2 => {
            sequence :+= Resistance(defense1, defense2, ElementType.values.map(get(defense1, defense2, _)))
          }
        }
    }
    sequence
  }

}
