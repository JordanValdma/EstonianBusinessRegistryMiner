
//run with 
//scala -classpath selenium-server-andalone-2.33.0.jar grab.scala 

//val dependency = "org.seleniumhq.selenium" % 
//          "selenium-htmlunit-driver" % 
//          "2.3.1"

import java.io._
import scala.collection.JavaConversions._
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver

object Application  {
  
  def main(args: Array[String]){
    val driver = new HtmlUnitDriver(true)
    
    //query(driver, "a")
    query(driver, "b")

    //println("Title: " + driver.getTitle())

  }
  
  def query(driver:WebDriver, query:String){
    driver.get("https://ariregister.rik.ee/lihtparing.py")

    val element = driver.findElement(By.name("nimi"))

    element.sendKeys(query)

    element.submit();

    var navElements = driver.findElements(By.className("bbt"))
    navElements.add(navElements.get(0)) //just on pass the while condition
    
    
    var i = 0
    while(navElements.size() == 2){
      i = i + 1
      println("PAGE: " + i)
      var table = driver.findElement(By.className("tbl_listing"))
      readTable(table)
      navElements.get(1).click()
      navElements = driver.findElements(By.className("bbt"))
    }
    
  }
  
  def readTable(table:WebElement) {
    val allRows = table.findElements(By.tagName("tr"))
  
    //println(allRows)

    var rows = new Array[String](10)
    var i = 0
    allRows.toList.foreach{ row =>   
      val cells = row.findElements(By.tagName("td"))
      if(cells(0).getText()(0) != "N"(0)){
        rows(i) = (cells.toList.map(cell => cell.getText()).mkString("\t").replaceAll("\n", "") + "\n")
        i = i + 1
      }
    }     

    val outFile = new File("data.csv")
    printToFile(outFile)(p => {
      rows.foreach(p.write)
    })


  }
  
  def printToFile(f: java.io.File)(op: java.io.FileWriter => Unit) {
    val p = new java.io.FileWriter(f, true)
    try { op(p) } finally { p.close() }
  }

}