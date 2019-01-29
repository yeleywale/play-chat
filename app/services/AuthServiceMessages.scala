package services


object AuthServiceMessages {
  type Username = String
  type Password = String
  type SessionId = String

  //Signup Response

  final case class SignupRequest(username: Username, password: Password)

  sealed trait SignUpResponse
  final case class SignupSuccess(username: Username) extends SignUpResponse
  final case class UserExists(username: Username) extends SignUpResponse

// Login Endpoint


  final case class LoginRequest(username: Username, password: Password)

  sealed trait LoginResponse
  final case class LoginSuccess(sessionId: SessionId) extends LoginResponse
  final case class UserNotFound(username: Username) extends LoginResponse
  final case class PasswordIncorrect(username: Username) extends LoginResponse

// Whoami Endpoint

  sealed trait WhoAmiResponse
  final case class Credentials(sessionId: SessionId, username: Username) extends WhoAmiResponse
  final case class SessionNotFound(sessionId: SessionId) extends WhoAmiResponse
  
}