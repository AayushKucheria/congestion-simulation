package basic

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import javax.imageio.ImageIO
import java.io.File
import java.awt.geom.Rectangle2D

object Door extends Constants{
  
  // Values
  var pos = Vector(400, 40)
  var size = 20
  def doorRadius = size/2

  // Draw Door
  def draw(g: Graphics2D) {
   val circle: Ellipse2D.Double = new Ellipse2D.Double(pos.x.toInt, pos.y.toInt, size, size)
   // If using Door image -->
   // val doorImg = new Rectangle2D.Double(pos.x.toInt, pos.y.toInt, doorRadius, size)
    val image = ImageIO.read(new File("./Image/doorCircle.png"))
    g.setColor(doorColor)
    // If using Rectangular Door --> 
    // g.drawImage(image, null, pos.x.toInt - doorRadius, pos.y.toInt - doorRadius)
    g.fill(circle)
  }
}

case class Boid(var pos:Vector, var mInitial_velocity:Vector, var behaviour: Behaviour) extends Constants {
  
  // Values
  var currentVelocity = mInitial_velocity
  var reached = false
  
  // Draw Boid
  def draw(g: Graphics2D) {
    val circle: Ellipse2D.Double = new Ellipse2D.Double(pos.x.toInt, pos.y.toInt, boidSize, boidSize)
    
    g.setColor(boidColor) 
    g.fill(circle)
    
    
  }

  def isInDoor(): Boolean = {
    if(this.pos.x >= (Door.pos.x - Door.doorRadius) && this.pos.x <= (Door.pos.x + Door.doorRadius) && this.pos.y >= (Door.pos.y - Door.doorRadius) && this.pos.y <= (Door.pos.y + Door.doorRadius)) {
      reached = true
    }
    reached
  }

  
  def limitVelocity(v: Vector):Vector = {
    if(v.magnitude > maxVelocity)
      return v/(v.magnitude * maxVelocity)
    else
      return v
  }
  
  // Calculate next step
  def step() = {
    if(!isInDoor()) {

      val separationV = this.behaviour.separation(this.pos)
      val seekV = this.behaviour.seek(this)
      val sumV = separationV + seekV
//      println("Separation => " + separationV + ", Seek => " + seekV + ", SumV => " + sumV)
      this.currentVelocity = this.currentVelocity + sumV/lowerFactors
            
      
      this.currentVelocity = this.limitVelocity(this.currentVelocity)
     
      this.pos += bound(this.pos, this.currentVelocity)
      
      
    }
    
  }
  
  // Keep boid within the room
  def bound(where: Vector, increase: Vector): Vector = {
    val initialFuture = where + increase
    if(initialFuture.x < 30 || initialFuture.x > 1160 || initialFuture.y < 30 || initialFuture.y > 650)
      return increase* (-1)
    else
      return increase
  }
  
 
  
}