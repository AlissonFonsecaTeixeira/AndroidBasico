package tibaes.com.clculostrabalhistas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_salario_base.*

class SalarioBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario_base)

        btnCalcular.setOnClickListener{
            val salario = salarioBase.text.toString().toDouble()
            val descontos = descontos.text.toString().toDouble()
            var resultado = salario

            if (salario < 1.800){
                resultado = resultado * 0.07
            }else if (salario >= 1.800 && salario < 3.800){
                resultado = resultado * 0.15
            }else{
                resultado = resultado * 0.25
            }
            resultado = (salario - descontos) - resultado
            liquido.visibility = View.VISIBLE
            liquido.setText("Salario liquido: "+resultado.toString())

        }
    }

}
