package model

class Competence {
  var name : String = null
  private var elementType : String = null
  private var physical : String = null
  var power : Int = -100
  var accuracy : Int = -1
  def completed(): Boolean = {
    name != null && elementType != null && physical!=null
  }

  def elementType(element: String): String = {
    elementType = ElementType.get(element).get
    elementType
  }

  def physical(kind: String): String = {
    physical = kind.replaceAll("Spécial","Special")
    physical
  }

  override def toString: String = {
    if (completed())
      s"$name,${ElementType.get(elementType).get},${physical},$power,$accuracy"
    else
      s"Empty,${ElementType.get(elementType).get},,,"
  }

  def simplifiedToString: String = {
    s"$name,$elementType,${physical},${Math.round(power/10)},${Math.max(Math.round(Math.round((accuracy-55)/5+1)/10*6),0)}"
  }
}

case object Competence {

  def header(): String = {
    "Competence,Type,Category,Power,Accuracy"
  }

}