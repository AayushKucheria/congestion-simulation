// package CongestionSimulation

package basic

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.{ActionListener, ActionEvent}
import java.awt.RenderingHints
import scala.collection.mutable.ArrayBuffer
import javax.swing.Timer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import BorderPanel.Position._
import java.awt.geom.Rectangle2D




object SimulationApp extends SimpleSwingApplication with Constants with FileHandler{

	val boidList = new ArrayBuffer[Boid]()

			// Values
			var separationFactor = 50
			var safeArea = 20
			var seekFactor = 100
			var doorChange = false
			var isPaused = false

			def top = new MainFrame {

		    title = "Congestion Simulation"
				resizable = true
				minimumSize = minSize
				preferredSize = prefSize
				maximumSize = maxSize
				// * * * * * * MENU * * * * * * * * * * 
				// Menu Helper Functions

				def openFile() {
			    val chooser = new FileChooser
					if(chooser.showOpenDialog(null) == FileChooser.Result.Approve) {
						val source = scala.io.Source.fromFile(chooser.selectedFile)
								load(source)
								source.close()

								subPanel.introvertSlider.value_=(safeArea)
								subPanel.seekSlider.value_=(seekFactor)
								subPanel.separationSlider.value_=(separationFactor)
					}

		}
		
    		def saveFile() {
    			val chooser = new FileChooser
					if(chooser.showSaveDialog(null) == FileChooser.Result.Approve) {
						val pw = new java.io.PrintWriter(chooser.selectedFile)
				    save(pw)
					  pw.close()
    					}
    		}


		// Menu Bar
		menuBar = new MenuBar {

			preferredSize = new Dimension(prefSize.width, prefSize.height/40)
					contents += new Menu("File") {
				contents += new MenuItem(Action("Open") {
					openFile()
				})
						contents += new MenuItem(Action("Save") {
							if(!isPaused)
							  pause.doClick()
							saveFile()
						})
						contents += new Separator
						contents += new MenuItem(Action("Exit") {
							sys.exit(0)
						})
			}
		}
		// * * * * * *  * * * * * * * * * * 

		contents = vertical
				centerOnScreen()

	}



	// Container of all my views
	val vertical = new BoxPanel(Orientation.Vertical)

	
	// Container of Buttons/Sliders
	val bottom = new BoxPanel(Orientation.Horizontal)
	
	
	// * * * * * * ARENA  * * * * * * * * * *     
	// Main Arena

	val arena = new Panel with ActionListener{
		preferredSize = new Dimension(1200, 800)

			override def paintComponent(g: Graphics2D) = {
		    // Arena Setup
			  g.setColor(roomBG) 
				g.fillRect(0, 22, 1200, 650) // Fills the rectangle with the latest color, here White
				g.setColor(wallColor) // Change latest color to Black
				g.fillRect(0, 650, 1200, 650) // Bottom Wall
				g.fillRect(0, 0, 30, 650) // Left Wall
				g.fillRect(0, 0, 1200, 30) // Top Wall
				g.fillRect(1160, 0, 1200, 650) // Right Wall
				g.setColor(boidColor)

				// For smoother boid movement
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

				Door.draw(g)
				boidList.foreach(a => if(!a.isInDoor()){a.draw(g)})
		}

				
		// If mouse clicked, add a boid at that location
		listenTo(mouse.clicks)
		reactions += {
		case e:MouseClicked => {
			if(doorChange) {
				Door.pos = new Vector(e.point.getX, e.point.getY)
						doorChange = false
			}
			else
				boidList.+=(new Boid(new Vector(e.point.getX(), e.point.getY()), initialVelocity, new Behaviour()))
				this.repaint()
		}
		}

		def actionPerformed(e: ActionEvent) {
			if(!isPaused) {
				boidList.foreach(_.step())
				repaint()
			}
		}
	}

	val timer = new Timer(this.speedInMillis, arena)
			timer.start()
			// * * * * * *  * * * * * * * * * * 

	// Bottom Control Panel Width and Height
	def infoW = 1200
	def infoH = 350
	
  // Sliders 
	val subPanel = new BoxPanel(Orientation.Vertical) {

		preferredSize = new Dimension(infoW, infoH)


		// Separation
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Separation")
		}

		val separationSlider = new Slider {
			min = 0
			max = 200
			value = separationFactor
			snapToTicks = true
		}
		contents += separationSlider

		// SafeArea (Introversion)
		contents += new BoxPanel(Orientation.Horizontal) {
		  contents += new Label("Introversion")
		}
		val introvertSlider = new Slider {
			min = 0
			max = 200
			value = safeArea
			snapToTicks = true
		}
		contents += introvertSlider

		// Seeking
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Seeking")
		}
		val seekSlider = new Slider {
			min = 0
			max = 200
			value = seekFactor
			snapToTicks = true
			paintTicks = true
		}
		contents += seekSlider

		// Door Size
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Door Size")
		}

		val doorSizeRadios = new BoxPanel(Orientation.Horizontal) {
			val group = new ButtonGroup {
				buttons += new RadioButton("Small") {
					name = "Small"
				}
				buttons += new RadioButton("Original") {
					name = "Original"
				}
				buttons += new RadioButton("Large") {
					name = "Large"
				}
			}
			group.select(group.buttons.head)
			contents ++= group.buttons
		}
		contents += doorSizeRadios

		listenTo(separationSlider)
		listenTo(introvertSlider)
		listenTo(seekSlider)
		doorSizeRadios.contents.foreach(a => listenTo(a))

		this.reactions += { // The symbols enclosing the sliders down here are crazy. Wasted a whole day thinking they were '. They're not.
			case ValueChanged(`separationSlider`) => 
			separationFactor = separationSlider.value
			case ValueChanged(`introvertSlider`) => 
			safeArea = introvertSlider.value
			case ValueChanged(`seekSlider`) => {
				seekFactor = seekSlider.value
			}
			case ButtonClicked(button) => {

				button.name match {
				case "Small" => Door.size = 20 
				case "Original" => Door.size = 40
				case "Large" => Door.size = 80
				}
				this.repaint()
			}
		} 
	}

	// All Buttons
	val buttons = new BoxPanel(Orientation.Vertical)


	val pause = new Button {
		text = "Pause"
		enabled=true
	}
	val changeDoorPos = new Button {
		text = "Change Door Position"
		enabled=true
	}

	listenTo(pause)
	listenTo(changeDoorPos)
	reactions += {
  	case ButtonClicked(component) if component == pause => {
  		if(pause.text.equals("Pause")) {
  			pause.text = "Play"
				this.isPaused = true
  		}
  		else {
  			pause.text = "Pause"
  		  this.isPaused = false
  		}
  	}
  	case ButtonClicked(component) if component == changeDoorPos => {
  		if(changeDoorPos.text.equals("Change Door Position")) {
  			doorChange = true
  		}
  	}
	}

	buttons.contents += pause
	buttons.contents += changeDoorPos

	bottom.contents += buttons
	bottom.contents += subPanel
	vertical.contents += arena
	vertical.contents += bottom

}


