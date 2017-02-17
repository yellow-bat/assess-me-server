package v1.session

import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, BodyParsers, Controller}

import scala.util.Random

case class Credentials(username: String, password: String)

/**
  * Controller for Session manipulation - login and check auth state.
  */
class SessionController extends Controller {
  implicit val credentialsReads = Json.reads[Credentials]

  def get(token: String) = Action { request =>
    if (token.length() == 50) {
      Ok(Json.obj("status" -> "OK"))
    } else {
      NotFound(Json.obj("errors" -> "Invalid token."))
    }
  }

  def create = Action(BodyParsers.parse.json) { request =>
    val credentialsResult = request.body.validate[Credentials]
    credentialsResult.fold(
      errors => {
        BadRequest(Json.obj("errors" -> JsError.toJson(errors)))
      },
      credentials => {
        if (credentials.username == "admin" && credentials.password == "password") {
          Ok(Json.obj("token" -> Random.alphanumeric.take(50).mkString))
        } else {
          Unauthorized(Json.obj("errors" -> "Invalid username or password."))
        }
      }
    )
  }
}
