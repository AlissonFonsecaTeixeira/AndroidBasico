package tibaes.com.clculostrabalhistas

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_fgts.*

class FGTSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fgts)

        // incluir o botão de voltar na página
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnCalcular.setOnClickListener {
            val fgts = ((etxSalarioBruto.text.toString().toDouble() * 0.8) * etxMesesTrabalhados.text.toString().toDouble()).toString()
            txtResultado.text =  fgts
        }

        // abrindo outro site
        btnVerFonte.setOnClickListener {
            val uris = Uri.parse("https://calculofgts.net/")
            val intents = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intents)
        }
    }

    // adicionando as funções nos itens do menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // caso o botão home seja selecionado (esse é o botão padrão quando inserimos o DisplayHome
        return if (item?.itemId == android.R.id.home) {
            // finalizando a activity
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
