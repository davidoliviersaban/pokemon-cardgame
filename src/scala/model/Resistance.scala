package model

import scala.io.Source

case class Resistance(defense1: String, defense2: String, values: Seq[Double]) {
  override def toString: String = {
    s"$defense1,$defense2,${values.mkString(",")}"
  }

}

case object Resistance {

  def parseResistances(filename: String = "generatedResources/pokeResistances.csv") = {
    var resistances = collection.mutable.Seq[Resistance]()
    Source.fromFile(filename).getLines().drop(1).foreach { line => {
      val values = line.split(",").toIterator
      resistances :+= Resistance(values.next(), values.next(), values.toSeq.map(_.toDouble))
    }
    }
    resistances
  }
}