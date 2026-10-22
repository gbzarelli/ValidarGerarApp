# Validador de documentos — Play Console checklist

## Novo escopo de atualização (22/10/2026)

As duas fichas existentes serão atualizadas, mantendo os packages V2: Free `br.com.helpdev.validador.v2.free` e Paid `br.com.helpdev.validador.v2`. O título pretendido para ambas é exatamente “Validador de documentos”. A atualização proposta usa `versionName 3.0.0` e `versionCode 2` (confirmar na Play Console que 2 supera todos os códigos já recebidos por cada app antes de qualquer envio).

O produto passa a fazer somente validações locais de formato e dígitos verificadores para CPF, CNPJ (inclusive alfanumérico), PIS e RENAVAM; remove geração e validação de cartão; não consulta ou afirma existência, situação cadastral ou titularidade. A Free permanece com anúncios e a Paid sem anúncios.

**Bloqueio de publicação confirmado na Console (22/10/2026):** as duas fichas mostram `Misleading Claims policy: Violation of Misleading Claims policy`, rejeitadas em 17/10/2026. Os detalhes da rejeição identificam as descrições completas como evidência — ambas ainda ofereciam geração de documentos/cartões — e orientam que, sem vínculo/autorização governamental, depois de corrigir o conteúdo o desenvolvedor deve enviar o app conforme a política usando **novo package name e novo app name**. Isso conflita com a decisão de manter os packages V2. Portanto não atualizar/submeter releases nessas fichas nem criar packages novos automaticamente. Solicitar decisão do responsável entre (a) obter esclarecimento escrito/appeal da Play sobre uma exceção que permita corrigir as fichas V2 ou (b) autorizar um plano de novos packages e novos registros.

**Bloqueio adicional do Paid:** a mesma instrução diz para remover taxas ou justificar detalhadamente qual serviço aprimorado justifica a cobrança por um serviço governamental gratuito. O Paid está definido em R$ 2 e, no escopo atual, só difere do Free por não exibir anúncios; não há justificativa de serviço aprimorado aprovada. Não manter/publicar a cobrança sem resolver isso. Nenhuma nova release foi enviada por esta implementação.

**Privacidade publicada desatualizada:** conferi `https://helpdev.com.br/apps/validar-gerar-documentos/privacidade/` em 22/10/2026. A página pública ainda se chama “Validar e Gerar Documentos” e descreve geração de números e cópia de resultados; ela não corresponde ao app validation-only. O rascunho local foi revisado, mas o repositório/site público não foi alterado nesta execução. Corrigir e publicar a política antes de qualquer nova ficha ou release.

## App records

Created in the Help DEV personal developer account on 2026-10-14 with Portuguese (Brazil) as the default language. The original Paid title was “Validar e Gerar Documentos”; the original Free title was “Validar e Gerar Documentos Ads”. The intended new title “Validador de documentos” has not been submitted to Play.

| Flavor | Package | Play app ID | Status |
| --- | --- | --- | --- |
| Free | `br.com.helpdev.validador.v2.free` | `4975364428850056655` | Previous production submission rejected; no replacement submitted |
| Paid | `br.com.helpdev.validador.v2` | `4975612393139412010` | Previous production submission rejected; no replacement submitted |

## Previous listing and release history

On 2026-10-14, the previous Portuguese (Brazil) listings and production releases were submitted for review. Both submissions were rejected on 2026-10-17. The policy details show that their descriptions offered number generation and the Paid described credit-card generation; those claims do not match the new validation-only scope. Previous releases were Brazil-only. No replacement AAB has been submitted.

The previous Paid listing stated that values were processed locally and there were no ads. The previous Free listing disclosed AdMob data use and linked the privacy page. Both descriptions must be rewritten for the new app before any new submission.

## Other current checks

- Play App Signing identity and Android developer verification still require a fresh check if Play confirms the old packages can be reused; otherwise setup follows the new app records.
- The previous Paid price was saved at R$ 2,00 in Brazil with automatic regional conversions. The rejection's fix guidance requires removing fees for a free government process or providing a detailed justification for an enhanced service; current Paid functionality only removes ads, so this is unresolved.
- The AdMob app `Validar e Gerar Documentos V2 Free Android` is still marked **Requires review**. Its overview showed zero requests/impressions, its Ad units page showed no data, and Privacy & messaging remained blank/loading during the 22/10/2026 inspection. Production ad readiness and consent configuration are unverified.
- The published privacy page still describes generation and clipboard copying; the updated local draft has not been published to the website repository.

There is currently no replacement release awaiting review. Do not upload or submit these AABs to the rejected records until the package/name direction, Paid price treatment, privacy page, and Free AdMob/consent gates have been resolved.
