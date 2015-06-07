package gxkbm

import scala.io.Source._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{Button, TableColumn, TableView}
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color

object App extends JFXApp {
    val keys = parseVisualOverlay(boundsFromTsv(fromString("/ergopro.pos"))).map {_.scale(.6)}
    val buttonIds = collection.mutable.Map[String, Button]()

    stage = new JFXApp.PrimaryStage {
        title.value = "gxkbm"
        width = 1430
        height = 600

        scene = new Scene {
            fill = Color.LightGreen

            val bindings = ObservableBuffer[KeyBinding](keys.map { k => KeyBinding(k.id) })
            val buttons = keys.map { k =>
                val button = new Button(k.id) {
                    prefWidth = k.w
                    prefHeight = k.h
                    layoutX = k.x
                    layoutY = k.y

                    onAction = () => {}
                }
                buttonIds(k.id) = button
                button
            }

            val vbox = new VBox() {
                children = new TableView[KeyBinding](bindings) {
                    columns ++= List(
                        new TableColumn[KeyBinding, String]() {
                            text = "ID"
                            cellValueFactory = { v => StringProperty(v.value.id) }
                        },
                        new TableColumn[KeyBinding, String]() {
                            text = "Binding"
                            cellValueFactory = { v => StringProperty(v.value.binding.getOrElse("Unmapped")) }
                        }
                    )
                }
            }


            content = new BorderPane() {
                top = new HBox() {
//                    children = List(new Button("record") {
//                        onAction = () => {
//                            buttonIds.values.foreach {_.disable = true}
//                            var prev: Option[Button] = None
//                            defineHardwareKeys(keys, (k) => {
//                                prev.foreach {_.disable = true}
//                                val b = buttonIds(k.id)
//                                b.disable = false
//                                b.requestFocus()
//                                b.filterEvent(KeyEvent.KeyPressed) {
//                                    (me: KeyEvent) =>
//                                        me.consume()
//                                }
//                                prev = Option(b)
//                                null
//                            })
//                        }
//                    })
                }
                //right = vbox
                center = new Pane() {
                    children = buttons
                }
            }
        }
    }
}
