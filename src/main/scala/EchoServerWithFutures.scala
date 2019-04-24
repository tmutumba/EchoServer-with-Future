//***** Echo

import java.net._
import java.io._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object EchoServerWithFutures {
  def main(args: Array[String]) {
    val server = new ServerSocket(9997)
    while (true) {
      val s = server.accept()
      val in = new BufferedReader(new InputStreamReader(s.getInputStream))
      val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

      Future {
        getResourceName(in, out)
        s.close()
      }
    }
  }
  // retrieving first line of request and setting it up for parsing
  def getResourceName(in: BufferedReader, out: BufferedWriter ): Unit = {

    val firstLine = in.readLine()
    val splitString = firstLine.split(" ")
    val requestVerb = splitString(0)
    val resourceName = splitString(1)
    //System.out.print(2 + "\n")

    if (requestVerb.equals("GET")){
      var link = resourceName
      if (link.replace(" ", " ").equals("/")) {
        link = "/Users/tonnyhuey/IdeaProjects/EchoServerWithFutures/test.html"
        //System.out.println(link)
      }
      try {
        val reader = new BufferedReader(new FileReader (link.substring(1)))
        //response header
        out.write("HTTP/1.1 200 OK \r\n")
        out.write("Content-Type; text/html \r\n")
        out.write("Connection; close \r\n")
        out.write("\r\n\n")

        //out.write("SUCCESS")

        for (line <- reader.lines().toArray()) {
          out.write(line + "\r\n\n")
        }
        reader.close()
      }
      catch {
        case e: FileNotFoundException =>
          out.write("HTTP/1.1 404 FileNotFound \r\n")
          out.write("Content-Type; text/html \r\n")
          out.write("Connection; close \r\n")
          out.write("\r\n\n")

          out.write("<center>FAIL</center>")
        //out.write("<HTML><BODY><TITLE> File Not Found</H1></BODY></HTML> \")")
        //out.write("<HTML><BODY><H1><CENTER> Error 404: File Not Found</CENTER></H1></BODY></HTML>")
      }
    }
    out.flush()
    in.close()
    out.close()
  }
}