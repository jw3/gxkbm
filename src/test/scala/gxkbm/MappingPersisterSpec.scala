package gxkbm

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class MappingPersisterSpec extends WordSpec with Matchers {

    "mapping to config process" should {
        "produce a config" in {
            val bindings: SoftwareLayout = Seq.tabulate(5) { i => KeyBinding(i.toString, Option(i.toString)) }
            val config = mappingConfig(bindings)
            val expected = ConfigFactory.parseString("0=0,1=1,2=2,3=3,4=4")
            config shouldBe expected
        }
    }
}
