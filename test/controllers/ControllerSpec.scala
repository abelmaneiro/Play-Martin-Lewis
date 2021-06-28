package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout}
import play.api.test.{FakeRequest, Helpers}

class ControllerSpec extends PlaySpec {
  "The Application controller" must  {

    "give back expected Index page" in {
      val controller = new Application(Helpers.stubControllerComponents()) // () to use  = defaults parameters for stubControllerComponents
      val result = controller.index(FakeRequest())    // index() calls the .apply of the Action returned by index and
                                                      // FakeRequest() to call .apply method of FakeRequest
      // val indexAction = controller.index           // Equivalent to above
      // val result = indexAction.apply(FakeRequest.apply())
      val bodyText = contentAsString(result) // import play.api.test.Helpers.defaultAwaitTimeout needed for implicit timeout
      bodyText must include ("<h1>It works!</h1>")
      bodyText must include ("This is a simple example of a web form")
      bodyText must include ("Nothing special at all")
    }

    val controller = new Application(Helpers.stubControllerComponents()) // moving up a level so val controller can be reused

    "give back a product information" in {
      val result = controller.product("test", 42).apply(FakeRequest.apply())
      contentAsString(result) must be ("Product type is: test, product number is: 42")
    }

    "give back a random number" in {
      val result = controller.randomNumber(FakeRequest())
      contentAsString(result).toInt must (be >= 0 and be <= 100)
    }
  }
}
