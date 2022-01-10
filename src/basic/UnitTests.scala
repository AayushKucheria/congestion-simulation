package basic

import org.junit.Test
import org.junit.Assert._

class UnitTests {
  
  @Test def stepTest() {
    var a = new Boid(new Vector(5, 10), new Vector(1, 1), new Behaviour())
    var b = new Boid(new Vector(1, 20), new Vector(1, 1), new Behaviour())
    a.step()
    
    // Check if boids interact with door
    assertEquals("Is in Door", false, a.isInDoor()) // Correct
    
    // Check if boids move
    assertNotEquals("Step working", Boid(Vector(5,10), Vector(1,1), Behaviour()), a)
  
  }
  
  
}