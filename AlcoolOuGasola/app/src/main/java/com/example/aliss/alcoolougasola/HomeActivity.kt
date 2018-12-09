package com.example.aliss.alcoolougasola

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        calcular.setOnClickListener{
            val alcool = alcool.text.toString().toDouble()
            val gasolina = gasolina.text.toString().toDouble()
            val builder = AlertDialog.Builder(this@HomeActivity)
            builder.setTitle("Resultado do calculo")

            if ((gasolina * 0.7) <= alcool){
                builder.setMessage("Use Gasolina")
            }else{
                builder.setMessage("Use alcool")
            }

            builder.create()
            builder.show()
        }
    }
}
