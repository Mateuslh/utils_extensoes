def List<String> lerArquivo(nameParamArquivo,String encoding = "iso-8859-1") {
    /*
    * NameParamArquivo: nome do arquivo definido no script
    * encoding: enconding utilizado
    * return: lista contendo strings das linhas
    * ex:
    * lerArquivo("arquivo","utf-8")
    */
    def paramArquivo = parametros.getAt(nameParamArquivo).valor
    def arquivo = Arquivo.ler(paramArquivo,"txt",[encoding: encoding])
    List linhas = []
    while (arquivo.contemProximaLinha()) {
        linhas.add(arquivo.lerLinha())
    }
    return linhas
}
