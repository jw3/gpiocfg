package gpio4s.gpiocfg

import gpio4s.gpiocfg.CfgDSL._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._

class ConfigsSpec extends FlatSpec with Matchers {
    import ConfigKeys._

    "set digital input" should "work as expected" in {
        val cfg = gpio {_ number 1 digital input}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe input.uid
    }
    "set digital output" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
    }
    "set multiple digitals" should "work as expected" in {
        val cfg = gpio { set =>
            set number 1 digital output
            set number 2 digital input
        }

        val cl = cfg.getConfigList(pins)
        cl.size() shouldBe 2
        val iter = cl.iterator

        {
            val c = iter.next()
            c.getInt(number) shouldBe 2
            c.getString(mode) shouldBe Modes.digital.uid
            c.getString(direction) shouldBe input.uid
        }
        {
            val c = iter.next()
            c.getInt(number) shouldBe 1
            c.getString(mode) shouldBe Modes.digital.uid
            c.getString(direction) shouldBe output.uid
        }
    }
    "set digital output with pull up" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output pull up}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(pull) shouldBe up.uid
    }

    "set digital output with pull down" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output pull down}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(pull) shouldBe down.uid
    }

    "set digital output with initial state high" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output set high}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(init) shouldBe high.uid
    }

    "set digital output with initial state low" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output set low}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(init) shouldBe low.uid
    }

    "set digital output with initial state low pulled up" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output set low pull up}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(init) shouldBe low.uid
        cfg.head.getString(pull) shouldBe up.uid
    }

    "set digital output with initial state low pulled down" should "work as expected" in {
        val cfg = gpio {_ number 1 digital output set low pull down}.getConfigList(pins)
        cfg.head.getInt(number) shouldBe 1
        cfg.head.getString(mode) shouldBe Modes.digital.uid
        cfg.head.getString(direction) shouldBe output.uid
        cfg.head.getString(init) shouldBe low.uid
        cfg.head.getString(pull) shouldBe down.uid
    }
}
