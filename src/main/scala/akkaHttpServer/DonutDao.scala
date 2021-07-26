package akkaHttpServer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
class DonutDao {
val donutsFromDb = Vector(
  Donut("plain Donut",1.51),
  Donut("chocolate Donut",1.53),
  Donut("Glazed Donut",1.60)

)
  def fetchDonuts():Future[Donuts] = Future{
    Donuts(donutsFromDb)
  }
}
