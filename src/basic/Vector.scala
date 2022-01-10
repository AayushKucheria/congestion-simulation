package basic

case class Vector(var x: Double, var y: Double) {
  
  def +(other: Vector) = new Vector(this.x + other.x, this.y + other.y)
  
  def -(other: Vector) = new Vector(this.x - other.x, this.y - other.y)
    
  def *(other: Vector) = new Vector(this.x * other.x, this.y * other.y)
   
  def *(other: Double) = new Vector(this.x * other, this.y * other)

  def /(other: Vector) = new Vector(this.x / other.x, this.y / other.y)
    
  def /(other: Int) = new Vector(this.x / other, this.y / other)
    
  def /(other: Double) = new Vector(this.x / other, this.y / other)

  def magnitude = math.sqrt(math.pow(this.x, 2) + math.pow(this.y, 2))
  
  def normalize = this/this.magnitude 
  
  def +=(other: Vector) = {
    this.x += other.x
    this.y += other.y
  }
  
  def -=(other: Vector) = {
    this.x -= other.x
    this.y -= other.y
  }
  
  

}