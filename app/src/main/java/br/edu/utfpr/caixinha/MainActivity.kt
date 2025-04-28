package br.edu.utfpr.caixinha

import android.content.Context
import android.database.sqlite.SQLiteDatabase
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

    private lateinit var banco: SQLiteDatabase

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

        banco = this.openOrCreateDatabase("banco", Context.MODE_PRIVATE, null)
        banco.execSQL("CREATE TABLE IF NOT EXISTS registros (_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo VARCHAR(1) NOT NULL, detalhe VARCHAR(20) NOT NULL, valor DECIMAL NOT NULL, data TEXT NOT NULL)")

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
            R.array.detalheArrayCredito,
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
        val tipo = spinnerTipo.selectedItem.toString()
        val detalhe = spinnerDetalhe.selectedItem.toString()
        val valor = editTextValue.text.toString().toDouble()
        val data = editTextDate.text.toString()
        val sql = "INSERT INTO registros (tipo, detalhe, valor, data) VALUES (?, ?, ?, ?)"
        val stmt = banco.compileStatement(sql)
        stmt.bindString(1, tipo)
        stmt.bindString(2, detalhe)
        stmt.bindDouble(3, valor)
        stmt.bindString(4, data)
        stmt.execute()
    }

}