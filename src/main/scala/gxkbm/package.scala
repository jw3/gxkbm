import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory._

import scala.collection.JavaConversions._
import scala.io.Source

package object gxkbm {
    type S = String
    type I = Int

    type KeyBounds = Seq[(S, I, I, I, I)]
    type PhysicalLayout = Seq[VisualKey]
    type HardwareLayout = Seq[HardwareKey]
    type SoftwareLayout = Seq[KeyBinding]


    // Bounds -> Visual Id
    case class VisualKey(id: String, x: Double, y: Double, w: Double, h: Double) {
        def scale(pct: Double) = new VisualKey(id, x * pct, y * pct, w * pct, h * pct)
    }

    // Visual -> KeyCode
    case class HardwareKey(id: String, to: Int)

    // Visual -> Char
    case class KeyBinding(id: String, var binding: Option[String] = None)


    // bounds -> Visual
    def parseVisualOverlay(bounds: KeyBounds): PhysicalLayout = bounds map { b => VisualKey(b._1, b._2, b._3, b._4, b._5) }

    // Visual -> Hardware
    def defineHardwareKeys(keys: Seq[VisualKey])(fn: VisualKey => HardwareKey): HardwareLayout = keys map fn

    // Hardware -> Bindings
    def bindKeys(keys: Seq[HardwareKey])(fn: HardwareKey => KeyBinding): SoftwareLayout = keys map fn

    // Bindings -> Config
    def mappingConfig(mapped: SoftwareLayout): Config =
        mapped.filter {_.binding.isDefined}.foldLeft(empty) { (c, b) => parseMap(Map(b.id -> b.binding.get)).withFallback(c) }

    // parse tab separated values; id, x, y, w, h
    def boundsFromTsv(source: Source): KeyBounds = {
        source.getLines().filter {!_.startsWith("#")}.map {_.split("\t").toSeq}.collect {
            case t @ Seq(id, x, y, w, h) => (id, x.toInt, y.toInt, w.toInt, h.toInt)
        }.toSeq
    }

    // modifier states
    object State {
        val Shift = 0x01
        val Lock = 0x02
        val Ctrl = 0x04
        val Mod1 = 0x08
        val Mod2 = 0x10
        val Mod3 = 0x20
        val Mod4 = 0x40
        val Mod5 = 0x80
    }

    implicit def stringToOpt(in: String): Option[String] = Option(in)

    trait Overlay {
        def get(k: String): Option[String]
    }
}
