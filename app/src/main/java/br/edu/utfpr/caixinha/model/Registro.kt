package br.edu.utfpr.caixinha.model

data class Registro(var tipo: String, var detalhe: String, var valor: Double, var data: String) {
    override fun toString(): String {
        return "$tipo - $detalhe - R$ $valor - $data"
    }
}
