package basic

import scala.swing.Dimension
import java.awt.Color

trait Constants {
  
  // SimulationApp and Behaviour
    var speedInMillis = 15
    
    // Full Mainframe
    var minSize = new Dimension(400, 400)
    var maxSize = new Dimension(2400, 2400)
    var prefSize = new Dimension(1200, 1200)
    
    // Arena Size
    var roomBG = Color.BLACK
    var roomStart = new Dimension(0, 22)
    var roomEnd = new Dimension(1200, 650)
    
    var wallColor = Color.DARK_GRAY
    var bottomWallStart = new Dimension(0, 650)
    var bottomWallEnd = new Dimension(1200, 650)
    var leftWallStart = new Dimension(0,0)
    var leftWallEnd = new Dimension(30, 650)
    var rightWallStart = new Dimension(1160, 0)
    var rightWallEnd = new Dimension(1200, 650)
    var topWallStart = new Dimension(0,0)
    var topWallEnd = new Dimension(1200, 30)
    
    var boidColor = Color.WHITE
    
    var initialVelocity = new Vector(10, 10)
    
    var modifyPanelSize = new Dimension(1200, 350)
    
    
    
    // Boid
    var boidSize = 10
    var lowerFactors = 20.0
    var maxVelocity = 2
    
    // Door
    
    var doorColor = Color.BLUE
}