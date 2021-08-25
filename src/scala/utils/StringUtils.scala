package utils

import java.net.URLDecoder
import java.text.Normalizer

case object StringUtils {

  def stripAccents(string: String): String = {
    var output : String = null
    if (string != null) {
      val str = Normalizer.normalize(string,Normalizer.Form.NFD)
      val exp = "\\p{InCombiningDiacriticalMarks}+".r
      output = decode(exp.replaceAllIn(str,""))
    }
    output
  }

  private def decode(string : String) : String = {
    string.replaceAll("&#39;","'")
  }

  private def convertRemainingAccentCharacters(decomposed: StringBuilder) = {
      for (i <- 0 to decomposed.length()) {
          if (decomposed.charAt(i) == '\u0141') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'L');
            } else if (decomposed.charAt(i) == '\u0142') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'l');
            }
        }
    }

}
