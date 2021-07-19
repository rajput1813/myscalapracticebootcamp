package options

object preOption {
  def filterEmployeeBysalary(employee:Employee,salary:Double):Option[Employee] ={
    if(employee.Salary>=salary)
      return Some(employee)

    None

  }
  def main(args:Array[String]){
    //  val data:Option[Int]=None
    //  println(data.getOrElse("its a None"))
    println("option example using For Comprension: ")
    val employees = List(Employee(1,"a","a@joveo.com",60000.0),Employee(2,"b","b@joveo.com",55000.0),Employee(3,"c","c@joveo.com",70000),Employee(4,"d","a@joveo.com",20000.0))
    val filterEmployees= for{employee<- employees}yield filterEmployeeBysalary(employee,50000.0)
    println(filterEmployees+s"\n")
    ///
    println("option using pattern Matching: ")
    filterEmployees.foreach(employee=>{
      employee match{
        case Some(employee)=>employee.EmployeeDetails()
        case None=>print("no details Found")
      }
    })
    println("Option of option of list of option of employee along with flatmap: ")
    val ooloe =Option(Option(filterEmployees))
    ooloe.foreach(data1=> data1.foreach(data2=> data2.foreach(emp=>{
      if(emp.getOrElse("?")!="?")
        emp.get.EmployeeDetails()
    })))


    ///


  }


}
