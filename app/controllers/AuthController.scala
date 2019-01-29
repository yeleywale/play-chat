package controllers

import play.api._
import play.api.mvc._
import javax.inject.Inject

class AuthController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
    with ControllerHelpers {

      import services.AuthService
      import services.AuthServiceMessages._

      def login(username: Username, password: Password) = Action { request =>
        AuthService.login(LoginRequest(username, password)) match {
          case res: LoginSuccess => 
            Ok("Logged In").withSessionCookie(res.sessionId)

          case res: UserNotFound => 
            BadRequest("User Not found or password incorrect")

          case res: PasswordIncorrect => 
            BadRequest("User Not found or password incorrect")
        }
      }
    }
