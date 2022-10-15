package com.example.myapplication

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(){

    lateinit var pathHosts : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val file = File(Environment.getRootDirectory().absolutePath)
        pathHosts = file.walkTopDown().filter{
            it.absolutePath.endsWith("hosts")
        }.take(1).toList()[0].absolutePath
        reset(buttonReset)

    }

    fun save(view : View){
//        checkBash()
//        return
        val fileHosts = File(pathHosts)
        edText.text.lines().forEach{
            fileHosts.writeText(it + "\r\n")
        }
        Toast.makeText(this, "Изменения внесены.", Toast.LENGTH_SHORT).show()
        reset(buttonSave)
    }


    fun checkBash(command : String){
        edText.text.clear()
        try {
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(
                InputStreamReader(process.inputStream)
            )
            var read: Int
            val buffer = CharArray(4096)
            val output = StringBuffer()
            do {
                read = reader.read(buffer)
                if (read <= 0) break
                output.append(buffer, 0, read)
            } while(true)
            reader.close()
            // Waits for the command to finish.
            process.waitFor()
            edText.append(output.toString())
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    fun reset(view : View){
        edName.text = pathHosts
        edText.text.clear()
        File(pathHosts).readLines(Charsets.UTF_8).forEach{
            edText.append(it.toString())
            edText.append("\n")
        }
    }


}