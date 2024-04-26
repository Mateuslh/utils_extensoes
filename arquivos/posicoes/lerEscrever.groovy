

def List buscaDadosAquivoPos(Map posicoes,ignorarDelimitador, arquivoFuncao) {
  /*
  * posicoes: deve receber um mapa sendo a chave o nome da propriedade que devemos nomear o obj, e o valor sendo um int do tamanho do campo
  * ignorarDelimitador: se receber false continua seguindo o padrão estabelicido acima, caso contrario busca a ocorrencia do delimitador no final de cada posição.
  * arquivo: instancia da classe Arquivo, utilizando a funcao Arquivo.ler()
  * return: Lista de HashMaps, sendo as chaves definidas no parametro posicoes.
  * ex :
  * Map dicionario = [
  *     e_num_ainf:6,
  *     e_tip_ainf:2,
  *     e_cpfcnpj:14
  * ]
  * def List listaArquivo = buscaDadosAquivoPos(dicionario,";",arquivo)
  */
    def linhas = []

    while (arquivoFuncao.contemProximaLinha()) {
        def linha = arquivoFuncao.lerLinha()
        def resultado = [:]
        def lastIndex = 0
        posicoes.each { chave, valor ->
            def indexFinal = lastIndex + valor
            resultado[chave] = linha.substring(lastIndex, indexFinal)
            lastIndex = indexFinal
            if(ignorarDelimitador)   lastIndex += ignorarDelimitador.size()
        }
        linhas << resultado
    }
    return linhas
}

