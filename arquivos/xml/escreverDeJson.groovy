/*
* -- EXEMPLO DE UTILIZAÇÃO --
*json = [
*  arquivo: [
*    atributoArquivo: "atributo",
*    pai: [
*      atributoPai: "atributoPai",
*      filho: [
*      [
*        atributoFilho:"atributoFilho"
*      ],
*      [
*        atributoFilho:"atributoFilho2"
*      ],
*      [
*        atributoFilho:"atributoFilho3"
*      ]]
*    ]
*  ]
*]
*
*geraXmlJsonArquivo("ArquivoTeste.xml","iso-8859-1", json  )
*Resultado.arquivo(xml) // caso queira gerar o arquivo
*/

def geraXmlJsonArquivo(String nomeArquivo, String encoding, Map json) {
    /*

    * return: Class com.betha.bfc.script.api.arquivos.xml.XmlFileWriter

    */

    def xml = Arquivo.novo(nomeArquivo, 'xml', [indentar: 'S', "encoding":encoding]);
    if(json.keySet().size() != 1){
        suspender " O json precisa da chave pai"
    }
    xml.escreverInicioDocumento(encoding, "1.0")
    geraXmlObjeto(json, xml)
    return xml
}


def geraXmlObjeto(Map json, xml) {
  json.each { key, value ->
    if (value instanceof Map) {
      xml.escreverInicioElemento(key)
      geraXmlObjeto(value, xml)
      xml.escreverFimElemento()
    } else if (value instanceof List) {
      value.each { item ->
        if (item instanceof Map) {
          xml.escreverInicioElemento(key)
          geraXmlObjeto(item, xml)
          xml.escreverFimElemento()
        } else {
          xml.escreverAtributo(key, item)
        }
      }
    } else {
      xml.escreverAtributo(key, value)
    }
  }
}
