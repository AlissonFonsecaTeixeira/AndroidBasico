package com.example.aliss.historiadeninar

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * Variáveis para trabalhar com notificações a partir da versão oero
     */
    private val channelId = "tibaes.com.historiasdeninar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val historias = arrayOf("Sandero", "Peugeot","Tucson")

        val adaptador = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, historias)

        spinnerHistorias.adapter = adaptador

        spinnerHistorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                // pegando o valor do item selecionado no spinner
                val selectedItem = parent.getItemAtPosition(position).toString()

                // para cada posição, iremos adicionar a história e uma imagem ilustrativa
                when (position) {
                    0 -> {
                        // adicionando a história no campo de texto
                        textHistoria.text =
                                (selectedItem + "\nSabemos que o Renault Sandero, assim como o Logan, foi" +
                                        " desenvolvido para ser um automóvel grande, simples e barato." +
                                        " A surpresa está em sua versão esportiva, preparada pela divisão" +
                                        " francesa Renaultsport para seguir à risca as mesmas máximas –" +
                                        " e que até ganhou elogios lá na França. ")
                        //tornando a imagem visivel
                        imgHistoria.visibility = View.VISIBLE
                        // alterando a imagem. Essa nova imagem está no pacote drawable do projeto
                        imgHistoria.setImageResource(R.drawable.sandero)
                        // chamando a notificação
                        notificacao()

                    }
                    1 -> {
                        textHistoria.text = (selectedItem + "\nEconomizar combustível é palavra de ordem no Peugeot 208." +
                                " Apesar de ter perdido 3 cv e 1 mkgf de torque em relação ao antigo 1.5, o consumo do novo" +
                                " motor 1.2 Puretech caiu em até 37%. Seguindo nosso padrão de testar veículos com gasolina," +
                                " o 208 fez 12,5 km/l na cidade e 17,2 km/l na estrada, superando outros modelos equipados" +
                                " com motor 1.0 tricilíndrico, como Fox, Ka e HB20.")
                        imgHistoria.visibility = View.VISIBLE
                        imgHistoria.setImageResource(R.drawable.peugeot)
                        notificacao()
                    }

                    2 -> {
                        textHistoria.text = (selectedItem + "\nO Tucson é como boa parte dos jovens de Seul: nasceram na Coreia do Sul" +
                                ", mas têm costumes dos americanos. Pedal de acelerador articulado, volante com aro fino, iluminação verde" +
                                " no interior e bancos pouco anatômicos remetem aos carros do segundo governo Clinton (1997-2001). Ainda assim," +
                                " esta máquina do tempo feita em Anápolis (GO) desde 2010 tem o melhor custo-benefício entre os SUVs.")
                        imgHistoria.visibility = View.VISIBLE
                        imgHistoria.setImageResource(R.drawable.tucson)
                        notificacao()
                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
//                imgHistoria.visibility = View.GONE
                textHistoria.text = ""
            }
        }
    }

    /**
     *  Trabalhando com notificações simples
     */
    fun notificacao(){
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val mNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
        } else {
            Notification.Builder(this)
        }.apply {
            setContentIntent(pendingIntent)
            // adicionando um ícone na notificação
            setSmallIcon(R.drawable.notification_icon_background)
            setAutoCancel(true)
            // título da notificação
            setContentTitle(spinnerHistorias.selectedItem.toString())
            // mensagem da notificação
            setContentText("MESSAGE")
        }.build()
        val mNotificationId: Int = 1000
        val nManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(mNotificationId, mNotification)
    }

}
