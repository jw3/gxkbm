package gxkbm

import org.scalatest.{Matchers, WordSpec}

import scala.io.Source._

class SingleBindingSpec extends WordSpec with Matchers {
    val single = "1\t0\t0\t10\t10"

    "bounds to physical conversion" should {
        "produce a physical layout" in {
            val bounds = boundsFromTsv(fromString(single))
            bounds.size shouldBe 1

            val overlay = parseVisualOverlay(bounds)
            overlay.size shouldBe 1
            overlay.head shouldBe VisualKey("1", 0, 0, 10, 10)
        }
    }

    "physical layout to hardware layout" should {
        val visual = parseVisualOverlay(boundsFromTsv(fromString(single)))

        "produce a hardware layout" in {
            val overlay = defineHardwareKeys(visual) { vk => HardwareKey(vk.id, vk.id.toInt + 1) }
            overlay.size shouldBe 1
            val hwKey = overlay.head
            hwKey.to shouldBe visual.head.id.toInt + 1
        }
    }

    "hardware layout to bindings conversion" should {
        val physicalOverlay = defineHardwareKeys(parseVisualOverlay(boundsFromTsv(fromString(single)))) { vk => HardwareKey(vk.id, vk.id.toInt + 1) }

            "produce a virtual layout" in {

            }
    }
}
