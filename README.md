## Validador de documentos

Aplicativo Android de validação local de formato e dígitos verificadores para CPF, CNPJ (inclusive alfanumérico), PIS e RENAVAM. Os dados digitados são processados no dispositivo.

Um resultado positivo não consulta cadastros oficiais e não confirma existência, situação cadastral ou titularidade do documento. O aplicativo não gera números e não valida cartões de crédito.

O projeto mantém duas flavors de distribuição: gratuita com anúncios (Free) e paga sem anúncios (Paid). Os application IDs Play existentes permanecem `br.com.helpdev.validador.v2.free` e `br.com.helpdev.validador.v2`.

## Desenvolvimento

Use o wrapper Gradle:

- `./gradlew assembleFreeDebug`
- `./gradlew assemblePaidDebug`
- `./gradlew testFreeDebugUnitTest testPaidDebugUnitTest`
- `./gradlew connectedFreeDebugAndroidTest connectedPaidDebugAndroidTest` (requer emulador ou dispositivo)

## Contribuições

Pull requests são bem-vindos. Para alterações importantes, abra uma issue antes para discutir o escopo.

## Licença

[Apache-2.0](https://choosealicense.com/licenses/apache-2.0/)
