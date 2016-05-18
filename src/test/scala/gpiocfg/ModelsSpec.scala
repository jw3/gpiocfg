package gpiocfg

import gpiocfg.GpioCfg.Modes.digital
import gpiocfg.GpioCfg.{down, high, output}
import org.scalatest.{Matchers, WordSpecLike}
import GpioCfg._
import GpioCfgModels._


class ModelsSpec extends WordSpecLike with Matchers {
    "bean access" should {
        "basic" in {
            val conf = gpio {_ number 0 digital output}
            val pin = conf.pins.head
            pin.num shouldBe 0
            pin.mode shouldBe digital
            pin.dir shouldBe output
            pin.state shouldBe None
            pin.pull shouldBe None
        }

        "with state" in {
            val conf = gpio {_ number 0 digital output set high}
            val pin = conf.pins.head
            pin.num shouldBe 0
            pin.mode shouldBe digital
            pin.dir shouldBe output
            pin.state shouldBe Some(high)
            pin.pull shouldBe None
        }

        "with pull" in {
            val conf = gpio {_ number 0 digital output set high pull down}
            val pin = conf.pins.head
            pin.num shouldBe 0
            pin.mode shouldBe digital
            pin.dir shouldBe output
            pin.state shouldBe Some(high)
            pin.pull shouldBe Some(down)
        }
    }
}
