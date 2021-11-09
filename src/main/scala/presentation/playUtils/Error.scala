package presentation.playUtils

import play.api.libs.json.{ JsObject, JsValue, Json }

object Error {

  def apply(details: String): JsObject = Json.obj("code" -> 0, "details" -> details)

  def apply(details: JsValue): JsObject = Json.obj("code" -> 0, "details" -> details)
}
