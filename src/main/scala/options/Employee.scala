package options

case class Employee(id:Int,name:String,email:String,Salary:Double){
  def EmployeeDetails ():Unit ={
    println(s"Employee id = $id")
    println(s"Employee name = $name")
    println(s"Employee email = $email")
    println(s"Employee salary = $Salary\n")

  }

}
