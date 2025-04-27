package br.edu.utfpr.caixinha

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerDetalhe: Spinner
    private lateinit var editTextValue: EditText
    private lateinit var editTextDate: EditText
    private lateinit var buttonLancar: Button
    private lateinit var buttonVer: Button
    private lateinit var buttonSaldo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spinnerTipo = findViewById(R.id.spinner)
        spinnerDetalhe = findViewById(R.id.spinner2)
        editTextValue = findViewById(R.id.editTextValue)
        editTextDate = findViewById(R.id.editTextDate)
        buttonLancar = findViewById(R.id.button)
        buttonVer = findViewById(R.id.button2)
        buttonSaldo = findViewById(R.id.button3)

        ArrayAdapter.createFromResource(
            this,
            R.array.tipoArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipo.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.detalheArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDetalhe.adapter = adapter
        }

        buttonLancar.setOnClickListener { handleLancarClick() }
        buttonVer.setOnClickListener { handleVerClick() }
        buttonSaldo.setOnClickListener { handleSaldoClick() }

    }

    private fun handleSaldoClick() {
        TODO("Not yet implemented")
    }

    private fun handleVerClick() {
        TODO("Not yet implemented")
    }

    private fun handleLancarClick() {
        //Ao lançar, o registro deve ser armazenado em um banco de dados SQLite.
    }
}