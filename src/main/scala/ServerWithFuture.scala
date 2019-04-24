//***** Echo

import java.net._
import java.io._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


object EchoServerWithFuture {
  def main(args: Array[String]) {
    val server = new ServerSocket(9998)
    while (true) {
      val s = server.accept()

      val f = Future {
        val in = new BufferedReader(new InputStreamReader(s.getInputStream))
        val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

        getResourceName(in, out)

        s.close()
      }
      println("2- before onComplete")
      f.onComplete{
        case Success(variable) => println(Success)
        case Failure(e) => e.fillInStackTrace()
      }
    }
      // retrieving first line of request and setting it up for parsing
    def getResourceName(in: BufferedReader, out: BufferedWriter): Unit = {

      val firstLine = in.readLine()
      val splitString = firstLine.split(" ")
      val requestVerb = splitString(0)
      val resourceName = splitString(1)

      if (requestVerb.equals("GET")) {
        val link = resourceName
        if (link.replace("/Users/tonnyhuey/IdeaProjects/server1.0/test.html"
          , "/Users/tonnyhuey/IdeaProjects/server1.0/404File.html ").equals("/")) {
          //link = "/Users/tonnyhuey/IdeaProjects/server1.0/test.html"
        }
        try {
          val reader = new BufferedReader(new FileReader(link.substring(1)))
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
            out.write("HTTP/1.1 404 FileNotFound  \r\n")
            out.write("Content-Type; text/html \r\n")
            out.write("Connection; close \r\n")
            out.write("\r\n\n")

          //out.write("SUCCESS")
        }
      }
      out.flush()
      in.close()
      out.close()
    }
  }
}

