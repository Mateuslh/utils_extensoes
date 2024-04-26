/*

- Exemplo utilização

*authorizationServiceLayer = [Authorization:"Bearer "+Variavel.token]
*
*bodysAlteracoes = pagamentosValidos.collect{ guia ->
*  return [
*    "idIntegracao": "${guia.debito.id}",
*    "guias": [
*      "idGerado": [
*        "id": guia.debito.id
*      ],
*      "situacao": 'PAGA'
*    ]
*  ]
*}
*alteracoes = []
*alteracoes.add([method:"PATCH",
*               url:"https://tributos.betha.cloud/service-layer-tributos/api/guias",
*               bodys:bodysAlteracoes,
*               headers: authorizationServiceLayer,
*               batchSize: 200])
*
*alteracoes.add([method:"POST",
*               url:"https://tributos.betha.cloud/service-layer-tributos/api/guias",
*               bodys:bodysAlteracoes,
*               headers: authorizationServiceLayer,
*               batchSize: 50])
*
*retornosEnvios = requestCreator(alteracoes)
*/





def makeRequest(String endPoint, body, String method, Map<String, String> headers) {
    /*
    * endPoint: EndPoint utilizado.
    * body: Corpo da requisicao.
    * method: Método.
    * headers: cabeçalhos.
    * return: Class com.betha.bfc.script.api.http.Resposta
    */
    try{
        returnRequest = Http.servico(endPoint).cabecalho(headers).METODO(method.toUpperCase(),body)
    }catch (Exception e) {
        returnRequest = Http.servico(endPoint).cabecalho(headers).METODO(method.toUpperCase(),body) // instabilidade na aplicação
    }
    return returnRequest
}


def requestCreator(List<Map>requestsBuilder){
    /*
    * requestsBuilder: Map contendo as propriedades para buildar as requisições.
    * estrutura : {
    *   method: Método (GET,OPTIONS,HEAD,TRACE,DELETE,POST,PUT,PATCH)
    *   url: EndPoint   "https://tributos.betha.cloud/service-layer-tributos/api/dividas"
    *   bodys: Lista com os registros a serem alterados, no padrão do SL
    *   headers: headers da requisição no formato Map<String, String>
    *   batchSize: Tamanho do lote, se informando 1, será encaminhado apenas 1 registro no formato obj.
    * }
    * return: {
    *   "method":metodo usado na alteração,
    *   "url": url usada na alteração,
    *   "bodys": lista dos bodys enviados,
    *   "batches": {
    *           "body": body enviado na requisição,
    *           "jsonReturned": json retornado pela requisição ( se do tipo json),
    *           "textReturned": texto do retorno,
    *    }
    * }
     */
    returnFuncion = []

    for(requestBuilder in requestsBuilder){

        def Number sizeBatch = requestBuilder.batchSize;

        if(sizeBatch == 1) {batches = requestBuilder.bodys}
        else {batches = requestBuilder.bodys.collate(sizeBatch)}

        def returnRequests = [];

        for(batch in batches){
            def returnOfRequest = makeRequest(requestBuilder.url, batch, requestBuilder.method, requestBuilder.headers);

            if(returnOfRequest.ehJson()) jsonOfRequest = returnOfRequest.json()
            else jsonOfRequest = [:]

            def bodyDTO = [
                    body:batch,
                    jsonReturned:jsonOfRequest,
                    statusReturned:returnOfRequest.codigo(),
                    textReturned:returnOfRequest.conteudo()
            ]
            returnRequests.add(bodyDTO)
        }
        def Map retornBatchs = requestBuilder
        retornBatchs.remove('headers')
        retornBatchs.remove('batchSize')
        retornBatchs.put('batches',returnRequests)
        returnFuncion.add(retornBatchs)
    }
    return returnFuncion
}