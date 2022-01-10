package basic

case class Behaviour() extends Constants {


  val boidList = SimulationApp.boidList
  var mass = 5
  def separationFactor = SimulationApp.separationFactor
  def seekFactor = SimulationApp.seekFactor
  def safeArea = SimulationApp.safeArea

  
  // Calculate Separation Vector
  def separation(currentPos: Vector): Vector = {

    var totalForce = Vector(1.0, 1.0)

    if(boidList.nonEmpty) {
      val filter = boidList.filter(a => inRange(currentPos, a.pos))
      filter.foreach((boid) => {
        val deltaPos = currentPos - boid.pos
        val distance = deltaPos.magnitude
        if(distance != 0) {
          totalForce += (deltaPos.normalize)/mass
        }
      })
    }
      totalForce*separationFactor/100
  }

  // Calculate Seek Vector
  def seek(boid: Boid): Vector = {

    val desiredVelocity = (Door.pos - boid.pos).normalize * maxVelocity
    return (desiredVelocity*seekFactor/50)

  }


  // Check if a position is within safeZone
   def inRange(thisPos: Vector, otherPos: Vector): Boolean = {
    val delta = thisPos - otherPos
    if(delta.magnitude <= safeArea && delta.magnitude != 0)
      return true
    else return false
  }



}
