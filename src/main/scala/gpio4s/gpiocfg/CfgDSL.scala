package gpio4s.gpiocfg

import com.typesafe.config.Config
import gpio4s.gpiocfg.CfgDSL._

import scala.collection.JavaConversions._
import scala.collection.mutable


/**
 * DSL describing the configuration of GPIO pins
 *
 * usage:
 *
 * gpio { pin =>
 * pin number 1 digital input
 * pin nuber 2 analog output
 * }
 */
object CfgDSL {
    /**
     * entrypoint to DSL
     * @param fn Function that configures the builder
     * @return
     */
    def gpio(fn: PinNumberBuilder => Unit): Config = {
        val b = new PinBuilder
        fn(b)
        b.build
    }


    /**
     * describes a gpio numbering scheme
     */
    sealed trait Layout extends DslComponent
    case object bcom extends Layout
    case object pi4j extends Layout


    /**
     * describes the mode of a pin (digital/analog)
     */
    sealed trait Mode extends DslComponent
    object Modes {
        case object digital extends Mode
        case object analog extends Mode
        case object pwm extends Mode
    }


    /**
     * describes the direction (input/output) of a pin
     */
    sealed trait Direction extends DslComponent {
        def isInput = this == input
        def isOutput = this == output
    }
    case object input extends Direction
    case object output extends Direction


    /**
     * describes the internal resistor state (pull up/down or off)
     */
    sealed trait Pull extends DslComponent
    case object off extends Pull
    case object up extends Pull
    case object down extends Pull


    /**
     * describes the digital state (hi or low) of a pin
     */
    sealed trait DigitalState extends DslComponent
    case object high extends DigitalState
    case object low extends DigitalState


    trait PinNumberBuilder {
        def number(num: Int): PinModeBuilder
    }

    trait PinModeBuilder {
        def digital(d: Direction): DigitalInitializer
        def analog(d: Direction): AnalogInitializer
        def pwm(): Unit
    }

    trait Initializer {
        def pull(dir: Pull)
    }

    trait DigitalInitializer extends Initializer {
        def set(v: DigitalState): Initializer
    }

    trait AnalogInitializer extends Initializer {
        def value(v: Double): Initializer
    }

    object ConfigKeys {
        val pins = "pins"
        val layout = "layout"
        val number = "number"
        val mode = "mode"
        val direction = "direction"
        val version = "version"
        val init = "set"
        val pull = "pull"
    }
}

// internal API
sealed trait DslComponent extends Product {
    this: Product =>
    final val uid = productPrefix
}

// default implementation of the dsl
private class PinBuilder(layout: Layout = pi4j) extends PinNumberBuilder with PinModeBuilder with DigitalInitializer with AnalogInitializer {
    import CfgDSL._
    import com.typesafe.config.{ConfigFactory => cf, ConfigValueFactory => cvf}

    val pins = mutable.Set[Int]()
    val modes = mutable.Map[Int, Mode]()
    val directions = mutable.Map[Int, Direction]()
    val values = mutable.Map[Int, AnyRef]()
    val pulls = mutable.Map[Int, Pull]()

    var num: Int = -1

    def value(v: Double): Initializer = {
        values(num) = Double.box(v)
        this
    }

    def pull(dir: Pull): Unit = {
        pulls(num) = dir
    }

    def number(num: Int) = {
        // bounds check valid pin 0-?
        this.num = num
        pins.add(num)
        this
    }
    def digital(d: Direction): DigitalInitializer = {
        modes(num) = Modes.digital
        directions(num) = d
        this
    }
    def analog(d: Direction): AnalogInitializer = {
        modes(num) = Modes.analog
        directions(num) = d
        this
    }
    def pwm(): Unit = {
        modes(num) = Modes.pwm
        directions(num) = output
    }

    def set(v: DigitalState): Initializer = {
        values(num) = v
        this
    }

    def build: Config = {
        num = -1

        val map = mutable.Map[String, AnyRef]()
        map(ConfigKeys.version) = Int.box(1)
        map(ConfigKeys.layout) = layout.uid

        val pinout = for (pin <- pins) yield {
            val pinmap = mutable.Map[String, AnyRef]()
            pinmap(ConfigKeys.number) = Int.box(pin)
            pinmap(ConfigKeys.mode) = modes(pin).uid
            pinmap(ConfigKeys.direction) = directions(pin).uid
            values.get(pin).foreach {
                case v: java.lang.Double => pinmap(ConfigKeys.init) = Double.box(v)
                case v: DigitalState => pinmap(ConfigKeys.init) = v.uid
            }
            pulls.get(pin).foreach { v => pinmap(ConfigKeys.pull) = v.uid }
            cvf.fromMap(pinmap)
        }

        map(ConfigKeys.pins) = cvf.fromIterable(pinout)
        cf.parseMap(map)
    }
}
