package core

final case class UserInput(pseudo: String, password: String)

object UserInput {
  def checkCreds(pseudo: String, password: String): Either[UserIdentificationError.type, (Pseudo, Password)] = {
    (Pseudo.fromString(pseudo), Password.fromString(password)) match {
      case (Some(psd), Some(pwd)) => Right((psd, pwd))
      case _ => Left(UserIdentificationError)
    }
  }

}
