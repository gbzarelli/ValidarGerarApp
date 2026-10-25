## Validador de documentos

Aplicativo independente para validação local de formato e dígitos verificadores de CPF, CNPJ (inclusive alfanumérico), PIS/NIT e RENAVAM. Não representa nem é afiliado a órgãos governamentais. Os dados digitados são processados no dispositivo.

Um resultado positivo não consulta cadastros oficiais e não confirma existência, situação cadastral ou titularidade do documento. O aplicativo não gera números e não valida cartões de crédito. Para confirmar cadastro ou situação, use os serviços oficiais: [CPF — Receita Federal](https://www.gov.br/pt-br/servicos/consultar-cadastro-de-pessoas-fisicas), [CNPJ alfanumérico — Receita Federal](https://www.gov.br/receitafederal/pt-br/acesso-a-informacao/acoes-e-programas/programas-e-atividades/cnpj-alfanumerico/cnpj-alfa), [cálculo do dígito verificador do CNPJ — Receita Federal](https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj), [PIS/NIT — INSS](https://www.gov.br/pt-br/servicos/realizar-inscricao-no-inss) e [RENAVAM — Senatran](https://www.gov.br/pt-br/servicos/consultar-dados-de-veiculo-na-base-renavam). O app não se conecta a esses serviços nem é endossado pelos órgãos citados.

O projeto mantém somente a edição gratuita com anúncios (Free). A próxima ficha usará o novo application ID `br.com.helpdev.validador.v3`, conforme a orientação recebida na rejeição da Play. As fichas V2 rejeitadas não devem receber novos envios.

## Desenvolvimento

Use o wrapper Gradle:

- `./gradlew assembleFreeDebug`
- `./gradlew testFreeDebugUnitTest`
- `./gradlew connectedFreeDebugAndroidTest` (requer emulador ou dispositivo)

## Contribuições

Pull requests são bem-vindos. Para alterações importantes, abra uma issue antes para discutir o escopo.

## Licença

[Apache-2.0](https://choosealicense.com/licenses/apache-2.0/)
