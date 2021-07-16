package listhandling

import scala.Console.println
import scala.io.StdIn.readInt

object ListHandling extends App{
//  val powersOfTwoClean = List[Int](8, 32, 2, 16384, 512)
//  val powersOfTwo = List[Int](8, 32, 2, 3, 16384, 512, 9)
//  // if (x + 1) of above lists is a multiple of 3
//  val (multipleOfThree, notMultipleOfThree) = ???
//  // example - 8  is 2^3 so the power value is 3. If a number is not a power of 2 print a message and discard it
//  val powerValues = ???
//  val ascendingSortedPowerValues = ???
//  // take 2nd element to n-2nd element and sum the values and print it
//  val slicedListSum : Int = ???
  // if (x + 1) of above lists is a multiple of 3
  def multipleofthree(list:List[Int])= {
  for(i<-list){
    val x=i+1;
    if((i+1)%3==0)
      println(s"$x is multiple of 3")
    else
      println(s"" +
        s"$x is not a multiple of 3")
  }
}

  def powerof2 ( x: Int): Int = {
   var cnt: Int=0
    var z =x
    var nthpower  = 0;
    while(z>0){
     // println(s"value of z is $z")
      if(z>1&&z%2==1){

        return -1
      }
      z=z/2
      nthpower=nthpower+1;
    }

      return  nthpower-1


  }
  // take 2nd element to n-2nd element and sum the values and print it
  def slicedsumvalues(list:List[Int]):Int={
    var  sum  =0
var c :Int=0;
    for(i<-list){
      c=c+1
      sum+=i;
    }

    sum=sum- list.head
    sum=sum-list(c -1)

    return sum
  }


   val n=readInt()


  var list: List[Int]=Nil
  for(i <- 1 to  n)
  { var y=readInt()
    //println(y);
   list =  list :+ y
  }
 // multipleofthree(list)
  var ascendingSortedPowerValues :List[Int] =Nil
  for(i<- list){
   var y=i
   var x=powerof2(y)
   //println(s" value of x is $x")
   if(x == -1){
     println(s"$i is not apower of two")
   }
   else{
     ascendingSortedPowerValues =ascendingSortedPowerValues :+ x
     println(s"$i is $x th power of two" )
   }
 }
  //  val ascendingSortedPowerValues = ???
  println(s"ascendingSortedPowerValues  are: $ascendingSortedPowerValues.sorted")

    var sum=slicedsumvalues(list)

    println(s"2nd element to n-2nd element sum is $sum")
}
