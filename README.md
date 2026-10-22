## Validador de documentos

Aplicativo Android gratuito de validação local de formato e dígitos verificadores para CPF, CNPJ (inclusive alfanumérico), PIS e RENAVAM. Os dados digitados são processados no dispositivo.

Um resultado positivo não consulta cadastros oficiais e não confirma existência, situação cadastral ou titularidade do documento. O aplicativo não gera números e não valida cartões de crédito.

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
