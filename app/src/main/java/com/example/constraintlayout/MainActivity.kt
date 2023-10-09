package com.example.constraintlayout

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import java.util.*
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtNumPessoas: EditText
    private lateinit var tvResultado: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtConta = findViewById(R.id.edtConta)
        edtNumPessoas = findViewById(R.id.edtNumPessoas)
        tvResultado = findViewById(R.id.tvResultado)

        edtConta.addTextChangedListener(this)
        edtNumPessoas.addTextChangedListener(this) // Ouvinte
        // Initialize TTS engine
        tts = TextToSpeech(this, this)
    }

    fun clickCalcular(view: View) {
        val contaValue = edtConta.text.toString().toDoubleOrNull()
        val pessoasValue = edtNumPessoas.text.toString().toIntOrNull()

        if (contaValue != null && pessoasValue != null && pessoasValue != 0) {
            val resultado = contaValue / pessoasValue
            val mensagem = "Cada pessoa deve pagar: R$ %.2f".format(resultado)
            tvResultado.text = mensagem // Define o resultado na TextView
            tts.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            tvResultado.text = "Valores inválidos. Por favor, insira números válidos."
            tts.speak("Valores inválidos. Por favor, insira números válidos.", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        val contaValue = edtConta.text.toString().toDoubleOrNull()
        val pessoasValue = edtNumPessoas.text.toString().toIntOrNull()

        if (contaValue != null && pessoasValue != null && pessoasValue != 0) {
            val resultado = contaValue / pessoasValue
            val mensagem = "Cada pessoa deve pagar: R$ %.2f".format(resultado)
            tvResultado.text = mensagem
        } else {
            tvResultado.text = "Valores inválidos. Por favor, insira números válidos."
        }
    }

    fun compartilharResultado(view: View) {
        val resultado = tvResultado.text.toString()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resultado)
            type = "text/plain"
            setPackage("com.whatsapp")
        }

        try {
            startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp não instalado.", Toast.LENGTH_SHORT).show()
        }

    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM23","Antes de mudar")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM23","Mudando")
    }


    fun clickFalar(view: View) {
        val resultado = tvResultado.text.toString()
        if (resultado.isNotEmpty()) {
            tts.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
            }
        }
}

