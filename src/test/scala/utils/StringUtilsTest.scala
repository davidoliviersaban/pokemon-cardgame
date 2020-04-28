package utils

case object StringUtilsTest {

  def stripAccents() = {
    val stringMap = Map {
      StringUtils.stripAccents(null) -> null
      StringUtils.stripAccents("Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ") -> "This is a funky String";
      StringUtils.stripAccents("Coud&#39;Boue,Sol,Special,20,100") -> "Coud'Boue,Sol,Special,20,100"
    }
    stringMap.foreach( k =>  {
      println (s"Validating that ${k._1} == ${k._2}")
      assert(k._1 == k._2)
    })
  }

  def main(args: Array[String]) = {
    stripAccents()
  }

}
