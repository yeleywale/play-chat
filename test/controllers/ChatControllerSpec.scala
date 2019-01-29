package controllers

import org.scalatest._
import org.scalatestplus.play._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws._
import play.api.test._
import javax.inject.Inject

import services.AuthService
import services.ChatService
import services.AuthServiceMessages._
import services.ChatServiceMessages._

// usually injected through @Inject((implicit mat: Materializer)

class ChatControllerSpec @Inject()(implicit wsClient: WSClient)
    extends PlaySpec
    with ControllerSpecHelpers
    with BeforeAndAfter {

  before {
    ChatService.clear()
  }

  def cookies: String = {
    val LoginSuccess(sessionId) =
      AuthService.login(LoginRequest("alice", "password1"))
    Cookies.encodeCookieHeader(Seq(Cookie("ChatAuth", sessionId)))
  }

  "chat page" must {
    "contain messages" in {
      implicit val port = 9000
      ChatService.chat("author1", "message1")
      ChatService.chat("author2", "message2")

      val response = await {
        wsCall(routes.ChatController.index())
          .withHttpHeaders("Cookie" -> cookies)
          .get()
      }

      response.body must include("author1")
      response.body must include("author1")
      response.body must include("message1")
      response.body must include("message2")
    }
    "be inaccessible to unauthorized user" in {
      val response = await(wsCall(routes.ChatController.index).get())
      response.body must include("""not logged in """)
    }
  }

  "chat form " must {
    "post a message " in {
      await {
        wsCall(routes.ChatController.submitMessage(text = "Hello World!")).
        withHttpHeaders("Cookier" -> cookies).
        get
      }

      val response = await {
        wsCall(routes.ChatController.index).
        withHttpHeaders("Cookie" -> cookies).
        get()
      }

      response.body must include("alice")
      response.body must include("Hello World!")
      response.body must include("author1")
      response.body must include("message1")
    }

    "be inaccessible to unauthorized users" in {
      val response = await {
        wsCall(routes.ChatController.submitMessage(text = "Hello World!")).
        get
      }

      response.body must include("""Not logged in """)
    }
  }

}
