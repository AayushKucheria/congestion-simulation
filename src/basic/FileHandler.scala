package basic

import scala.io._
import java.io._

trait FileHandler {
  
  def load(source: BufferedSource) = {
    
    
    val allLines = source.getLines()
    
    var currentLine = allLines.next().toLowerCase()
    
    def getNextLine() = {
      try {
        do {
          currentLine = (allLines.next()).toLowerCase()
        } while(currentLine.isEmpty())
      } catch {
        case _: Throwable => currentLine = "#/#"
      }
    }
    
    if(!currentLine.equals("congestion simulation"))
      currentLine = "#/#"
    
    try {
      getNextLine()
      while(currentLine != null && !currentLine.equals("#/#")) {
        
        if(currentLine.startsWith("#")) {
          var header = ""
          println("# Detected")
          if(currentLine.contains("factors"))
            header = "factors"
          else if(currentLine.contains("door"))
            header = "door"
          else if(currentLine.contains("boid"))
            header = "boid"
        
        
          getNextLine()
          header match {
            case "factors" => {
              
              while(!currentLine.startsWith("#") && !currentLine.equals("#/#")) {
                println("Factors Loop on")
                
                if(currentLine.contains("separation"))
                  SimulationApp.separationFactor = currentLine.dropWhile(a => a != '=').trim().tail.toInt
                else if(currentLine.contains("seek"))
                  SimulationApp.seekFactor = currentLine.dropWhile(a => a != '=').trim().tail.toInt
                else if(currentLine.contains("safearea")) {
                  println("Safe Area Reached")
                  SimulationApp.safeArea = currentLine.dropWhile(a => a != '=').trim().tail.toInt
                }
                println("Next line = " + currentLine)
                getNextLine()
                println("Next line = " + currentLine)
              }
            }
            
            case "door" => {
              while(!currentLine.startsWith("#") && !currentLine.equals("#/#")) {
                println("Door loop on")
                
                if(currentLine.contains("doorpos")) {
                  val strPos = currentLine.dropWhile(a => a != '=').trim().tail.split(',')
                  Door.pos = new Vector(strPos(0).toDouble, strPos(1).toDouble)
                }
                else if(currentLine.contains("size")) {
                  Door.size = currentLine.dropWhile(a => a != '=').trim().tail.toInt
                }
                getNextLine()
              }
            }
            
            case "boid" => {
              SimulationApp.boidList.clear()
              if(currentLine.equals("poslist")) {
                getNextLine()
                
                while(!currentLine.startsWith("#") && !currentLine.equals("#/#")) {
                  
                  var strPos = (currentLine.trim().split(','))
                  
                  val pos = new Vector(strPos(0).toDouble, strPos(1).toDouble)
                  SimulationApp.boidList += new Boid(pos, SimulationApp.initialVelocity, new Behaviour())
                  getNextLine()
                }
              }
            }
          }
        }
      }
    }
    catch {
      case e: Exception => print(e)
    }
    
  }
  
  def save(writer: PrintWriter) = {
    
    writer.write("congestion simulation\n")
    writer.write("#factors\n")
    writer.write("separation=" + SimulationApp.separationFactor + "\n")
    writer.write("seek=" + SimulationApp.seekFactor + "\n")
    writer.write("safearea=" + SimulationApp.safeArea + "\n")
    
    writer.write("#door\n")
    writer.write("doorpos=" + Door.pos.x + "," + Door.pos.y + "\n")
    writer.write("size=" + Door.size + "\n")
    
    writer.write("#boid\n")
    writer.write("poslist\n")
    for(i <- SimulationApp.boidList) {
      writer.write("" + i.pos.x + "," + i.pos.y + "\n")
    }
    
    writer.write("#/#")
    

  }
}