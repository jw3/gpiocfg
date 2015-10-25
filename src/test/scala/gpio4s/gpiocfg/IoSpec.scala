package gpio4s.gpiocfg

import gpio4s.gpiocfg.CfgDSL.Modes.digital
import gpio4s.gpiocfg.CfgDSL.{down, high, output}
import org.scalatest.{Matchers, WordSpecLike}
import CfgDSL._
import CfgIO._


class IoSpec extends WordSpecLike with Matchers {
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
