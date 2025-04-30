package br.edu.utfpr.caixinha.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.utfpr.caixinha.model.Registro

class Datasource (private val context: Context){
    fun carregarRegistros(): List<Registro> {
        val registros = mutableListOf<Registro>()
        val db: SQLiteDatabase = context.openOrCreateDatabase("banco", Context.MODE_PRIVATE, null)

        val cursor = db.rawQuery("SELECT * FROM registros", null)

        with(cursor) {
            while (moveToNext()) {
                val tipo = getString(getColumnIndexOrThrow("tipo"))
                val detalhe = getString(getColumnIndexOrThrow("detalhe"))
                val valor = getDouble(getColumnIndexOrThrow("valor"))
                val data = getString(getColumnIndexOrThrow("data"))
                registros.add(Registro(detalhe=detalhe, valor=valor, data=data, tipo=tipo))
            }
        }
        cursor.close()
        return registros
    }
}