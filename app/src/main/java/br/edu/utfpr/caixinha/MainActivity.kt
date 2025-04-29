package br.edu.utfpr.caixinha

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

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
        banco.execSQL("CREATE TABLE IF NOT EXISTS registros (_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT NOT NULL, detalhe TEXT NOT NULL, valor DECIMAL NOT NULL, data TEXT NOT NULL)")

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

        //Escolher a data a partir de um calendário
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        //Trocar os valores do segundo spinner de acordo com o que for selecionado no primeiro
        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTipo = spinnerTipo.selectedItem.toString()
                val detalheArrayRes = when (selectedTipo) {
                    getString(R.string.credito) -> R.array.detalheArrayCredito
                    getString(R.string.debito) -> R.array.detalheArrayDebito
                    else -> return
                }

                ArrayAdapter.createFromResource(
                    this@MainActivity,
                    detalheArrayRes,
                    android.R.layout.simple_spinner_item
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerDetalhe.adapter = this
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

    }

    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, // Context
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the date as "dd/MM/yyyy"
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editTextDate.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun handleSaldoClick() {
        //Fazer a diferença entre os créditos e débitos
        var creditos = 0.0
        var debitos = 0.0
        val cursor = banco.rawQuery("SELECT SUM(valor) FROM registros WHERE tipo='${getString(R.string.credito)}'", null)
        if (cursor.moveToFirst()) {
            creditos = cursor.getDouble(0)
        }
        cursor.close()
        val cursor2 = banco.rawQuery("SELECT SUM(valor) FROM registros WHERE tipo='${getString(R.string.debito)}'", null)
        if (cursor2.moveToFirst()) {
            debitos = cursor2.getDouble(0)
        }
        cursor2.close()
        val saldo = creditos - debitos
        //Formatar o resultado para exibir o sinal correto
        val saldoString = if (saldo >= 0) "+R$ %.2f".format(saldo) else "-R$ %.2f".format(-saldo)
        //Renderizar um dialogo com o saldo
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.saldo)
        builder.setMessage(saldoString)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun handleVerClick() {
        TODO("Not yet implemented")
    }

    private fun handleLancarClick() {
        try {
            //Ao lançar, o registro deve ser armazenado em um banco de dados SQLite.
            val tipo = spinnerTipo.selectedItem.toString()
            val detalhe = spinnerDetalhe.selectedItem.toString()
            val valor = editTextValue.text.toString().toDouble()
            val data = editTextDate.text.toString()
            banco.beginTransaction()
            try {
                val sql = "INSERT INTO registros (tipo, detalhe, valor, data) VALUES (?, ?, ?, ?)"
                val stmt = banco.compileStatement(sql)
                stmt.bindString(1, tipo)
                stmt.bindString(2, detalhe)
                stmt.bindDouble(3, valor)
                stmt.bindString(4, data)
                stmt.executeInsert()
                banco.setTransactionSuccessful()
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.ok_message,
                    Snackbar.LENGTH_SHORT
                ).show()
            } finally {
                banco.endTransaction()
            }
        } catch (e: Exception) {
            Snackbar.make(
                findViewById(android.R.id.content),
                R.string.error_message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

}