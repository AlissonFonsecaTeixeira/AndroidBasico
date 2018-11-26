package tibaes.com.clculostrabalhistas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_salario_base.*
import kotlinx.android.synthetic.main.activity_salario_por_hora.*

class SalarioPorHoraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario_por_hora)

        calcularSalarioPorHora.setOnClickListener{
            var resultado = salarioHora.text.toString().toDouble()
            resultado = (resultado / 30) / 8

            resultHorasTrabalhadas.visibility = View.VISIBLE
            resultHorasTrabalhadas.setText("Você ganha "+ resultado + " Por dia")
        }

    }
}
