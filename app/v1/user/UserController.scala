package v1.user

import play.api.mvc._
import play.api.libs.json._

import scala.util.Random

case class User(name: String, password: String)

/**
  * Controller for auth functionality.
  */
class UserController extends Controller {
  implicit val userReads = Json.reads[User]

  def post = Action(BodyParsers.parse.json) { request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("errors" -> JsError.toJson(errors)))
      },
      user => {
        if (user.name == "admin" && user.password == "password") {
          Ok(Json.obj("token" -> Random.alphanumeric.take(50).mkString))
        } else {
          Unauthorized(Json.obj("errors" -> "Invalid username or password."))
        }
      }
    )
  }
}
