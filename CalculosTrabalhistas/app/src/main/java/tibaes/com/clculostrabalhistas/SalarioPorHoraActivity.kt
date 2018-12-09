package tibaes.com.clculostrabalhistas

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_salario_base.*
import kotlinx.android.synthetic.main.activity_salario_por_hora.*

class SalarioPorHoraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario_por_hora)

        val historias = arrayOf("8", "6")

        val adaptador = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, historias)

        spinnerHoras.adapter = adaptador

        calcularSalarioPorHora.setOnClickListener{
            var resultado = salarioHora.text.toString().toDouble()

            resultado = (resultado / 30) / spinnerHoras.selectedItem.toString().toInt()

            resultHorasTrabalhadas.visibility = View.VISIBLE
            resultHorasTrabalhadas.setText("Você ganha "+ resultado + " por hora !")
        }

        fonte_hora.setOnClickListener {
            val uris = Uri.parse("http://www.calculadoratrabalhista.com.br/calculo-da-hora-trabalhada/")
            val intents = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intents)
        }

    }
}
