package com.example.aliss.imc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        altura.setOnSeekBarChangeListener(yourListener())
        peso.setOnSeekBarChangeListener(yourListener())
        idade.setOnSeekBarChangeListener(yourListener())

        btnCalcular.setOnClickListener{
            val altura = altura.progress * 0.01
            val peso = peso.getProgress()
            val resultado = peso / (altura * altura)

            var estrelas = 0

            if (resultado < 18.5){
                resultText.text = "Abaixo do peso, seu IMC é: " + resultado. toString()
                estrelas = 2
            } else if (resultado >= 18.5 && resultado < 24.9){
                resultText.text = "Saudável, seu IMC é: " + resultado.toString()
                estrelas = 5
            } else if (resultado >= 25 && resultado < 29.9){
                resultText.text = "Excesso de peso, seu IMC é: " + resultado.toString()
                estrelas = 2
            } else if (resultado >= 30) {
                resultText.text = "Obesidade, seu IMC é: " + resultado.toString()
                estrelas = 1
            }

            resultText.visibility = View.VISIBLE
            ratingBar.setIsIndicator(true)
            ratingBar.visibility = View.VISIBLE
            ratingBar.numStars = 5
            ratingBar.rating = estrelas.toFloat()
        }
    }

    private inner class yourListener : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                       fromUser: Boolean) {

            // Log the progress
            when (seekBar.getId()) {

                R.id.altura -> altura_text.setText("Altura: " + (progress * 0.01))

                R.id.peso -> peso_text.setText("Peso: " + progress)

                R.id.idade -> idade_text.setText("Idade: " + progress)

            }

        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}

        override fun onStopTrackingTouch(seekBar: SeekBar) {}

    }
}
